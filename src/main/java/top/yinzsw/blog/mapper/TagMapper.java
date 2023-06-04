package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.StatisticsNameCountVO;
import top.yinzsw.blog.model.vo.TagBackgroundVO;
import top.yinzsw.blog.model.vo.TagVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Mapper
 * @createDate 2023-01-12 22:11:32
 * @Entity top.yinzsw.blog.model.po.TagPO
 */
public interface TagMapper extends CommonMapper<TagPO> {
    List<TagVO> listTags(@Param("page") PageReq pageReq, @Param("userId") Long userId);

    Long countTags(@Param("userId") Long userId);

    List<TagVO> listHotTagsLimit10(@Param("userId") Long userId);

    List<TagBackgroundVO> listBackgroundTags(@Param("page") PageReq pageReq, @Param("keywords") String keywords);

    Long countBackgroundTags(@Param("keywords") String keywords);

    boolean saveTagNamesIgnoreDuplicateKey(@Param("tags") List<String> tags);

    List<StatisticsNameCountVO> listHotPercentTagsLimit50();
}




