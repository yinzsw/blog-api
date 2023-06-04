package top.yinzsw.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.security.UserDetailsDTO;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.enums.AppTypeEnum;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.request.LoginReq;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;
import top.yinzsw.blog.service.AuthService;

import java.util.List;

/**
 * 用户认证业务接口实现
 *
 * @author yinzsW
 * @since 22/12/21
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleMapper roleMapper;
    private final JwtManager jwtManager;
    private final UserManager userManager;
    private final AuthenticationManager authenticationManager;
    private final UserConverter userConverter;

    @Override
    public UserInfoVO login(LoginReq loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .unauthenticated(loginReq.getUsername(), loginReq.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authenticate.getPrincipal();

        // 创建JWT和用户信息模型
        Long userId = userDetailsDTO.getId();
        List<Long> roleIds = userDetailsDTO.getRoleIds();
        TokenVO tokenVO = jwtManager.createToken(userId, roleIds, loginReq.getAppType());

        // 保存登录信息
        userManager.saveUserLoginHistory(userId);
        userManager.weakUserToken(List.of(userId));
        return userConverter.toUserInfoVO(userDetailsDTO, tokenVO);
    }

    @Override
    public TokenVO refreshToken() {
        JwtTokenDTO currentJwtTokenDTO = JwtManager.getCurrentTokenDTO()
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        Long userId = currentJwtTokenDTO.getUid();
        AppTypeEnum aud = currentJwtTokenDTO.getAud();

        List<Long> roleIds = roleMapper.getRoleIdsByUserId(userId);
        return jwtManager.createToken(userId, roleIds, aud);
    }

    @Override
    public boolean logout() {
        userManager.blockToken();
        return true;
    }

    @Override
    public boolean sendEmailCode(String email) {
        userManager.sendEmailCode(email);
        return true;
    }
}
