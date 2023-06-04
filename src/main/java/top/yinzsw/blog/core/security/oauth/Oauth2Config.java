package top.yinzsw.blog.core.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;
import top.yinzsw.blog.enums.LoginTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/06/01
 */
@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties("oauth2")
public class Oauth2Config {
    private Map<LoginTypeEnum, Oauth2DTO> supports = new HashMap<>();

    public Oauth2DTO getSupports(LoginTypeEnum typeEnum) {
        return supports.get(typeEnum);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Oauth2DTO {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String authorizeUrl;

        public String getAuthorizeUrl() {
            return UriComponentsBuilder.fromHttpUrl(authorizeUrl).queryParam("client_id", clientId).build().toUriString();
        }
    }
}
