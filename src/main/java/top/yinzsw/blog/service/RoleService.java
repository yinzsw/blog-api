package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.RoleAuthInfoVO;
import top.yinzsw.blog.model.vo.RoleBackgroundVO;
import top.yinzsw.blog.model.vo.RoleVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Service
 * @createDate 2022-12-15 14:47:44
 */
public interface RoleService {

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<RoleVO> listRoles();

    /**
     * 获取用户角色所有信息
     *
     * @return 用户角色列表信息
     */
    List<RoleBackgroundVO> listBackgroundRoles();

    /**
     * 获取授权信息
     *
     * @param roleId 角色id
     * @return 授权信息
     */
    RoleAuthInfoVO getAuthResourceInfo(Long roleId);

    /**
     * 更新角色禁用状态
     *
     * @param roleId     角色id
     * @param isDisabled 禁用状态
     * @return 当前值
     */
    boolean updateRoleIsDisabled(Long roleId, Boolean isDisabled);

    boolean saveOrUpdateRole(RoleReq roleReq);

    /**
     * 批量删除角色
     *
     * @param roleIds 角色id列表
     * @return 是否成功
     */
    boolean deleteRoles(List<Long> roleIds);
}
