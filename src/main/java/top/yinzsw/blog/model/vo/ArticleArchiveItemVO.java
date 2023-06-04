package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "归档文章信息")
public class ArticleArchiveItemVO {

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

    @Schema(title = "文章内容")
    private String articleContent;

    /**
     * 文章发表时间
     */
    @Schema(title = "文章发表时间")
    private LocalDateTime createTime;
}
