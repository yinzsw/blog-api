package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.enums.ActionTypeEnum;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.vo.ArticleHeatVO;
import top.yinzsw.blog.model.vo.ArticleRankVO;
import top.yinzsw.blog.util.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 文章数据映射模型
 *
 * @author yinzsW
 * @since 23/01/18
 */
@Service
public class ArticleManagerImpl extends ServiceImpl<ArticleMapper, ArticlePO> implements ArticleManager {
    private final StringRedisTemplate stringRedisTemplate;

    public ArticleManagerImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Map<Long, ArticleHeatVO> getArticleHotMap(List<Long> articleIds) {
        String likeActionName = ActionTypeEnum.LIKE.getActionName();
        String viewActionName = ActionTypeEnum.VIEW.getActionName();

        List<Object> results = stringRedisTemplate.executePipelined(CommonUtils.<String, String>getSessionCallback(operations -> {
            SetOperations<String, String> opsForSet = operations.opsForSet();
            articleIds.stream().map(Object::toString).forEach(id -> {
                long articleId = Long.parseLong(id);
                opsForSet.size(RedisConstEnum.ARTICLE_ACTION_USERS.getKey(likeActionName, articleId));
                opsForSet.size(RedisConstEnum.ARTICLE_ACTION_USERS.getKey(viewActionName, articleId));
            });
            return null;
        }));

        return IntStream.range(0, articleIds.size()).boxed()
                .collect(Collectors.toMap(articleIds::get, i -> {
                    Long likesCount = (Long) results.get(i * 2);
                    Long viewsCount = (Long) results.get(i * 2 + 1);
                    return new ArticleHeatVO().setLikesCount(likesCount).setViewsCount(viewsCount);
                }));
    }

    @Async
    @Override
    public void updateViewsInfo(Long articleId, String userIdentify) {
        if (Objects.isNull(articleId)) {
            return;
        }

        String key = RedisConstEnum.ARTICLE_ACTION_USERS.getKey(ActionTypeEnum.VIEW.getActionName(), articleId);
        if (SqlHelper.retBool(stringRedisTemplate.opsForSet().add(key, userIdentify))) {
            stringRedisTemplate.opsForZSet().incrementScore(RedisConstEnum.ARTICLE_HEAT_RANKING.getKey(), articleId.toString(), ActionTypeEnum.VIEW.getValue());
        }
    }

    @Override
    public List<ArticleRankVO> listHotArticlesLimit10() {
        var tuples = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(RedisConstEnum.ARTICLE_HEAT_RANKING.getKey(), 0, 10);

        if (Objects.isNull(tuples) || CollectionUtils.isEmpty(tuples)) {
            return Collections.emptyList();
        }

        Map<Long, Double> scoreMap = tuples.stream()
                .filter(typedTuple -> Objects.nonNull(typedTuple.getValue()) && Objects.nonNull(typedTuple.getScore()))
                .collect(Collectors.toMap(typedTuple -> Long.parseLong(typedTuple.getValue()), ZSetOperations.TypedTuple::getScore));

        Set<Long> articleIds = scoreMap.keySet();
        List<ArticlePO> articlePOList = lambdaQuery()
                .select(ArticlePO::getId, ArticlePO::getArticleTitle, ArticlePO::getViewsCount, ArticlePO::getLikesCount)
                .in(ArticlePO::getId, articleIds)
                .list();

        Map<Long, ArticleHeatVO> articleHotMap = getArticleHotMap(new ArrayList<>(articleIds));
        return articlePOList.stream().map(articlePO -> {
            Long id = articlePO.getId();
            Double score = scoreMap.get(id);
            ArticleHeatVO articleHeatVO = Optional.ofNullable(articleHotMap.get(id)).orElseGet(ArticleHeatVO::new);

            long viewsCount = articlePO.getViewsCount() + articleHeatVO.getViewsCount();
            long likesCount = articlePO.getLikesCount() + articleHeatVO.getLikesCount();
            return new ArticleRankVO()
                    .setHeatScore(score)
                    .setViewsCount(viewsCount)
                    .setLikesCount(likesCount)
                    .setArticleTitle(articlePO.getArticleTitle());
        }).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return lambdaQuery().eq(ArticlePO::getIsDeleted, false).count();
    }
}
