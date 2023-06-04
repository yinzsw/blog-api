package top.yinzsw.blog.core.scheduled;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.enums.ActionTypeEnum;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.ViewedManager;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.ViewedPO;
import top.yinzsw.blog.util.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/09
 */
@Service
@RequiredArgsConstructor
public class WebsiteScheduled {
    private final ViewedManager viewedManager;
    private final ArticleManager articleManager;
    private final StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 30 0 * * ?")
    public void clearWebsiteViewedUserAndSaveViewCount() {
        String key = RedisConstEnum.VIEW_DAY_USERS.getKey();
        List<?> results = stringRedisTemplate.execute(CommonUtils.<String, String>getSessionCallback(operations -> {
            operations.multi();
            operations.opsForSet().size(key);
            operations.delete(key);
            return operations.exec();
        }));

        if (Objects.isNull(results) || CollectionUtils.isEmpty(results)) {
            viewedManager.save(new ViewedPO().setViewCount(0L));
            return;
        }

        Long size = Optional.ofNullable(results.get(0)).map(s -> (Long) s).orElse(0L);
        viewedManager.save(new ViewedPO().setViewCount(size));
    }

    @SuppressWarnings("unchecked")
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearArticleViewedUserAndSaveViewCount() {
        String articleViewKey = RedisConstEnum.ARTICLE_ACTION_USERS.getKey(ActionTypeEnum.VIEW.getActionName(), "*");
        String articleLikeKey = RedisConstEnum.ARTICLE_ACTION_USERS.getKey(ActionTypeEnum.LIKE.getActionName(), "*");

        List<String> articleViewKeys =
                Optional.ofNullable(stringRedisTemplate.keys(articleViewKey))
                        .<List<String>>map(ArrayList::new)
                        .orElse(Collections.emptyList());

        List<String> articleLikeKeys =
                Optional.ofNullable(stringRedisTemplate.keys(articleLikeKey))
                        .<List<String>>map(ArrayList::new)
                        .orElse(Collections.emptyList());

        List<Object> results = stringRedisTemplate.executePipelined(CommonUtils.<String, String>getSessionCallback(operations -> {
            operations.multi();
            articleViewKeys.forEach(key -> operations.opsForSet().size(key));
            operations.delete(articleViewKeys);
            operations.exec();

            operations.multi();
            articleLikeKeys.forEach(key -> operations.opsForSet().size(key));
            operations.delete(articleLikeKeys);
            operations.exec();
            return null;
        }));

        List<Long> articleViewResult = (List<Long>) results.get(0);
        List<Long> articleLikeResult = (List<Long>) results.get(1);

        List<ArticlePO> articleViewList = IntStream.range(0, articleViewKeys.size()).boxed()
                .map(idx -> {
                    Long count = articleViewResult.get(idx);
                    String key = articleViewKeys.get(idx);
                    long articleId = Long.parseLong(key.substring(key.lastIndexOf(":") + 1));
                    return new ArticlePO().setId(articleId).setViewsCount(count);
                }).collect(Collectors.toList());

        List<ArticlePO> articleLikeList = IntStream.range(0, articleLikeKeys.size()).boxed()
                .map(idx -> {
                    Long count = articleLikeResult.get(idx);
                    String key = articleLikeKeys.get(idx);
                    long articleId = Long.parseLong(key.substring(key.lastIndexOf(":") + 1));
                    return new ArticlePO().setId(articleId).setLikesCount(count);
                }).collect(Collectors.toList());

        articleViewList.addAll(articleLikeList);
        articleManager.updateBatchById(articleViewList);
    }
}
