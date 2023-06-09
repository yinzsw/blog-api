package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import top.yinzsw.blog.enums.AppTypeEnum;

/**
 * 用户登录
 *
 * @author yinzsW
 * @since 22/12/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户登录模型")
public class LoginReq {

    /**
     * 用户名
     */
    @Length(min = 5, max = 64, message = "用户名长度至少{min}位!")
    @Schema(title = "用户名")
    private String username;

    /**
     * 密码
     */
    @Length(min = 6, max = 64, message = "密码长度至少{min}位!")
    @Schema(title = "密码")
    private String password;

    @Schema(title = "应用类型")
    private AppTypeEnum appType = AppTypeEnum.FRONT;
}
