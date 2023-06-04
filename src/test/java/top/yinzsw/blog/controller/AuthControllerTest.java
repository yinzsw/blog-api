package top.yinzsw.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import top.yinzsw.blog.model.request.LoginReq;
import top.yinzsw.blog.model.vo.UserInfoVO;
import top.yinzsw.blog.service.AuthService;

@SpringBootTest
@Slf4j
public class AuthControllerTest {
    @Autowired
    private AuthService authService;

    @Test
    void loginSuccess() {
        UserInfoVO yinzsw = authService.login(new LoginReq().setUsername("yinzsw").setPassword(""));
        Assertions.assertNotNull(yinzsw, "用户信息未找到");
    }

    @Test
    void loginFail() {
        Assertions.assertThrows(AuthenticationException.class, () -> authService.login(new LoginReq().setUsername("yinzsw").setPassword("")));
    }
}