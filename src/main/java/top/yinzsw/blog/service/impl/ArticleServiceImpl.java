package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.constant.CommonConst;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.core.system.HttpContext;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.manager.SystemWebManager;
import top.yinzsw.blog.manager.TagManager;
import top.yinzsw.blog.mapper.ArticleMapper;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.converter.ArticleConverter;
import top.yinzsw.blog.model.dto.QueryPreviewArticleDTO;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yinzsW
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2023-01-12 23:17:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final HttpContext httpContext;
    private final UploadProvider uploadProvider;
    private final TagMapper tagMapper;
    private final ArticleMapper articleMapper;
    private final TagManager tagManager;
    private final ArticleManager articleManager;
    private final CategoryManager categoryManager;
    private final SystemWebManager systemWebManager;
    private final ArticleConverter articleConverter;

    @Override
    public ArticleBackgroundVO getBackgroundArticle(Long articleId) {
        return articleMapper.getBackgroundArticle(articleId);
    }

    @Override
    public PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(PageReq pageReq, ArticleQueryReq articleQueryReq) {
        List<ArticleDigestBackgroundVO> backgroundArticles = articleMapper.listBackgroundArticles(pageReq, articleQueryReq);
        Long count = articleMapper.countBackgroundArticles(articleQueryReq);

        List<Long> articleIds = SimpleQuery.list2List(backgroundArticles, ArticleDigestBackgroundVO::getId);
        Map<Long, ArticleHeatVO> articleHotIndexMap = articleManager.getArticleHotMap(articleIds);
        backgroundArticles.forEach(backgroundArticle -> {
            ArticleHeatVO articleHeatVO = Optional
                    .ofNullable(articleHotIndexMap.get(backgroundArticle.getId()))
                    .orElseGet(ArticleHeatVO::new);
            backgroundArticle.setDayHot(articleHeatVO);
        });
        return PageVO.getPageVO(backgroundArticles, count);
    }

    @Override
    public ArticleVO getArticle(Long articleId) {
        // 查询文章信息
        Optional<JwtTokenDTO> currentContextDTO = JwtManager.getCurrentTokenDTO();
        Long userId = currentContextDTO.map(JwtTokenDTO::getUid).orElse(null);
        ArticleVO article = articleMapper.getArticle(articleId, userId);
        if (Objects.isNull(article)) {
            return null;
        }

        // 查询上一篇和下一篇文章
        ArticlePreviewVO prevArticle = articleMapper.getPrevArticle(articleId, userId);
        ArticlePreviewVO nextArticle = articleMapper.getNextArticle(articleId, userId);
        article.setPrevArticle(prevArticle).setNextArticle(nextArticle);

        //查询文章热度信息
        ArticleHeatVO articleHeatVO = Optional
                .ofNullable(articleManager.getArticleHotMap(List.of(articleId)).get(articleId))
                .orElseGet(ArticleHeatVO::new);
        article
                .setViewsCount(article.getViewsCount() + articleHeatVO.getViewsCount())
                .setLikesCount(article.getLikesCount() + articleHeatVO.getLikesCount());

        // 更新浏览量
        String userFlag = httpContext.getUserFlag();
        articleManager.updateViewsInfo(articleId, userFlag);
        return article;
    }

    @Override
    public List<ArticlePreviewVO> listTopArticles() {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);
        return articleMapper.listTopArticles(userId);
    }

    @Override
    public PageVO<ArticlePreviewVO> pagePreviewArticles(PageReq pageReq, QueryPreviewArticleDTO queryPreviewArticleDTO) {
        JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).ifPresent(queryPreviewArticleDTO::setUserId);
        List<ArticlePreviewVO> articlePreviewVOList = articleMapper.listArticles(pageReq, queryPreviewArticleDTO);
        Long count = articleMapper.countArticles(queryPreviewArticleDTO);
        return PageVO.getPageVO(articlePreviewVOList, count);
    }

    @Override
    public PageVO<ArticleArchiveVO> pageArchivesArticles(PageReq pageReq) {
        Page<ArticleArchiveVO> articleArchivePage = articleMapper.pageArchivesArticles(pageReq.getPager());
        return PageVO.getPageVO(articleArchivePage);
    }

    @Override
    public List<ArticleSearchVO> searchArticles(String keywords) {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);
        return articleMapper.searchArticles(keywords, userId);
    }

    @Override
    public List<String> exportArticles(List<Long> articleIds) {
        List<ArticlePO> articlePOList = articleManager.lambdaQuery()
                .select(ArticlePO::getArticleTitle, ArticlePO::getArticleContent)
                .in(ArticlePO::getId, articleIds)
                .list();
        return articlePOList.parallelStream()
                .map(articlePO -> {
                    try (InputStream stream = new ByteArrayInputStream(articlePO.getArticleContent().getBytes(StandardCharsets.UTF_8))) {
                        String filename = articlePO.getArticleTitle().concat(".md");
                        return uploadProvider.uploadFile(FilePathEnum.MARKDOWN.getPath(), filename, stream);
                    } catch (IOException e) {
                        throw new BizException("导出文章失败");
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public boolean importArticles(MultipartFile file) {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        String articleTitle = Optional.ofNullable(file.getOriginalFilename())
                .map(filename -> filename.split("\\.")[0])
                .orElse(CommonConst.UNKNOWN);

        String articleContent;
        try {
            articleContent = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new BizException("导入文章失败");
        }

        String articleCover = systemWebManager.getWebSiteConfig(WebsiteConfigVO::getDefaultArticleCover);

        ArticlePO articlePO = new ArticlePO()
                .setUserId(userId)
                .setArticleTitle(articleTitle)
                .setArticleContent(articleContent)
                .setArticleCover(articleCover)
                .setArticleType(ArticleTypeEnum.REPRINTED)
                .setArticleStatus(ArticleStatusEnum.DRAFT);
        return articleManager.save(articlePO);
    }

    @Override
    public boolean updateArticleIsLiked(Long articleId, Boolean like) {
        if (articleManager.lambdaQuery().eq(ArticlePO::getId, articleId).count() == 0L) {
            return false;
        }
        return false;
    }

    @Override
    public boolean updateArticleIsTop(Long articleId, Boolean isTop) {
        return articleManager.updateById(new ArticlePO().setId(articleId).setIsTop(isTop));
    }

    @Override
    public boolean updateArticleIsDeleted(List<Long> articleIds, Boolean isDeleted) {
        return articleManager.lambdaUpdate().set(ArticlePO::getIsDeleted, isDeleted).in(ArticlePO::getId, articleIds).update();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateArticle(ArticleReq articleReq) {
        Long uid = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        //设置文章默认封面
        if (!StringUtils.hasText(articleReq.getArticleCover())) {
            String articleCover = systemWebManager.getWebSiteConfig(WebsiteConfigVO::getDefaultArticleCover);
            articleReq.setArticleCover(articleCover);
        }

        //保存或更新草稿文章
        ArticlePO articlePO = articleConverter.toArticlePO(articleReq, uid);
        if (ArticleStatusEnum.DRAFT.equals(articleReq.getArticleStatus())) {
            return articleManager.saveOrUpdate(articlePO);
        }

        // 参数合法性校验
        if (Objects.isNull(articleReq.getCategoryName())
                || articleReq.getCategoryName().length() < 2) {
            throw new BizException("缺少分类名或分类名不合法");
        }

        if (CollectionUtils.isEmpty(articleReq.getTagNames())
                || articleReq.getTagNames().stream().anyMatch(tagName -> tagName.length() < 2)) {
            throw new BizException("缺少标签名或含有不合法的标签名");
        }

        if (!ArticleTypeEnum.ORIGINAL.equals(articleReq.getArticleType())
                && !StringUtils.hasText(articleReq.getOriginalUrl())) {
            throw new BizException("非原创文章需要注明源地址");
        }

        // 保存分类
        CategoryPO categoryPO = categoryManager.lambdaQuery()
                .eq(CategoryPO::getCategoryName, articleReq.getCategoryName())
                .oneOpt()
                .orElseGet(() -> {
                    CategoryPO category = new CategoryPO().setCategoryName(articleReq.getCategoryName());
                    categoryManager.save(category);
                    return category;
                });

        //保存或更新文章
        articleManager.saveOrUpdate(articlePO.setCategoryId(categoryPO.getId()));

        //保存标签
        tagMapper.saveTagNamesIgnoreDuplicateKey(articleReq.getTagNames());

        List<Long> tagIds = tagManager.lambdaQuery()
                .select(TagPO::getId, TagPO::getTagName)
                .in(TagPO::getTagName, articleReq.getTagNames())
                .list()
                .stream()
                .map(TagPO::getId)
                .collect(Collectors.toList());
        Db.lambdaUpdate(ArticleMtmTagPO.class).eq(ArticleMtmTagPO::getArticleId, articlePO.getId()).remove();
        return Db.saveBatch(SimpleQuery.list2List(tagIds, tagId -> new ArticleMtmTagPO().setTagId(tagId).setArticleId(articlePO.getId())));
    }

    @Override
    public boolean deleteArticles(List<Long> articleIds) {
        return articleManager.removeByIds(articleIds);
    }
}




