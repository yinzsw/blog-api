package top.yinzsw.blog.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import top.yinzsw.blog.model.dto.Oauth2UserInfoDTO;

@FeignClient(url = "https://api.github.com", name = "github-user")
public interface GithubUserClient {

    @GetMapping("user")
    Oauth2UserInfoDTO getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}