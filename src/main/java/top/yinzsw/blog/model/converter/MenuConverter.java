package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.MenuPO;
import top.yinzsw.blog.model.request.MenuReq;

/**
 * 菜单模型转换器
 *
 * @author yinzsW
 * @since 23/03/02
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuConverter {

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "orderNum", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    MenuPO toMenuPO(MenuReq menuReq);

}
