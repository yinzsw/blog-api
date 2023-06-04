package top.yinzsw.blog.core.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.AppTypeEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JWT claims数据模型
 *
 * @author yinzsW
 * @since 22/12/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JwtTokenDTO {

    /**
     * 应用类型
     */
    private AppTypeEnum aud;

    /**
     * 用户id
     */
    private String sub;

    /**
     * 过期时间
     */
    private Long exp;

    /**
     * 版本号 ID(雪花id)
     */
    private String jti;

    /**
     * Token 签名
     */
    private String sign;

    /**
     * Token 类型
     */
    private TokenTypeEnum type;

    /**
     * 用户角色列表
     */
    private List<Long> roles;

    @JsonIgnore
    public Long getUid() {
        return Optional.ofNullable(this.sub).map(Long::parseLong).orElse(null);
    }

    @JsonIgnore
    public Map<String, ?> toMap(ObjectMapper objectMapper) {
        return objectMapper.convertValue(this, new TypeReference<>() {
        });
    }
}
