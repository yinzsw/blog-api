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
 * @since 23/05/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "资源模块模型")
public class ResourceModuleVO {
    /**
     * 资源模块
     */
    @Schema(title = "资源模块")
    private String module;

    /**
     * 模块名
     */
    @Schema(title = "模块名")
    private String moduleName;
}
