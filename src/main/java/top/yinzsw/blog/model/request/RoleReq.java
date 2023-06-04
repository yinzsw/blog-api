package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "角色模型")
public class RoleReq {

    /**
     * 角色id
     */
    @Min(value = 1, message = "不合法的标签id: ${validatedValue}")
    @Schema(title = "角色id")
    private Long id;

    /**
     * 角色名
     */
    @NotBlank(message = "角色名不能为空")
    @Length(min = 2, max = 32, message = "角色名长度不能小于{min}, 大于{max}")
    @Schema(title = "角色名")
    private String name;
}
