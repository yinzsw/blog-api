package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.MenuManager;
import top.yinzsw.blog.mapper.MenuMapper;
import top.yinzsw.blog.model.po.MenuPO;

import java.util.List;

/**
 * 菜单统一业务处理层实现
 *
 * @author yinzsW
 * @since 23/03/02
 */
@Service
public class MenuManagerImpl extends ServiceImpl<MenuMapper, MenuPO> implements MenuManager {
    @Override
    public List<MenuPO> listAccessibleMenus(List<Long> accessibleMenuIds) {
        return lambdaQuery()
                .select(MenuPO::getId, MenuPO::getParentId, MenuPO::getName, MenuPO::getTitle, MenuPO::getIconPath)
                .in(MenuPO::getId, accessibleMenuIds)
                .eq(MenuPO::getIsHidden, false)
                .orderByAsc(MenuPO::getOrderNum)
                .list();
    }
}
