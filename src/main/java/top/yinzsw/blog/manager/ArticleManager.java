package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.vo.ArticleHeatVO;
import top.yinzsw.blog.model.vo.ArticleRankVO;

import java.util.List;
import java.util.Map;

/**
 * 文章通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/13
 */

public interface ArticleManager extends CommonManager<ArticlePO> {

    /**
     * 获取文章热度指数映射信息
     *
     * @param articleIds 文章id列表
     * @return 映射信息
     */
    Map<Long, ArticleHeatVO> getArticleHotMap(List<Long> articleIds);

    void updateViewsInfo(Long articleId, String userIdentify);

    List<ArticleRankVO> listHotArticlesLimit10();
}
