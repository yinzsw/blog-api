package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.dto.GroupMenuDTO;
import top.yinzsw.blog.model.po.MenuPO;

import java.util.List;

@CacheNamespace(readWrite = false, blocking = true)
public interface MenuMapper extends CommonMapper<MenuPO> {
    /**
     * 获取所有菜单列表
     *
     * @return 菜单列表
     */
    List<GroupMenuDTO> listMenus();
}