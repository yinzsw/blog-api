package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单数据模型
 *
 * @author yinzsW
 * @since 23/03/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "菜单数据")
public class MenuBackgroundVO {
    /**
     * id
     */
    @Schema(title = "id")
    private Long id;

    /**
     * 父菜单项id
     */
    @Schema(title = "父菜单项id")
    private Long parentId;

    /**
     * 菜单名
     */
    @Schema(title = "菜单名")
    private String name;

    /**
     * 菜单标题
     */
    @Schema(title = "菜单标题")
    private String title;

    /**
     * icon
     */
    @Schema(title = "svg绘制路径")
    private String iconPath;

    /**
     * 是否隐藏
     */
    @Schema(title = "是否隐藏")
    private Boolean isHidden;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 角色列表
     */
    @Schema(title = "角色列表")
    private List<RoleVO> roles;

    /**
     * 子菜单列表
     */
    @Schema(title = "子菜单列表", nullable = true)
    private List<MenuBackgroundVO> children;
}
