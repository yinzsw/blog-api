package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 文章
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章")
public class ArticleReq {
    /**
     * 文章id
     */
    @Parameter(description = "文章id")
    private Long id;

    /**
     * 文章分类
     */
    @Parameter(description = "文章分类")
    private String categoryName;

    /**
     * 文章标题
     */
    @NotBlank(message = "文章标题不能为空")
    @Parameter(description = "文章标题")
    private String articleTitle;

    /**
     * 文章内容
     */
    @NotBlank(message = "文章内容不能为空")
    @Parameter(description = "文章内容")
    private String articleContent;

    /**
     * 文章缩略图
     */
    @Parameter(description = "文章缩略图")
    private String articleCover;

    /**
     * 状态值 1.公开 2.私密 3.评论可见
     */
    @NotNull(message = "文章状态不能为空")
    @Parameter(description = "状态值: 公开 私密 评论可见")
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型 1.原创 2.转载 3.翻译
     */
    @NotNull(message = "文章类型不能为空")
    @Parameter(description = "文章类型: 原创 转载 翻译")
    private ArticleTypeEnum articleType;

    /**
     * 原文链接
     */
    @Parameter(description = "原文链接")
    private String originalUrl;

    /**
     * 是否置顶 0否 1是
     */
    @Parameter(description = "是否置顶")
    private Boolean isTop;

    /**
     * 文章标签列表
     */
    @NotNull(message = "文章标签列表不可为空")
    @Size(max = 5, message = "最多只能为文章设置{min}个标签")
    @Parameter(description = "文章标签列表")
    private List<String> tagNames;
}
