package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.TagReq;

/**
 * 标签数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagConverter {

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    TagPO toTagPO(TagReq tagReq);
}
