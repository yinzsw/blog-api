package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.RolePO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【role(角色表)】的数据库操作Mapper
 * @createDate 2022-12-15 14:47:44
 * @Entity top.yinzsw.blog.model.po.RolePO
 */
@CacheNamespace(readWrite = false, blocking = true)
public interface RoleMapper extends CommonMapper<RolePO> {

    /**
     * 根据关键词角色列表
     *
     * @param keywords 关键词
     * @return 角色 分页
     */
    List<RolePO> listSearchRoles(@Param("keywords") String keywords);
}




