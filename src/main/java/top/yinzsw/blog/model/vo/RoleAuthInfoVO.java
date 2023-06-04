package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 角色授权信息
 *
 * @author yinzsW
 * @since 23/04/01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "角色授权信息")
public class RoleAuthInfoVO {

    @Schema(title = "菜单资源列表")
    private List<MenuVO> menus;

    @Schema(title = "接口资源列表")
    private List<ResourceVO> resources;
}
