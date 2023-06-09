package top.yinzsw.blog.service;

import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.model.request.*;
import top.yinzsw.blog.model.vo.*;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2022-12-15 14:14:31
 */
public interface UserService {

    List<UserSearchVO> searchUsers(String keywords);

    PageVO<UserBackgroundVO> pageBackgroundUsers(PageReq pageReq, String keywords);

    PageVO<UserOnlineVO> pageOnlineUsers(PageReq pageReq, String keywords);

    List<UserAreaVO> getUserRegionMap(Boolean isLogin);

    boolean register(UserReq userReq);

    /**
     * 更新或绑定用户邮箱
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 更新状态
     */
    boolean updateUserEmail(String email, String code);

    boolean updateUserNickname(Long userId, String nickname);

    /**
     * 更新用户禁用状态
     *
     * @param userIds    用户id
     * @param isDisabled 禁用状态
     * @return 更新状态
     */
    boolean updateUserIsDisable(List<Long> userIds, Boolean isDisabled);

    /**
     * 根据邮箱更新用户密码
     *
     * @param password 密码验证信息
     * @return 是否成功
     */
    boolean updateUserPassword(PasswordByEmailReq password);

    /**
     * 根据旧密码更新用户密码
     *
     * @param password 密码验证信息
     * @return 是否成功
     */
    boolean updateUserPassword(PasswordByOldReq password);

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像文件
     * @return 头像地址
     */
    String updateUserAvatar(MultipartFile avatar);

    /**
     * 更新用户信息
     *
     * @param userInfoReq 用户信息
     * @return 是否更新成功
     */
    boolean updateUserInfo(UserInfoReq userInfoReq);

    /**
     * 更新用户角色列表
     *
     * @param userId  用户id
     * @param roleIds 角色列表
     * @return 是否成功
     */
    boolean updateUserRoles(Long userId, List<Long> roleIds);

    boolean offlineUsers(List<Long> userIds);
}
