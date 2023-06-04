package top.yinzsw.blog.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.constant.MQConst;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.manager.UserManager;

import java.util.UUID;

/**
 * 邮箱消息队列监听器
 *
 * @author yinzsW
 * @since 23/01/01
 */
@Slf4j
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(MQConst.EMAIL_CODE_QUEUE),
        exchange = @Exchange(MQConst.EMAIL_EXCHANGE),
        key = MQConst.EMAIL_CODE_KEY))
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "blog", name = "enable-message-queue", havingValue = "true")
public class EmailCaptchaListener {
    private final JavaMailSender mailSender;
    private @Value("${spring.mail.username}") String from;
    private final StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void sendEmailVerificationCode(String email) {
        String subject = "验证码 [yinzsw Blog]";
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String verificationCodeText = String.format("您的验证码为 [%s] 有效期%d分钟，请不要告诉他人哦！", code, UserManager.USER_EMAIL_CODE_EXPIRE_TIME.toMinutes());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(verificationCodeText);
        try {
            mailSender.send(message);
            String emailCodeKey = RedisConstEnum.EMAIL_CODE.getKey(email);
            stringRedisTemplate.opsForValue().set(emailCodeKey, code, UserManager.USER_EMAIL_CODE_EXPIRE_TIME);
        } catch (MailException e) {
            log.error("验证码发送失败 接收者邮箱:{} 验证码:{}", email, code);
        }
    }
}
