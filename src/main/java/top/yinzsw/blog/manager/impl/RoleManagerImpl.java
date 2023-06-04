package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.RoleManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.po.RolePO;

/**
 * 角色通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/01/25
 */
@Service
@RequiredArgsConstructor
public class RoleManagerImpl extends ServiceImpl<RoleMapper, RolePO> implements RoleManager {

}
