package top.yinzsw.blog.core.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import top.yinzsw.blog.enums.AppTypeEnum;
import top.yinzsw.blog.model.vo.TokenVO;

import java.util.List;
import java.util.Optional;

/**
 * Json Web Token 操作接口
 *
 * @author yinzsW
 * @since 22/12/21
 */

public interface JwtManager {

    void loadResource();

    boolean isExcludeResource();

    JwtResourceDTO parseAndGetCurrentResource();

    /**
     * 根据用户id创建access token 与 refresh token
     *
     * @param userId  用户id
     * @param roleIds 角色列表
     * @param appType 应用类型
     * @return token信息
     */
    TokenVO createToken(Long userId, List<Long> roleIds, AppTypeEnum appType);

    /**
     * 解析 http request token信息
     *
     * @return token信息
     * @throws AuthenticationException 认证异常
     */
    JwtTokenDTO parseAndCurrentJwtToken(Boolean isAnonymous) throws AuthenticationException;

    /**
     * 得到当前用户认证信息上下文
     *
     * @return 用户认证信息上下文
     */
    static Optional<JwtTokenDTO> getCurrentTokenDTO() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JwtTokenDTO jwtTokenDTO = principal instanceof JwtTokenDTO ? (JwtTokenDTO) principal : null;
        return Optional.ofNullable(jwtTokenDTO);
    }

    /**
     * 获取当前资源id
     *
     * @return 资源id
     */
    static Optional<JwtResourceDTO> getCurrentResourceDTO() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        JwtResourceDTO jwtResourceDTO = details instanceof JwtResourceDTO ? (JwtResourceDTO) details : null;
        return Optional.ofNullable(jwtResourceDTO);
    }
}
