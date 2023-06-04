package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.core.security.jwt.JwtResourceDTO;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.ResourceQueryReq;
import top.yinzsw.blog.model.vo.ResourceBackgroundVO;
import top.yinzsw.blog.model.vo.ResourceVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Mapper
 * @createDate 2022-12-15 15:00:20
 * @Entity top.yinzsw.blog.model.po.ResourcePO
 */
public interface ResourceMapper extends CommonMapper<ResourcePO> {

    List<JwtResourceDTO> listRoleResources();

    List<ResourceBackgroundVO> listBackgroundResources(@Param("page") PageReq pageReq, @Param("query") ResourceQueryReq resourceQueryReq);

    Long countBackgroundResources(@Param("query") ResourceQueryReq resourceQueryReq);

    List<ResourceVO> listAccessibleResourcesByRoleId(@Param("roleIds") List<Long> roleIds);
}




