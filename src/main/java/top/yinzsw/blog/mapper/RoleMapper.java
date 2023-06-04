package top.yinzsw.blog.mapper;

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
public interface RoleMapper extends CommonMapper<RolePO> {


    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);

}




