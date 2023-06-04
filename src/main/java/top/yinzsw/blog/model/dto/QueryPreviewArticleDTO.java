package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 抽象预览文章传输模型
 *
 * @author yinzsW
 * @since 23/05/03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryPreviewArticleDTO {
    private Long userId;
    private Long categoryId;
    private Long tagId;
}
