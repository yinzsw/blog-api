package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.MenuPO;
import top.yinzsw.blog.model.vo.MenuVO;

import java.util.List;

/**
 * 菜单统一业务处理层
 *
 * @author yinzsW
 * @since 23/03/02
 */

public interface MenuManager extends CommonManager<MenuPO> {

    List<MenuVO> listAccessibleMenusByRoleId(List<Long> roleIds);
}
