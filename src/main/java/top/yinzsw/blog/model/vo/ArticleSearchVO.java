package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.time.LocalDateTime;

/**
 * 文章搜索
 *
 * @author yinzsW
 * @since 23/01/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章搜索")
public class ArticleSearchVO {

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
     * 文章内容摘要
     */
    @Schema(title = "文章内容摘要")
    private String articleContent;

    /**
     * 文章状态
     */
    @Schema(title = "文章类型")
    private ArticleTypeEnum articleType;

    /**
     * 发表时间
     */
    @Schema(title = "发表时间")
    private LocalDateTime createTime;
}
