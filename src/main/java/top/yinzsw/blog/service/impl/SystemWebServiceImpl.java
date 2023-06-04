package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.*;
import top.yinzsw.blog.mapper.*;
import top.yinzsw.blog.model.request.AboutReq;
import top.yinzsw.blog.model.request.LogQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.SystemWebService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 网站系统业务层实现
 *
 * @author yinzsW
 * @since 23/05/06
 */
@Service
@RequiredArgsConstructor
public class SystemWebServiceImpl implements SystemWebService {
    private final TagMapper tagMapper;
    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final LogOperationMapper logOperationMapper;
    private final TagManager tagManager;
    private final UserManager userManager;
    private final ViewedManager viewedManager;
    private final ArticleManager articleManager;
    private final CommentManager commentManager;
    private final CategoryManager categoryManager;
    private final SystemWebManager systemWebManager;

    @Override
    public PageVO<LogBackgroundVO> pageLogs(PageReq pageReq, LogQueryReq logQueryReq) {
        Page<LogBackgroundVO> logBackgroundVOPage = logOperationMapper.pageLogs(pageReq.getPager(), logQueryReq);
        return PageVO.getPageVO(logBackgroundVOPage);
    }

    @Override
    public boolean report() {
        systemWebManager.report();
        return true;
    }

    @Override
    public BlogHomeInfoVO getBlogHomeInfo() {
        var futureArticleCount = CompletableFuture.supplyAsync(articleManager::count);
        var futureCategoryCount = CompletableFuture.supplyAsync(categoryManager::count);
        var futureTagCount = CompletableFuture.supplyAsync(tagManager::count);
        var futureTalkCount = CompletableFuture.supplyAsync(() -> 0L);
        var futureViewCount = CompletableFuture.supplyAsync(systemWebManager::getUniqueVisitorCount);
        var futureWebsiteConfig = CompletableFuture.supplyAsync(this::getWebsiteConfig);
        return new BlogHomeInfoVO()
                .setArticleCount(futureArticleCount.join())
                .setCategoryCount(futureCategoryCount.join())
                .setTagCount(futureTagCount.join())
                .setTalkCount(futureTalkCount.join())
                .setViewCount(futureViewCount.join())
                .setConfig(futureWebsiteConfig.join());
    }

    @Override
    public BlogBackInfoVO getBlogBackInfo() {
        BlogBackInfoVO blogBackInfoVO = new BlogBackInfoVO();

        CompletableFuture<Void> countFuture = CompletableFuture.runAsync(() -> blogBackInfoVO
                .setViewsCount(systemWebManager.getUniqueVisitorCount())
                .setArticleCount(articleManager.count())
                .setUsersCount(userManager.count())
                .setMessageCount(commentManager.getMessageCount()));

        CompletableFuture<Void> statisticsDayCountFuture = CompletableFuture.runAsync(() -> blogBackInfoVO
                .setWeekViewsCount(viewedManager.listWebsiteViewCountsOfLast7Days())
                .setWeekUsersCount(userMapper.listUserCountsOfLast7Days())
                .setYearArticleCount(articleMapper.listArticleCountOfPastYear()));

        CompletableFuture<Void> statisticsArticleFuture = CompletableFuture.runAsync(() -> blogBackInfoVO
                .setTags(tagMapper.listHotPercentTagsLimit50())
                .setCategories(categoryMapper.listHotCategoriesLimit50())
                .setArticles(articleManager.listHotArticlesLimit10()));

        List.of(countFuture, statisticsDayCountFuture, statisticsArticleFuture).forEach(CompletableFuture::join);
        return blogBackInfoVO;
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        return systemWebManager.getWebSiteConfig();
    }

    @Override
    public boolean updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        return systemWebManager.updateWebsiteConfig(websiteConfigVO);
    }

    @Override
    public String getAbout() {
        return null;
    }

    @Override
    public boolean updateAbout(AboutReq aboutReq) {
        return false;
    }
}
