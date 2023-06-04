package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文章排名信息
 *
 * @author yinzsW
 * @since 23/05/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章排名信息")
public class ArticleRankVO {
    private String articleTitle;

    private Double heatScore;

    private Long viewsCount;

    private Long likesCount;

    private Long starCount;

    private Long forwardCount;
}
