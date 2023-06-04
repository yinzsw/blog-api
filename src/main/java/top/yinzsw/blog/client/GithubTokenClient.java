package top.yinzsw.blog.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.yinzsw.blog.model.dto.Oauth2AccessTokenDTO;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/06/01
 */
@FeignClient(url = "https://github.com", name = "github-token")
public interface GithubTokenClient {
    @PostMapping(value = "login/oauth/access_token", headers = "Accept=application/json")
    Oauth2AccessTokenDTO getAccessToken(@RequestParam("client_id") String clientId,
                                        @RequestParam("client_secret") String clientSecret,
                                        @RequestParam("code") String code);
}

