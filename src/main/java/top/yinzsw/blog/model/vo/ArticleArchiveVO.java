package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.YearMonth;
import java.util.List;

/**
 * 文章归档
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "文章归档")
public class ArticleArchiveVO {

    @Schema(title = "文章发表年月")
    private YearMonth date;

    @Schema(title = "文章")
    private List<ArticleArchiveItemVO> articles;
}
