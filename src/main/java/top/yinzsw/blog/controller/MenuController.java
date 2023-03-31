package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.MenuReq;
import top.yinzsw.blog.model.vo.MenuDataVO;
import top.yinzsw.blog.model.vo.MenuIconVO;
import top.yinzsw.blog.model.vo.MenuVO;
import top.yinzsw.blog.service.MenuService;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单模块
 *
 * @author yinzsW
 * @since 23/03/02
 */
@Tag(name = "菜单模块")
@Validated
@RestController
@RequestMapping("menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "菜单列表")
    @GetMapping
    public List<MenuDataVO> listMenus() {
        return menuService.listMenus();
    }

    @Operation(summary = "获取可访问的菜单列表")
    @GetMapping("accessible")
    public List<MenuVO> listAccessibleMenus() {
        return menuService.listAccessibleMenus();
    }

    @Operation(summary = "修改菜单标题")
    @PatchMapping("{menuId:\\d+}/title/{title}")
    public boolean updateMenuTitle(@Parameter(description = "菜单id", required = true)
                                   @PathVariable Long menuId,
                                   @Parameter(description = "菜单标题", required = true)
                                   @PathVariable("title") String title) {
        return menuService.updateMenuTitle(menuId, title);
    }

    @Operation(summary = "修改菜单隐藏状态")
    @PatchMapping("{menuId:\\d+}/isHidden/{isHidden:true|false}")
    public boolean updateMenuIsHidden(@Parameter(description = "菜单id", required = true)
                                      @PathVariable Long menuId,
                                      @Parameter(description = "是否隐藏菜单", required = true)
                                      @PathVariable("isHidden") Boolean isHidden) {
        return menuService.updateMenuIsHidden(menuId, isHidden);
    }

    @Operation(summary = "修改菜单图标")
    @PatchMapping("{menuId:\\d+}/icon")
    public boolean updateMenuIcon(@Parameter(description = "菜单id", required = true)
                                  @PathVariable Long menuId,
                                  @Valid @RequestBody MenuIconVO menuIconVO) {
        return menuService.updateMenuIcon(menuId, menuIconVO.getIconPath());
    }

    @Operation(summary = "修改菜单顺序")
    @PatchMapping("order/{menuIds:\\d+(?:,\\d+)*}")
    public boolean updateMenuOrder(@Parameter(description = "菜单id列表", required = true)
                                   @PathVariable("menuIds") List<Long> menuIds) {
        return menuService.updateMenuOrder(menuIds);
    }

    @Operation(summary = "修改菜单授权角色列表")
    @PatchMapping("{menuId:\\d+}/role/{roleIds:\\d+(?:,\\d+)*}")
    public boolean updateMenuAccessibleRoles(@Parameter(description = "菜单id", required = true)
                                             @PathVariable Long menuId,
                                             @Parameter(description = "菜单标题", required = true)
                                             @PathVariable("roleIds") List<Long> roleIds) {
        return menuService.updateMenuAccessibleRoles(menuId, roleIds);
    }

    @Operation(summary = "添加或新增菜单")
    @PutMapping
    public boolean saveOrUpdateMenu(@Valid @RequestBody MenuReq menuReq) {
        return menuService.saveOrUpdateMenu(menuReq);
    }

    @Operation(summary = "批量删除菜单")
    @DeleteMapping("{menuIds:\\d+(?:,\\d+)*}")
    public boolean deleteMenus(@Parameter(description = "菜单id列表", required = true)
                               @PathVariable("menuIds") List<Long> menuIds) {
        return menuService.deleteMenus(menuIds);
    }
}
