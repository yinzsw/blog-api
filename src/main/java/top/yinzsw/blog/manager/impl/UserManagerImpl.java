package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.config.BlogConfig;
import top.yinzsw.blog.constant.CommonConst;
import top.yinzsw.blog.constant.MQConst;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.system.HttpContext;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.util.VerifyUtils;
import ua_parser.Client;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 用户通用业务处理层实现
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagerImpl extends ServiceImpl<UserMapper, UserPO> implements UserManager {
    private final IpClient ipClient;
    private final JavaMailSender mailSender;
    private final HttpContext httpContext;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final BlogConfig blogConfig;
    private @Value("${spring.mail.username}") String from;

    @Override
    public void checkEmailVerificationCode(String email, String code) throws BizException {
        String emailCodeKey = RedisConstEnum.EMAIL_CODE.getKey(email);
        String emailCode = stringRedisTemplate.opsForValue().get(emailCodeKey);
        if (!code.equalsIgnoreCase(emailCode)) {
            throw new BizException("邮箱验证码错误");
        }
        stringRedisTemplate.delete(emailCodeKey);
    }

    @Async
    @Override
    public void sendEmailCode(String email) {
        if (blogConfig.isEnableMessageQueue()) {
            rabbitTemplate.convertAndSend(MQConst.EMAIL_EXCHANGE, MQConst.EMAIL_CODE_KEY, email);
            return;
        }
        String subject = "验证码 [yinzsw Blog]";
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String verificationCodeText = String.format("您的验证码为 [%s] 有效期%d分钟，请不要告诉他人哦！", code, USER_EMAIL_CODE_EXPIRE_TIME.toMinutes());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(verificationCodeText);
        try {
            mailSender.send(message);
            String emailCodeKey = RedisConstEnum.EMAIL_CODE.getKey(email);
            stringRedisTemplate.opsForValue().set(emailCodeKey, code, USER_EMAIL_CODE_EXPIRE_TIME);
        } catch (MailException e) {
            log.error("验证码发送失败 接收者邮箱:{} 验证码:{}", email, code);
        }
    }

    @Async
    @Override
    public void blockToken() {
        JwtManager.getCurrentTokenDTO().ifPresent(jwtTokenDTO -> {
            long tokenMaxUseSeconds = (jwtTokenDTO.getExp() - (SystemClock.now() / 1000)) + (TokenTypeEnum.REFRESH.getTtl() - TokenTypeEnum.ACCESS.getTtl());
//            stringRedisTemplate.opsForValue().set(RedisConstEnum.USER_BLOCK_TOKEN.getKey(jwtTokenDTO.getVid()), "", Duration.ofSeconds(tokenMaxUseSeconds));
        });
    }


    @Override
    public List<Long> getOnlineUserIds() {
        String onlineUserKey = RedisConstEnum.ONLINE_USER.getKey("*");
        Set<String> keys = Optional.ofNullable(stringRedisTemplate.keys(onlineUserKey)).orElse(Collections.emptySet());
        return keys.stream()
                .map(key -> Long.parseLong(key.substring(key.lastIndexOf(":") + 1)))
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public void saveOnlineUser(Long uid) {
        String key = RedisConstEnum.ONLINE_USER.getKey(uid);
        stringRedisTemplate.opsForValue().set(key, "", USER_ONLINE_DIFF_TIME);
    }

    @Async
    @Override
    public void expireUserToken(List<Long> userId, TokenTypeEnum... types) {
        String[] values = userId.stream().map(Objects::toString).toArray(String[]::new);
        for (TokenTypeEnum type : types) {
            stringRedisTemplate.opsForSet().add(RedisConstEnum.USER_TOKEN_EXPIRE.getKey(type), values);
        }
    }

    @Override
    public void weakUserToken(List<Long> userId) {
        Object[] values = userId.stream().map(Objects::toString).toArray(Object[]::new);
        for (TokenTypeEnum type : TokenTypeEnum.values()) {
            stringRedisTemplate.opsForSet().remove(RedisConstEnum.USER_TOKEN_EXPIRE.getKey(type), values);
        }
    }

    @Override
    public UserPO getUserByIdentity(String identity) {
        if (!StringUtils.hasText(identity)) {
            return null;
        }
        return Db.lambdaQuery(UserPO.class)
                .select(UserPO::getId, UserPO::getUsername, UserPO::getPassword,
                        UserPO::getEmail, UserPO::getPhone, UserPO::getNickname, UserPO::getAvatar,
                        UserPO::getIntro, UserPO::getWebsite, UserPO::getIsDisabled)
                .eq(VerifyUtils.isEmail(identity)
                        ? UserPO::getEmail
                        : (VerifyUtils.isPhone(identity) ? UserPO::getPhone : UserPO::getUsername), identity)
                .one();
    }

    @Override
    public boolean updateUserPassword(String identity, String newPassword) {
        boolean isEmail = VerifyUtils.isEmail(identity);
        return Db.lambdaUpdate(UserPO.class)
                .set(UserPO::getPassword, newPassword)
                .eq(isEmail ? UserPO::getEmail : UserPO::getUsername, identity)
                .update();
    }

    @Override
    public void saveUserLoginHistory(Long userId) {
        Client userClient = httpContext.getUserClient();
        String ipAddress = httpContext.getUserIpAddress().orElse(CommonConst.UNKNOWN);

        CompletableFuture.runAsync(() -> {
            String ipLocation = ipClient.getIpInfo(ipAddress).getFirstLocation().orElse(CommonConst.UNKNOWN);
            String deviceInfo = String.format("%s %s", userClient.os.family, userClient.os.major);
            String platformInfo = String.format("%s %s.%s.%s",
                    userClient.userAgent.family,
                    userClient.userAgent.major,
                    userClient.userAgent.minor,
                    userClient.userAgent.patch);

            var userLogin = new UserPO.UserLogin()
                    .setIp(ipAddress)
                    .setAddress(ipLocation)
                    .setTime(LocalDateTime.now())
                    .setDevice(deviceInfo)
                    .setPlatform(platformInfo);
            updateById(new UserPO().setId(userId).setLastLogin(userLogin));
        });
    }

}
