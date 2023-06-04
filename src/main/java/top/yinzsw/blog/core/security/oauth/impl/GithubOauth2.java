package top.yinzsw.blog.core.security.oauth.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.client.GithubTokenClient;
import top.yinzsw.blog.client.GithubUserClient;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.oauth.Oauth2Config;
import top.yinzsw.blog.core.security.oauth.Oauth2Strategy;
import top.yinzsw.blog.enums.AppTypeEnum;
import top.yinzsw.blog.enums.LoginTypeEnum;
import top.yinzsw.blog.manager.UserManager;
import top.yinzsw.blog.mapper.RoleMapper;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.converter.UserConverter;
import top.yinzsw.blog.model.dto.Oauth2AccessTokenDTO;
import top.yinzsw.blog.model.dto.Oauth2UserInfoDTO;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.vo.TokenVO;
import top.yinzsw.blog.model.vo.UserInfoVO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * desc
 *
 * @author yinzsW
 * @since 23/06/01
 */
@Service
@AllArgsConstructor
public class GithubOauth2 implements Oauth2Strategy {
    private final GithubTokenClient tokenClient;
    private final GithubUserClient userClient;
    private final Oauth2Config oauth2Config;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final UserManager userManager;
    private final JwtManager jwtManager;
    private final PasswordEncoder passwordEncoder;
    private final UserConverter userConverter;

    @Override
    public UserInfoVO authorize(String code) {
        Oauth2Config.Oauth2DTO oauth2DTO = oauth2Config.getSupports(LoginTypeEnum.GITHUB);
        Oauth2AccessTokenDTO accessToken = tokenClient
                .getAccessToken(oauth2DTO.getClientId(), oauth2DTO.getClientSecret(), code);

        Oauth2UserInfoDTO githubUserInfo = userClient.getUserInfo(accessToken.getBearerToken());
        String githubUserId = githubUserInfo.getId();

        UserPO userPO = Optional.ofNullable(userMapper.getUserByLoginType(githubUserId, LoginTypeEnum.GITHUB))
                .orElseGet(() -> {
                    String username = String.format("%s_%s", LoginTypeEnum.GITHUB.name(), githubUserId);
                    String password = passwordEncoder.encode(String.format("%s_%s", LoginTypeEnum.GITHUB.name(), UUID.randomUUID()));
                    UserPO user = new UserPO()
                            .setUsername(username)
                            .setPassword(password)
                            .setGithub(githubUserId)
                            .setNickname(githubUserInfo.getNickname())
                            .setAvatar(githubUserInfo.getAvatar());
                    userMapper.insert(user);
                    return userMapper.getUserByLoginType(githubUserId, LoginTypeEnum.GITHUB);
                });
        List<Long> roleIds = roleMapper.getRoleIdsByUserId(userPO.getId());

        TokenVO tokenVO = jwtManager.createToken(userPO.getId(), roleIds, AppTypeEnum.FRONT);

        // 保存登录信息
        userManager.saveUserLoginHistory(userPO.getId());
        userManager.weakUserToken(List.of(userPO.getId()));
        return userConverter.toUserInfoVO(userPO, tokenVO);
    }
}
