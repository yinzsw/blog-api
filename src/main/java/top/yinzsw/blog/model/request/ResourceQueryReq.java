package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 资源查询模型
 *
 * @author yinzsW
 * @since 23/05/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class ResourceQueryReq {

    /**
     * 分组名
     */
    @Parameter(description = "分组名")
    private String module;


    /**
     * 请求方法
     */
    @Parameter(description = "请求方法")
    private RequestMethod method;


    /**
     * 是否允许匿名访问
     */
    @Parameter(description = "是否允许匿名访问")
    private Boolean isAnonymous;

    /**
     * 授权角色id
     */
    @Parameter(description = "授权角色id")
    private Long roleId;
}
