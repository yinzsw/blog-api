package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.core.security.oauth.impl.GithubOauth2;
import top.yinzsw.blog.model.request.LoginReq;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;
import top.yinzsw.blog.service.AuthService;

import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 用户认证控制器
 *
 * @author yinzsW
 * @since 22/12/19
 */
@Tag(name = "用户认证模块")
@Validated
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final GithubOauth2 githubOauth2;

    @Operation(summary = "用户登录")
    @PostMapping
    public UserInfoVO login(@Valid @RequestBody LoginReq loginReq) {
        return authService.login(loginReq);
    }

    @Operation(summary = "用户登录")
    @PostMapping("github")
    public UserInfoVO loginByGithub(String code) {
        return githubOauth2.authorize(code);
    }

    @Operation(summary = "刷新凭据")
    @PutMapping
    public TokenVO refresh() {
        return authService.refreshToken();
    }

    @Operation(summary = "用户注销")
    @DeleteMapping
    public boolean logout() {
        return authService.logout();
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("email/{email}")
    public boolean sendEmailCode(@Parameter(description = "邮箱", required = true)
                                 @Email(message = "邮箱格式不正确")
                                 @PathVariable("email") String email) {
        return authService.sendEmailCode(email);
    }
}
