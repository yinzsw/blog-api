package top.yinzsw.blog.core.security.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.security.Key;
import java.util.List;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/22
 */
@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties("jwt")
public class JwtConfig {
    /**
     * JWT密钥
     */
    private String key = "";
    /**
     * 用户刷新token的Uri 标识
     */
    private HttpMethod refreshMethod = HttpMethod.PUT;
    /**
     * 用户刷新token的Uri 标识
     */
    private String refreshUri = "/refresh";
    /**
     * 不需要认证的资源
     */
    private List<String> excludeUris = List.of();

    public Key getKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }
}
