package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.MenuReq;
import top.yinzsw.blog.model.vo.MenuDataVO;
import top.yinzsw.blog.model.vo.MenuVO;

import java.util.List;

/**
 * 菜单业务层
 *
 * @author yinzsW
 * @since 23/03/02
 */

public interface MenuService {
    /**
     * 菜单列表
     *
     * @return 菜单列表
     */
    List<MenuDataVO> listMenus();

    /**
     * 获取可访问菜单列表
     *
     * @return 菜单列表
     */
    List<MenuVO> listAccessibleMenus();

    /**
     * 修改的菜单标题
     *
     * @param menuId 菜单id
     * @param title  标题
     * @return 是否成功
     */
    boolean updateMenuTitle(Long menuId, String title);

    /**
     * 修改菜单隐藏状态
     *
     * @param menuId   菜单id
     * @param isHidden 隐藏状态
     * @return 当前状态
     */
    boolean updateMenuIsHidden(Long menuId, Boolean isHidden);

    /**
     * 修改菜单图标
     *
     * @param menuId   菜单id
     * @param iconPath 菜单图标 svg path
     * @return 是否成功
     */
    boolean updateMenuIcon(Long menuId, String iconPath);

    /**
     * 修改菜单顺序
     *
     * @param menuIds 菜单id列表
     * @return 是否成功
     */
    boolean updateMenuOrder(List<Long> menuIds);

    /**
     * 修改菜单可访问的角色
     *
     * @param menuId  菜单id
     * @param roleIds 角色id列表
     * @return 是否成功
     */
    boolean updateMenuAccessibleRoles(Long menuId, List<Long> roleIds);

    /**
     * 添加或更新菜单
     *
     * @param menuReq 菜单信息
     * @return 是否成功
     */
    boolean saveOrUpdateMenu(MenuReq menuReq);

    /**
     * 批量删除菜单
     *
     * @param menuIds 菜单id列表
     * @return 是否成功
     */
    boolean deleteMenus(List<Long> menuIds);
}
