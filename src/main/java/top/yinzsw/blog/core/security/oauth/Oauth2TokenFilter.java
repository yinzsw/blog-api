package top.yinzsw.blog.core.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yinzsw.blog.enums.LoginTypeEnum;
import top.yinzsw.blog.exception.BizException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * jwt 用户认证授权过滤器
 *
 * @author yinzsW
 * @since 22/12/23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2TokenFilter extends OncePerRequestFilter {
    private static final String DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization/{registrationId}";
    private final Oauth2Config oauth2Config;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var authorizationRequestMatcher = new AntPathRequestMatcher(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        String registrationId = authorizationRequestMatcher.matcher(request).getVariables().get("registrationId");

        if (Objects.isNull(registrationId)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(registrationId.toUpperCase());
            Oauth2Config.Oauth2DTO oauth2DTO = oauth2Config.getSupports().get(loginTypeEnum);
            response.sendRedirect(oauth2DTO.getAuthorizeUrl());
        } catch (Exception e) {
            throw new BizException(String.format("不支持的认证方式[%s]!", registrationId));
        }
    }
}
