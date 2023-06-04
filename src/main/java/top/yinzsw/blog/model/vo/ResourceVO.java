package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 资源
 *
 * @author yinzsW
 * @since 23/04/01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "接口资源")
public class ResourceVO {

    /**
     * 资源id
     */
    @Schema(title = "资源id")
    private Long id;

    /**
     * 分组名
     */
    @Schema(title = "分组名")
    private String module;

    /**
     * 分组名
     */
    @Schema(title = "分组名")
    private String moduleName;

    /**
     * 资源名称
     */
    @Schema(title = "资源名称")
    private String name;

    /**
     * 请求方式
     */
    @Schema(title = "请求方式")
    private String method;

    /**
     * 请求路径
     */
    @Schema(title = "请求路径")
    private String uri;

    /**
     * 是否允许匿名访问
     */
    @Schema(title = "是否允许匿名访问")
    private Boolean isAnonymous;
}
