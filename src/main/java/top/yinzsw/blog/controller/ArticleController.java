package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.extension.validation.MatchFileType;
import top.yinzsw.blog.model.dto.QueryPreviewArticleDTO;
import top.yinzsw.blog.model.request.ArticleQueryReq;
import top.yinzsw.blog.model.request.ArticleReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.ArticleService;

import javax.validation.Valid;
import java.util.List;

/**
 * 文章控制器
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Tag(name = "文章模块")
@Validated
@RestController
@RequestMapping("article")
@RequiredArgsConstructor
public class ArticleController {
    private final UploadProvider uploadProvider;
    private final ArticleService articleService;

    @Operation(summary = "查看文章详情(后台)")
    @GetMapping("background/{articleId:\\d+}")
    public ArticleBackgroundVO getBackgroundArticle(@Parameter(description = "文章id", required = true)
                                                    @PathVariable("articleId") Long articleId) {
        return articleService.getBackgroundArticle(articleId);
    }

    @Operation(summary = "查看文章列表(后台)")
    @GetMapping("background")
    public PageVO<ArticleDigestBackgroundVO> pageBackgroundArticles(@Valid PageReq pageReq,
                                                                    @Valid ArticleQueryReq articleQueryReq) {
        return articleService.pageBackgroundArticles(pageReq, articleQueryReq);
    }

    @Operation(summary = "查看文章详情")
    @GetMapping("{articleId:\\d+}")
    public ArticleVO getArticle(@Parameter(description = "文章id", required = true)
                                @PathVariable("articleId") Long articleId) {
        return articleService.getArticle(articleId);
    }

    @Operation(summary = "查看置顶文章")
    @GetMapping("/top")
    public List<ArticlePreviewVO> listTopArticles() {
        return articleService.listTopArticles();
    }

    @Operation(summary = "查看文章预览")
    @GetMapping
    public PageVO<ArticlePreviewVO> pagePreviewArticles(@Valid PageReq pageReq) {
        return articleService.pagePreviewArticles(pageReq, new QueryPreviewArticleDTO());
    }

    @Operation(summary = "查看文章预览(分类ID)")
    @GetMapping("category/{categoryId:\\d+}")
    public PageVO<ArticlePreviewVO> pagePreviewArticlesByCategoryId(@Valid PageReq pageReq,
                                                                    @Parameter(description = "分类id", required = true)
                                                                    @PathVariable("categoryId") Long categoryId) {
        return articleService.pagePreviewArticles(pageReq, new QueryPreviewArticleDTO().setCategoryId(categoryId));
    }

    @Operation(summary = "查看文章预览(标签ID)")
    @GetMapping("tag/{tagId:\\d+}")
    public PageVO<ArticlePreviewVO> pagePreviewArticlesByTagId(@Valid PageReq pageReq,
                                                               @Parameter(description = "标签id", required = true)
                                                               @PathVariable("tagId") Long tagId) {
        return articleService.pagePreviewArticles(pageReq, new QueryPreviewArticleDTO().setTagId(tagId));
    }

    @Operation(summary = "查看文章归档")
    @GetMapping("archives")
    public PageVO<ArticleArchiveVO> pageArchivesArticles(@Valid PageReq pageReq) {
        return articleService.pageArchivesArticles(pageReq);
    }

    @Operation(summary = "搜索文章")
    @GetMapping("search")
    public List<ArticleSearchVO> searchArticles(@Parameter(description = "搜索关键字", required = true)
                                                @RequestParam(value = "keywords", required = false) String keywords) {
        return articleService.searchArticles(keywords);
    }

    @Operation(summary = "上传文章封面")
    @PostMapping(value = "articleCover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadArticleCover(@Parameter(description = "文章封面", required = true)
                                     @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                     @RequestPart("file") MultipartFile file) {
        return uploadProvider.uploadFile(FilePathEnum.COVER.getPath(), file);
    }

    @Operation(summary = "上传文章图片")
    @PostMapping(value = "articleImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadArticleImage(@Parameter(description = "文章图片", required = true)
                                     @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                     @RequestPart("file") MultipartFile file) {
        return uploadProvider.uploadFile(FilePathEnum.ARTICLE.getPath(), file);
    }

    @Operation(summary = "导出文章")
    @PostMapping("export/{articleIds:\\d+(?:,\\d+)*}")
    public List<String> exportArticles(@PathVariable List<Long> articleIds) {
        return articleService.exportArticles(articleIds);
    }

    @Operation(summary = "导入文章")
    @PostMapping("import")
    public boolean importArticles(@Parameter(description = "文章", required = true)
                                  @MatchFileType(mimeType = "text/*", message = "仅支持上传文本类型{mimeType}的文件")
                                  @RequestPart("file") MultipartFile file) {
        return articleService.importArticles(file);
    }

    @Deprecated
    @Operation(summary = "点赞文章")
    @PatchMapping("{articleId:\\d+}/isLiked/{isLiked:true|false}")
    public boolean updateArticleIsLiked(@Parameter(description = "文章id", required = true)
                                        @PathVariable("articleId") Long articleId,
                                        @Parameter(description = "点赞状态", required = true)
                                        @PathVariable("isLiked") Boolean isLiked) {
        return articleService.updateArticleIsLiked(articleId, isLiked);
    }

    @Operation(summary = "修改文章置顶状态")
    @PatchMapping("{articleId:\\d+}/isTop/{isTop:true|false}")
    public boolean updateArticleIsTop(@Parameter(description = "文章id", required = true)
                                      @PathVariable("articleId") Long articleId,
                                      @Parameter(description = "是否置顶", required = true)
                                      @PathVariable("isTop") Boolean isTop) {
        return articleService.updateArticleIsTop(articleId, isTop);
    }

    @Operation(summary = "恢复或删除文章")
    @PatchMapping("{articleIds:\\d+(?:,\\d+)*}/isDeleted/{isDeleted:true|false}")
    public boolean updateArticleIsDeleted(@Parameter(description = "文章id列表", required = true)
                                          @PathVariable("articleIds") List<Long> articleIds,
                                          @Parameter(description = "是否删除", required = true)
                                          @PathVariable("isDeleted") Boolean isDeleted) {
        return articleService.updateArticleIsDeleted(articleIds, isDeleted);
    }

    @Operation(summary = "添加或修改文章")
    @PutMapping
    public boolean saveOrUpdateArticle(@Valid @RequestBody ArticleReq articleReq) {
        return articleService.saveOrUpdateArticle(articleReq);
    }

    @Operation(summary = "物理删除文章")
    @DeleteMapping("{articleIds:\\d+(?:,\\d+)*}")
    public boolean deleteArticles(@Parameter(description = "文章id列表", required = true)
                                  @PathVariable("articleIds") List<Long> articleIds) {
        return articleService.deleteArticles(articleIds);
    }
}
