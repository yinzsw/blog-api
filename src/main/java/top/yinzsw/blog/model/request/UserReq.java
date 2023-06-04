package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户")
public class UserReq {

    @NotBlank(message = "邮箱不能为空")
    @Schema(title = "邮箱")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Schema(title = "验证码")
    private String code;

    @NotBlank(message = "密码不能为空")
    @Schema(title = "密码")
    private String password;
}
