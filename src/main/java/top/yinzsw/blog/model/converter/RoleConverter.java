package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.RolePO;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.RoleBackgroundVO;
import top.yinzsw.blog.model.vo.RoleVO;

import java.util.List;

/**
 * 角色数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleConverter {

    List<RoleVO> toRoleDigestVO(List<RolePO> rolePOList);

    List<RoleBackgroundVO> toRoleBackgroundVO(List<RolePO> rolePOList);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDisabled", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    RolePO toRolePO(RoleReq roleReq);
}

