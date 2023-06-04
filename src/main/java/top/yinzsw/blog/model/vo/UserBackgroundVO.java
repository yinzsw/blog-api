package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.model.po.UserPO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台用户
 *
 * @author yinzsW
 * @since 23/05/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "后台用户")
public class UserBackgroundVO {
    /**
     * 用户账号id
     */
    @Schema(title = "用户id")
    private Long id;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(title = "用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @Schema(title = "用户头像")
    private String avatar;

    /**
     * 个人网站
     */
    @Schema(title = "个人网站")
    private String website;

    /**
     * 是否禁用
     */
    @Schema(title = "是否禁用")
    private Boolean isDisabled;

    @Schema(title = "上次登录信息")
    private UserPO.UserLogin lastLogin;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "角色列表")
    private List<RoleVO> roles;
}
