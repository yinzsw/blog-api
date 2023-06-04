package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.manager.MenuManager;
import top.yinzsw.blog.mapper.MenuMapper;
import top.yinzsw.blog.model.po.MenuPO;
import top.yinzsw.blog.model.vo.MenuVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 菜单统一业务处理层实现
 *
 * @author yinzsW
 * @since 23/03/02
 */
@Service
@RequiredArgsConstructor
public class MenuManagerImpl extends ServiceImpl<MenuMapper, MenuPO> implements MenuManager {
    private final MenuMapper menuMapper;

    @Override
    public List<MenuVO> listAccessibleMenusByRoleId(List<Long> roleIds) {
        List<MenuVO> accessibleMenus = menuMapper.listAccessibleMenus(roleIds);
        Map<Long, List<MenuVO>> menusMap = SimpleQuery.listGroupBy(accessibleMenus, MenuVO::getParentId);

        List<MenuVO> menuVOList = menusMap.get(null);
        if (CollectionUtils.isEmpty(menuVOList)) {
            return Collections.emptyList();
        }

        menuVOList.forEach(menuVO -> menuVO.setSubMenus(menusMap.get(menuVO.getId())));
        return menuVOList;
    }
}
