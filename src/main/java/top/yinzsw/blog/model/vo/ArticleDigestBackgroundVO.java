package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台文章摘要
 *
 * @author yinzsW
 * @since 23/01/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "后台文章摘要")
public class ArticleDigestBackgroundVO {
    /**
     * 文章id
     */
    @Schema(title = "文章id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(title = "文章标题")
    private String articleTitle;

    /**
     * 文章封面
     */
    @Schema(title = "文章封面")
    private String articleCover;

    /**
     * 文章分类id
     */
    @Schema(title = "文章分类id")
    private Long categoryId;

    /**
     * 文章分类名
     */
    @Schema(title = "文章分类名")
    private String categoryName;

    /**
     * 文章标签
     */
    @Schema(title = "文章标签")
    private List<TagVO> tags;

    /**
     * 文章状态
     */
    @Schema(title = "文章状态")
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型
     */
    @Schema(title = "文章类型")
    private ArticleTypeEnum articleType;

    /**
     * 是否置顶
     */
    @Schema(title = "是否置顶")
    private Boolean isTop;

    /**
     * 发表时间
     */
    @Schema(title = "发表时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 点赞量
     */
    @Schema(title = "点赞量")
    private Long likesCount;

    /**
     * 浏览量
     */
    @Schema(title = "浏览量")
    private Long viewsCount;

    /**
     * 当日热度
     */
    @Schema(title = "当日热度")
    private ArticleHeatVO dayHot;
}
