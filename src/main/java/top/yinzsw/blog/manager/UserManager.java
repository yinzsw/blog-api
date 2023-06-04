package top.yinzsw.blog.manager;

import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.UserPO;

import java.time.Duration;
import java.util.List;

/**
 * 用户通用业务处理层
 *
 * @author yinzsW
 * @since 22/12/25
 */

public interface UserManager extends CommonManager<UserPO> {
    Duration USER_EMAIL_CODE_EXPIRE_TIME = Duration.ofMinutes(15);
    Duration USER_ONLINE_DIFF_TIME = Duration.ofMinutes(15);

    /**
     * 校验邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     */
    void checkEmailVerificationCode(String email, String code) throws BizException;

    void sendEmailCode(String email);

    void blockToken();

    List<Long> getOnlineUserIds();

    void saveOnlineUser(Long uid);

    void expireUserToken(List<Long> userId, TokenTypeEnum... types);

    void weakUserToken(List<Long> userId);


/////////////////////////////////////////////////////MYSQL//////////////////////////////////////////////////////////////

    /**
     * 根据用标志查询用户
     *
     * @param identity 用户身份标识字符串
     * @return 用户
     */
    UserPO getUserByIdentity(String identity);

    /**
     * 根据用户名或邮箱修改密码
     *
     * @param identity    用户身份标识字符串
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean updateUserPassword(String identity, String newPassword);

    /**
     * 保存用户登录历史
     *
     * @param userId 用户id
     */
    void saveUserLoginHistory(Long userId);
}
