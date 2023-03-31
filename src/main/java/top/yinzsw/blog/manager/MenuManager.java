package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.MenuPO;

import java.util.List;

/**
 * 菜单统一业务处理层
 *
 * @author yinzsW
 * @since 23/03/02
 */

public interface MenuManager extends CommonManager<MenuPO> {

    /**
     * 获取可以访问的菜单列表
     *
     * @param accessibleMenuIds 可访问菜单id
     * @return 菜单列表
     */
    List<MenuPO> listAccessibleMenus(List<Long> accessibleMenuIds);
}
