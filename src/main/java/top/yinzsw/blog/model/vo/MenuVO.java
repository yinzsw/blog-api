package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yinzsW
 * @since 23/03/02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "菜单")
public class MenuVO {

    /**
     * id
     */
    @Schema(title = "id")
    private Long id;

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
     * 子菜单列表
     */
    @Schema(title = "子菜单列表", nullable = true)
    private List<MenuVO> subMenus;
}
