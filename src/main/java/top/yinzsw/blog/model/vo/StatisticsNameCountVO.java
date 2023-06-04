package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "数量统计模型")
public class StatisticsNameCountVO {

    @Schema(title = "数据名")
    private String name;

    @Schema(title = "数量")
    private Long count;
}
