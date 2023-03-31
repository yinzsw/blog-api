package top.yinzsw.blog.model.converter;

import org.mapstruct.*;
import top.yinzsw.blog.model.dto.GroupMenuDTO;
import top.yinzsw.blog.model.po.MenuPO;
import top.yinzsw.blog.model.request.MenuReq;
import top.yinzsw.blog.model.vo.MenuDataVO;
import top.yinzsw.blog.model.vo.MenuVO;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 菜单模型转换器
 *
 * @author yinzsW
 * @since 23/03/02
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MenuConverter {
    List<MenuDataVO> toMenuDataVO(List<GroupMenuDTO> groupMenuDTOList, @Context Map<Long, List<GroupMenuDTO>> menusMap);

    List<MenuVO> toMenuVO(List<MenuPO> menuPOList, @Context Map<Long, List<MenuPO>> menusMap);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "orderNum", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    MenuPO toMenuPO(MenuReq menuReq);


    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator1(GroupMenuDTO origin,
                                  @Context Map<Long, List<GroupMenuDTO>> menusMap,
                                  @TargetType Class<T> targetType) {

        Long menuId = origin.getMenuId();
        Long parentId = origin.getMenuId();
        List<GroupMenuDTO> children = menusMap.get(parentId);

        if (targetType.isAssignableFrom(MenuDataVO.class)) {
            return (T) new MenuDataVO().setId(menuId).setChildren(toMenuDataVO(children, Collections.emptyMap()));
        }

        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @ObjectFactory
    default <T> T defaultCreator2(MenuPO origin,
                                  @Context Map<Long, List<MenuPO>> menusMap,
                                  @TargetType Class<T> targetType) {
        Long parentId = origin.getId();
        List<MenuPO> children = menusMap.get(parentId);

        if (targetType.isAssignableFrom(MenuVO.class)) {
            return (T) new MenuVO().setSubMenus(toMenuVO(children, Collections.emptyMap()));
        }

        throw new UnsupportedOperationException();
    }
}
