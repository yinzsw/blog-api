package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 菜单图标
 *
 * @author yinzsW
 * @since 23/03/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "菜单图标")
public class MenuIconReq {

    @NotBlank(message = "菜单图标路径不能为空")
    @Schema(title = "菜单图标绘制路径")
    private String iconPath;
}
