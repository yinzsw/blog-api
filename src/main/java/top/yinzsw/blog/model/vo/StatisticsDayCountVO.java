package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 每日统计模型
 *
 * @author yinzsW
 * @since 23/05/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "每日次数统计模型")
public class StatisticsDayCountVO {

    @Schema(title = "数量")
    private Long count;

    @Schema(title = "日期")
    private LocalDate date;
}
