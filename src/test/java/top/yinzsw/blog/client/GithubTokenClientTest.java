package top.yinzsw.blog.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class GithubTokenClientTest {
    @Autowired
    GithubTokenClient githubTokenClient;

    @Test
    void getGithubAccessToken() {

    }
}