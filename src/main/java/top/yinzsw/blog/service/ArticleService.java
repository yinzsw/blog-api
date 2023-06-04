package top.yinzsw.blog.service;

import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.model.dto.QueryPreviewArticleDTO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service
 * @createDate 2023-01-12 23:17:07
 */
public interface ArticleService {

    ArticleBackgroundVO getBackgroundArticle(Long articleId);

    PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(PageReq pageReq, ArticleQueryReq articleQueryReq);

    ArticleVO getArticle(Long articleId);

    List<ArticlePreviewVO> listTopArticles();

    PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, QueryPreviewArticleDTO queryPreviewArticleDTO);

    PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq);

    List<ArticleSearchVO> searchArticles(String keywords);

    List<String> exportArticles(List<Long> articleIds);

    boolean importArticles(MultipartFile file);

    boolean updateArticleIsLiked(Long articleId, Boolean like);

    boolean updateArticleIsTop(Long articleId, Boolean isTop);

    boolean updateArticleIsDeleted(List<Long> articleIds, Boolean isDeleted);

    boolean saveOrUpdateArticle(ArticleReq articleReq);

    boolean deleteArticles(List<Long> articleIds);
}
