package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户地区分布模型
 *
 * @author yinzsW
 * @since 23/05/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户地区分布模型")
public class UserAreaVO {

    @Schema(title = "地区名称")
    private String name;

    @Schema(title = "用户数量")
    private Long value;
}
