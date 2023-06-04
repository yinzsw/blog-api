package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.MenuManager;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.mapper.ResourceMapper;
import top.yinzsw.blog.model.converter.RoleConverter;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.RoleService;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Service实现
 * @createDate 2022-12-15 14:47:44
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl implements RoleService {
    private final ResourceMapper resourceMapper;
    private final RoleManager roleManager;
    private final MenuManager menuManager;
    private final RoleConverter roleConverter;

    @Override
    public List<RoleVO> listRoles() {
        List<RolePO> roles = roleManager.lambdaQuery()
                .select(RolePO::getId, RolePO::getName)
                .list();
        return roleConverter.toRoleDigestVO(roles);
    }

    @Override
    public List<RoleBackgroundVO> listBackgroundRoles() {
        List<RolePO> roles = roleManager.lambdaQuery()
                .select(RolePO::getId, RolePO::getName, RolePO::getIsDisabled, RolePO::getCreateTime, RolePO::getUpdateTime)
                .list();
        return roleConverter.toRoleBackgroundVO(roles);
    }

    @Override
    public RoleAuthInfoVO getAuthResourceInfo(Long roleId) {
        List<MenuVO> menuVOList = menuManager.listAccessibleMenusByRoleId(List.of(roleId));
        List<ResourceVO> resourceVOList = resourceMapper.listAccessibleResourcesByRoleId(List.of(roleId));
        return new RoleAuthInfoVO().setMenus(menuVOList).setResources(resourceVOList);
    }

    @Override
    public boolean updateRoleIsDisabled(Long roleId, Boolean isDisabled) {
        return roleManager.updateById(new RolePO().setId(roleId).setIsDisabled(isDisabled));
    }

    @Override
    public boolean saveOrUpdateRole(RoleReq roleReq) {
        RolePO rolePO = roleConverter.toRolePO(roleReq);
        return roleManager.saveOrUpdate(rolePO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRoles(List<Long> roleIds) {
        // 防止删除角色后, 导致部分用户无法正常使用
        boolean isUsing = Db.lambdaQuery(UserMtmRolePO.class).in(UserMtmRolePO::getRoleId, roleIds).count() > 0L;
        if (isUsing) {
            throw new BizException("该角色下存在用户, 删除失败");
        }

        Db.lambdaUpdate(RoleMtmMenuPO.class).in(RoleMtmMenuPO::getRoleId, roleIds).remove();
        Db.lambdaUpdate(RoleMtmResourcePO.class).in(RoleMtmResourcePO::getRoleId, roleIds).remove();
        return roleManager.lambdaUpdate().in(RolePO::getId, roleIds).remove();
    }
}




