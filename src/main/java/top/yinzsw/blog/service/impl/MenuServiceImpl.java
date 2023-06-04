package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.MenuManager;
import top.yinzsw.blog.mapper.MenuMapper;
import top.yinzsw.blog.model.converter.MenuConverter;
import top.yinzsw.blog.model.po.MenuPO;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.request.MenuReq;
import top.yinzsw.blog.model.vo.MenuBackgroundVO;
import top.yinzsw.blog.model.vo.MenuVO;
import top.yinzsw.blog.service.MenuService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 菜单业务层实现
 *
 * @author yinzsW
 * @since 23/03/02
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuMapper menuMapper;
    private final MenuManager menuManager;
    private final MenuConverter menuConverter;

    @Override
    public List<MenuBackgroundVO> listBackgroundMenus() {
        List<MenuBackgroundVO> backgroundMenus = menuMapper.listBackgroundMenus();
        Map<Long, List<MenuBackgroundVO>> menusMap = SimpleQuery.listGroupBy(backgroundMenus, MenuBackgroundVO::getParentId);

        List<MenuBackgroundVO> menuBackgroundVOList = menusMap.get(null);
        menuBackgroundVOList.forEach(menuBackgroundVO -> menuBackgroundVO.setChildren(menusMap.get(menuBackgroundVO.getId())));
        return menuBackgroundVOList;
    }

    @Override
    public List<MenuVO> listAccessibleMenus() {
        List<Long> roleIds = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getRoles)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        return menuManager.listAccessibleMenusByRoleId(roleIds);
    }

    @Override
    public boolean updateMenuTitle(Long menuId, String title) {
        return menuManager.updateById(new MenuPO().setId(menuId).setTitle(title));
    }

    @Override
    public boolean updateMenuIsHidden(Long menuId, Boolean isHidden) {
        return menuManager.updateById(new MenuPO().setId(menuId).setIsHidden(isHidden));
    }

    @Override
    public boolean updateMenuIcon(Long menuId, String iconPath) {
        return menuManager.updateById(new MenuPO().setId(menuId).setIconPath(iconPath));
    }

    @Override
    public boolean updateMenuOrder(List<Long> menuIds) {
        List<MenuPO> menuPOList = IntStream.range(0, menuIds.size()).boxed()
                .map(idx -> new MenuPO().setId(menuIds.get(idx)).setOrderNum(idx))
                .collect(Collectors.toList());
        return menuManager.updateBatchById(menuPOList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMenuAccessibleRoles(Long menuId, List<Long> roleIds) {
        Db.lambdaUpdate(RoleMtmMenuPO.class).eq(RoleMtmMenuPO::getMenuId, menuId).remove();
        return Db.saveBatch(SimpleQuery.list2List(roleIds, roleId -> new RoleMtmMenuPO(roleId, menuId)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateMenu(MenuReq menuReq) {
        MenuPO menuPO = menuConverter.toMenuPO(menuReq);
        if (Objects.nonNull(menuPO.getParentId())) {
            if (Objects.nonNull(menuManager.getById(menuPO.getParentId()).getParentId())) {
                throw new BizException("仅支持二级菜单,不可再设置当前父菜单项");
            }
            if (Objects.nonNull(menuPO.getId())
                    && menuManager.lambdaQuery().eq(MenuPO::getParentId, menuPO.getId()).count() > 0) {
                throw new BizException("仅支持二级菜单,不可再设置当前父菜单项");
            }
        }
        menuManager.saveOrUpdate(menuPO);
        return updateMenuAccessibleRoles(menuPO.getId(), menuReq.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenus(List<Long> menuIds) {
        List<Long> ids = menuManager.lambdaQuery()
                .select(MenuPO::getId)
                .eq(MenuPO::getParentId, menuIds)
                .list()
                .stream()
                .map(MenuPO::getId)
                .collect(Collectors.toList());
        menuIds.addAll(ids);

        menuManager.removeByIds(menuIds);
        return Db.lambdaUpdate(RoleMtmMenuPO.class).in(RoleMtmMenuPO::getMenuId, menuIds).remove();
    }
}
