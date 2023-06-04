package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.dto.QueryPreviewArticleDTO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Mapper
 * @createDate 2023-01-12 23:17:07
 * @Entity top.yinzsw.blog.model.po.ArticlePO
 */
public interface ArticleMapper extends CommonMapper<ArticlePO> {

    ArticleBackgroundVO getBackgroundArticle(@Param("articleId") Long articleId);

    List<ArticleDigestBackgroundVO> listBackgroundArticles(@Param("page") PageReq pageReq, @Param("query") ArticleQueryReq query);

    Long countBackgroundArticles(@Param("query") ArticleQueryReq articleQueryReq);

    ArticleVO getArticle(@Param("articleId") Long articleId, @Param("userId") Long userId);

    ArticlePreviewVO getPrevArticle(@Param("articleId") Long articleId, @Param("userId") Long userId);

    ArticlePreviewVO getNextArticle(@Param("articleId") Long articleId, @Param("userId") Long userId);

    List<ArticlePreviewVO> listTopArticles(@Param("userId") Long userId);

    boolean incrementViewCount(@Param("articleId") Long articleId, @Param("count") long count);

    List<ArticlePreviewVO> listArticles(@Param("page") PageReq pageReq, @Param("query") QueryPreviewArticleDTO queryPreviewArticleDTO);

    Long countArticles(@Param("query") QueryPreviewArticleDTO queryPreviewArticleDTO);

    Page<ArticleArchiveVO> pageArchivesArticles(Page<ArticleArchiveVO> pager);

    List<ArticleSearchVO> searchArticles(@Param("keywords") String keywords, @Param("userId") Long userId);

    List<StatisticsDayCountVO> listArticleCountOfPastYear();
}




