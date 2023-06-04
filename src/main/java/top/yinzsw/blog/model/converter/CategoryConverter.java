package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.vo.CategoryVO;

/**
 * 分类数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryConverter {

    @Mapping(target = "articleCount", ignore = true)
    CategoryVO toCategoryVO(CategoryPO categoryPO);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    CategoryPO toCategoryPO(CategoryReq categoryReq);
}
