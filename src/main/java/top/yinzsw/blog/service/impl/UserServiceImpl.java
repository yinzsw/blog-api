package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.SystemWebManager;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.*;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:14:31
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserManager userManager;
    private final UserConverter userConverter;
    private final UploadProvider uploadProvider;
    private final PasswordEncoder passwordEncoder;
    private final SystemWebManager systemWebManager;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<UserSearchVO> searchUsers(String keywords) {
        return userMapper.searchUsers(keywords);
    }

    @Override
    public PageVO<UserBackgroundVO> pageBackgroundUsers(PageReq pageReq, String keywords) {
        List<UserBackgroundVO> backgroundVOList = userMapper.listBackgroundUsers(pageReq, keywords);
        Long count = userMapper.countBackgroundUsers(keywords);
        return PageVO.getPageVO(backgroundVOList, count);
    }

    @Override
    public PageVO<UserOnlineVO> pageOnlineUsers(PageReq pageReq, String keywords) {
        List<Long> ids = userManager.getOnlineUserIds();
        if (CollectionUtils.isEmpty(ids)) {
            return PageVO.getEmptyPageVO();
        }
        Page<UserOnlineVO> pageOnlineUsers = userMapper.pageOnlineUsers(pageReq.getPager(), ids, keywords);
        return PageVO.getPageVO(pageOnlineUsers);
    }

    @Override
    public List<UserAreaVO> getUserRegionMap(Boolean isLogin) {
        Map<Object, Object> userArea = stringRedisTemplate.opsForHash().entries(RedisConstEnum.VIEW_AREA.getKey(isLogin));
        return userArea.entrySet().stream()
                .map(entry -> new UserAreaVO()
                        .setName(entry.getKey().toString())
                        .setValue(Long.parseLong(entry.getValue().toString())))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(UserReq userReq) {
        userManager.checkEmailVerificationCode(userReq.getEmail(), userReq.getCode());

        String username = userManager.lambdaQuery()
                .select(UserPO::getId)
                .orderByDesc(UserPO::getId)
                .last("LIMIT 1")
                .oneOpt()
                .map(UserPO::getId)
                .map(Objects::toString)
                .orElse(String.format("用户%s", SystemClock.now()));
        String nickName = userReq.getEmail().split("@")[0];
        String avatar = systemWebManager.getWebSiteConfig(WebsiteConfigVO::getDefaultUserAvatar);
        UserPO userPO = new UserPO()
                .setUsername(username)
                .setNickname(nickName)
                .setAvatar(avatar)
                .setEmail(userReq.getEmail())
                .setPassword("{noop}" + userReq.getPassword());
        try {
            userManager.save(userPO);
            userManager.saveUserLoginHistory(userPO.getId());
        } catch (DuplicateKeyException e) {
            throw new BizException("用户名或邮箱已存在");
        }
        return Db.save(new UserMtmRolePO().setUserId(userPO.getId()).setRoleId(2L));
    }

    @Override
    public boolean updateUserEmail(String email, String code) {
        userManager.checkEmailVerificationCode(email, code);
        Long uid = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        try {

            userManager.updateById(new UserPO().setId(uid).setEmail(email));
        } catch (DuplicateKeyException e) {
            throw new BizException("邮箱已注册, 请先解绑");
        }
        return true;
    }

    @Override
    public boolean updateUserNickname(Long userId, String nickname) {
        return userManager.updateById(new UserPO().setId(userId).setNickname(nickname));
    }

    @Override
    public boolean updateUserIsDisable(List<Long> userIds, Boolean isDisabled) {
        return userManager.lambdaUpdate().set(UserPO::getIsDisabled, isDisabled).in(UserPO::getId, userIds).update();
    }

    @Override
    public boolean updateUserPassword(PasswordByEmailReq password) {
        Optional.ofNullable(userManager.getUserByIdentity(password.getEmail()))
                .orElseThrow(() -> new BizException("邮箱尚未注册"));

        userManager.checkEmailVerificationCode(password.getEmail(), password.getCode());
        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return userManager.updateUserPassword(password.getEmail(), newPassword);
    }

    @Override
    public boolean updateUserPassword(PasswordByOldReq password) {
        Long uid = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        String oldPassword = userManager.lambdaQuery()
                .select(UserPO::getPassword)
                .eq(UserPO::getId, uid)
                .one()
                .getPassword();
        if (!passwordEncoder.matches(password.getOldPassword(), oldPassword)) {
            throw new BizException("旧密码不正确");
        }

        String newPassword = passwordEncoder.encode(password.getNewPassword());
        return userManager.lambdaUpdate().set(UserPO::getPassword, newPassword).eq(UserPO::getId, uid).update();
    }

    @Override
    public String updateUserAvatar(MultipartFile avatar) {
        String avatarUrl = uploadProvider.uploadFile(FilePathEnum.AVATAR.getPath(), avatar);
        Long uid = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        userManager.updateById(new UserPO().setId(uid).setAvatar(avatarUrl));
        return avatarUrl;
    }

    @Override
    public boolean updateUserInfo(UserInfoReq userInfoReq) {
        Long uid = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));
        UserPO userPO = userConverter.toUserPO(userInfoReq, uid);
        return userManager.updateById(userPO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserRoles(Long userId, List<Long> roleIds) {
        Db.lambdaUpdate(UserMtmRolePO.class).eq(UserMtmRolePO::getUserId, userId).remove();
        boolean isSaved = Db.saveBatch(SimpleQuery.list2List(roleIds, roleId -> new UserMtmRolePO(userId, roleId)));
        if (isSaved) {
            userManager.expireUserToken(List.of(userId), TokenTypeEnum.ACCESS);
        }
        return isSaved;
    }

    @Override
    public boolean offlineUsers(List<Long> userIds) {
        userManager.expireUserToken(userIds, TokenTypeEnum.ACCESS, TokenTypeEnum.REFRESH);
        return true;
    }
}




