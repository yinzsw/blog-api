package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.request.ArticleReq;

/**
 * 文章数据模型转换器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArticleConverter {

    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "viewsCount", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "likesCount", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    ArticlePO toArticlePO(ArticleReq articleReq, Long userId);

}
