package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单参数
 *
 * @author yinzsW
 * @since 23/03/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "菜单")
public class MenuReq {
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
    @NotBlank(message = "菜单名不能为空")
    @Schema(title = "菜单名")
    private String name;

    /**
     * 菜单标题
     */
    @NotBlank(message = "菜单标题不能为空")
    @Schema(title = "菜单标题")
    private String title;

    /**
     * icon
     */
    @Schema(title = "图标不能为空")
    private String iconPath = "";

    /**
     * 是否隐藏
     */
    @Schema(title = "是否隐藏")
    private Boolean isHidden;

    /**
     * 角色id列表
     */
    @NotEmpty(message = "角色列表不能为空")
    @Schema(title = "角色id列表")
    private List<@NotNull(message = "角色id不能位null") Long> roleIds;
}
