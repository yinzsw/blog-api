package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryBackgroundVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.StatisticsNameCountVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Mapper
 * @createDate 2023-01-13 09:57:14
 * @Entity top.yinzsw.blog.model.po.CategoryPO
 */

public interface CategoryMapper extends CommonMapper<CategoryPO> {

    List<CategoryVO> listCategories(@Param("page") PageReq pageReq, @Param("userId") Long userId);

    Long countCategories(Long userId);

    List<CategoryVO> listHotCategoriesLimit10(@Param("userId") Long userId);

    List<CategoryBackgroundVO> listBackgroundCategories(@Param("page") PageReq pageReq, @Param("keywords") String keywords);

    Long countBackgroundCategories(@Param("keywords") String keywords);

    List<StatisticsNameCountVO> listHotCategoriesLimit50();
}