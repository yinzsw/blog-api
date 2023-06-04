package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.vo.ResourceModuleVO;

import java.util.List;

/**
 * 资源模型转换器
 *
 * @author yinzsW
 * @since 23/04/01
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceConverter {
    List<ResourceModuleVO> toResourceModuleVO(List<ResourcePO> resourcePOList);
}
