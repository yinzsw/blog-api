package top.yinzsw.blog.core.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JwtResourceDTO {
    private Long id;

    /**
     * 权限路径
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 分组名
     */
    private String module;

    /**
     * 是否匿名访问 0否 1是
     */
    private Boolean isAnonymous;

    private List<Long> roleIds;
}
