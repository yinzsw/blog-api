package top.yinzsw.blog.core.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yinzsw.blog.manager.UserManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;
    private final UserManager userManager;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (jwtManager.isExcludeResource()) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtResourceDTO jwtResourceDTO = jwtManager.parseAndGetCurrentResource();
        authenticated(jwtResourceDTO);
        filterChain.doFilter(request, response);
    }

    private void authenticated(JwtResourceDTO resource) {
        if (Objects.isNull(resource)) {
            return;
        }
        JwtTokenDTO token = jwtManager.parseAndCurrentJwtToken(resource.getIsAnonymous());

        if (Boolean.FALSE.equals(resource.getIsAnonymous())) {
            List<Long> roles = token.getRoles();
            if (CollectionUtils.isEmpty(roles) || resource.getRoleIds().stream().noneMatch(roles::contains)) {
                throw new AccessDeniedException("权限不足, 拒绝访问!");
            }

            userManager.saveOnlineUser(token.getUid());
        }

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(token, null, null);
        authenticationToken.setDetails(resource);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
