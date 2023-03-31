package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.converter.RoleConverter;
import top.yinzsw.blog.model.dto.RoleMapsDTO;
import top.yinzsw.blog.model.po.RoleMtmMenuPO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.po.UserMtmRolePO;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.RoleBackgroundVO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.service.RoleService;
import top.yinzsw.blog.util.MapQueryUtils;

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
    private final RoleMapper roleMapper;
    private final RoleManager roleManager;
    private final RoleConverter roleConverter;

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        List<Long> roleIds = MapQueryUtils.create(UserMtmRolePO::getUserId, List.of(userId))
                .getValues(UserMtmRolePO::getRoleId);
        return roleManager.getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<Long> getRoleIdsByResourceId(Long resourceId) {
        List<Long> roleIds = MapQueryUtils.create(RoleMtmResourcePO::getResourceId, List.of(resourceId))
                .getValues(RoleMtmResourcePO::getRoleId);
        return roleManager.getEnabledRoleNamesByIds(roleIds);
    }

    @Override
    public List<RoleVO> listSearchRoleVO(String keywords) {
        List<RolePO> roles = roleMapper.listSearchRoles(keywords);
        return roleConverter.toRoleDigestVO(roles);
    }

    @Override
    public List<RoleBackgroundVO> listBackgroundRoles(String keywords) {
        List<RolePO> roles = roleMapper.listSearchRoles(keywords);

        RoleMapsDTO roleMapsDTO = roleManager.getRoleMapsDTO(SimpleQuery.list2List(roles, RolePO::getId));
        return roleConverter.toRoleBackgroundVO(roles, roleMapsDTO);
    }

    @Override
    public boolean updateRoleIsDisabled(Long roleId, Boolean isDisabled) {
        return roleManager.lambdaUpdate().set(RolePO::getIsDisabled, isDisabled).eq(RolePO::getId, roleId).update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateRole(RoleReq roleReq) {
        Long existCount = roleManager.lambdaQuery()
                .eq(RolePO::getRoleName, roleReq.getRoleName()).or()
                .eq(RolePO::getRoleLabel, roleReq.getRoleLabel())
                .count();
        if (existCount > 0) {
            throw new BizException(String.format("角色名 %s/%s 已经存在, 请更换", roleReq.getRoleName(), roleReq.getRoleLabel()));
        }

        RolePO rolePO = new RolePO()
                .setId(roleReq.getRoleId())
                .setRoleName(roleReq.getRoleName())
                .setRoleLabel(roleReq.getRoleLabel());
        roleManager.saveOrUpdate(rolePO);

        //建立角色与资源和菜单的映射关系
        Long roleId = rolePO.getId();
        Db.lambdaUpdate(RoleMtmMenuPO.class).eq(RoleMtmMenuPO::getRoleId, roleId).remove();
        Db.lambdaUpdate(RoleMtmResourcePO.class).eq(RoleMtmResourcePO::getRoleId, roleId).remove();
        Db.saveBatch(SimpleQuery.list2List(roleReq.getMenuIds(), menuId -> new RoleMtmMenuPO(roleId, menuId)));
        Db.saveBatch(SimpleQuery.list2List(roleReq.getResourceIds(), resourceId -> new RoleMtmResourcePO(roleId, resourceId)));
        return true;
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




