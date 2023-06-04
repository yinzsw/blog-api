package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.MenuPO;
import top.yinzsw.blog.model.vo.MenuBackgroundVO;
import top.yinzsw.blog.model.vo.MenuVO;

import java.util.List;

public interface MenuMapper extends CommonMapper<MenuPO> {
    /**
     * 获取所有菜单列表
     *
     * @return 菜单列表
     */
    List<MenuBackgroundVO> listBackgroundMenus();

    List<MenuVO> listAccessibleMenus(@Param("roleIds") List<Long> roleIds);
}