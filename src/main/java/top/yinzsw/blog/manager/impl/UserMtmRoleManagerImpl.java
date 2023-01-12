package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.UserMtmRoleManager;
import top.yinzsw.blog.mapper.UserMtmRoleMapper;
import top.yinzsw.blog.model.po.UserMtmRolePO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户,角色关联表 通用处理层实现
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Service
public class UserMtmRoleManagerImpl extends ServiceImpl<UserMtmRoleMapper, UserMtmRolePO> implements UserMtmRoleManager {
    @Override
    public List<Long> listRoleIdsByUserId(Long userId) {
        List<UserMtmRolePO> userMtmRolePOList = lambdaQuery()
                .select(UserMtmRolePO::getRoleId)
                .eq(UserMtmRolePO::getUserId, userId)
                .list();

        return userMtmRolePOList.stream().map(UserMtmRolePO::getRoleId).collect(Collectors.toList());
    }

    @Override
    public Long countUserByRoleId(List<Long> roleIdList) {
        return lambdaQuery()
                .select(UserMtmRolePO::getUserId)
                .in(UserMtmRolePO::getRoleId, roleIdList)
                .count();
    }
}