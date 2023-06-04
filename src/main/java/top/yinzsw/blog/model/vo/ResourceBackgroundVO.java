package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台接口资源模型
 *
 * @author yinzsW
 * @since 23/05/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "后台接口资源模型")
public class ResourceBackgroundVO {

    /**
     * 资源id
     */
    @Schema(title = "资源id")
    private Long id;

    @Schema(title = "资源模块")
    private String module;

    @Schema(title = "资源模块名")
    private String moduleName;

    /**
     * 资源名称
     */
    @Schema(title = "资源名称")
    private String name;

    /**
     * 资源描述
     */
    @Schema(title = "资源描述")
    private String description;

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
     * 是否匿名访问 0否 1是
     */
    @Schema(title = "是否允许匿名访问")
    private Boolean isAnonymous;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;

    /**
     * 授权角色列表
     */
    @Schema(title = "授权角色列表")
    private List<RoleVO> roles;
}
