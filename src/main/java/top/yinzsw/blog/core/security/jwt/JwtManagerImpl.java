package top.yinzsw.blog.core.security.jwt;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.pattern.PathPatternParser;
import top.yinzsw.blog.constant.CommonConst;
import top.yinzsw.blog.core.system.HttpContext;
import top.yinzsw.blog.enums.AppTypeEnum;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.enums.TokenTypeEnum;
import top.yinzsw.blog.mapper.ResourceMapper;
import top.yinzsw.blog.model.vo.TokenVO;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Json Web Token 操作实现
 *
 * @author yinzsW
 * @since 22/12/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtManagerImpl implements JwtManager {
    private final HttpContext httpContext;
    private final ObjectMapper objectMapper;
    private final ResourceMapper resourceMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final JwtConfig jwtConfig;
    private static Map<String, List<JwtResourceDTO>> mapping;

    @PostConstruct
    @Override
    public synchronized void loadResource() {
        log.info("资源加载开始...");
        List<JwtResourceDTO> jwtResourceDTOS = resourceMapper.listRoleResources();
        mapping = SimpleQuery.listGroupBy(jwtResourceDTOS, JwtResourceDTO::getModule);
        log.info("资源加载成功!");
    }

    @Override
    public boolean isExcludeResource() {
        String uri = httpContext.getRequest().getRequestURI();
        PathContainer askUriPathContainer = PathContainer.parsePath(uri);
        return jwtConfig.getExcludeUris().stream()
                .map(PathPatternParser.defaultInstance::parse)
                .anyMatch(pathPattern -> pathPattern.matches(askUriPathContainer));
    }

    @Override
    public JwtResourceDTO parseAndGetCurrentResource() {
        HttpServletRequest request = httpContext.getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        uri = UriComponentsBuilder.fromPath("/".concat(uri)).build().toUriString();
        PathContainer askUriPathContainer = PathContainer.parsePath(uri);
        if (askUriPathContainer.elements().size() < 2) {
            return null;
        }

        String module = askUriPathContainer.subPath(1, 2).value().toLowerCase();
        List<JwtResourceDTO> jwtResourceDTOList = mapping.get(module);
        if (CollectionUtils.isEmpty(jwtResourceDTOList)) {
            return null;
        }

        return jwtResourceDTOList.stream()
                .filter(jwtResourceDTO -> method.equalsIgnoreCase(jwtResourceDTO.getMethod()))
                .filter(jwtResourceDTO -> PathPatternParser.defaultInstance.parse(jwtResourceDTO.getUri()).matches(askUriPathContainer))
                .findFirst()
                .orElse(null);
    }

    @Override
    public TokenVO createToken(Long userId, List<Long> roleIds, AppTypeEnum appType) {
        long versionId = IdWorker.getId();
        String jti = "" + versionId;
        long iat = Sequence.parseIdTimestamp(versionId) / 1000;

        Key key = jwtConfig.getKey();
        String sign = getSign(key.getEncoded());

        JacksonSerializer<Map<String, ?>> serializer = new JacksonSerializer<>(objectMapper);
        Function<TokenTypeEnum, String> getTokenFn =
                tokenType -> {
                    long exp = iat + tokenType.getTtl();
                    Map<String, ?> claims = new JwtTokenDTO()
                            .setAud(appType)
                            .setSub(userId.toString())
                            .setExp(exp)
                            .setJti(jti)
                            .setSign(sign)
                            .setType(tokenType)
                            .setRoles(roleIds)
                            .toMap(objectMapper);

                    return Jwts.builder().setClaims(claims).serializeToJsonWith(serializer).signWith(key).compact();
                };

        String accessToken = getTokenFn.apply(TokenTypeEnum.ACCESS);
        String refreshToken = getTokenFn.apply(TokenTypeEnum.REFRESH);
        return new TokenVO(accessToken, refreshToken);
    }

    @Override
    public JwtTokenDTO parseAndCurrentJwtToken(Boolean isAnonymous) throws AuthenticationException {
        try {
            String token = httpContext.getBearerToken().orElseThrow(() -> new BadCredentialsException("无效的访问令牌"));

            Key jwk = jwtConfig.getKey();
            JacksonDeserializer<Map<String, ?>> serializer = new JacksonDeserializer<>(objectMapper);
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwk).deserializeJsonWith(serializer).build();

            Jws<Claims> claimsJws;
            try {
                claimsJws = jwtParser.parseClaimsJws(token);
            } catch (ExpiredJwtException e) {
                throw new CredentialsExpiredException("访问令牌已过期");
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException |
                     IllegalArgumentException e) {
                throw new BadCredentialsException("无效的访问令牌");
            }

            JwtTokenDTO jwtTokenDTO = objectMapper.convertValue(claimsJws.getBody(), JwtTokenDTO.class);
            if (!getSign(jwk.getEncoded()).equals(jwtTokenDTO.getSign())) {
                throw new BadCredentialsException("非法的访问令牌");
            }

            HttpServletRequest request = httpContext.getRequest();
            String method = request.getMethod();
            String uri = request.getRequestURI();

            boolean isRefreshUri = jwtConfig.getRefreshMethod().matches(method) && jwtConfig.getRefreshUri().equalsIgnoreCase(uri);
            TokenTypeEnum expectTokenType = isRefreshUri ? TokenTypeEnum.REFRESH : TokenTypeEnum.ACCESS;
            if (!expectTokenType.equals(jwtTokenDTO.getType())) {
                throw new BadCredentialsException("访问令牌类型不正确");
            }

            String uid = jwtTokenDTO.getUid().toString();
            if (Boolean.TRUE.equals(stringRedisTemplate.opsForSet().isMember(RedisConstEnum.USER_TOKEN_EXPIRE.getKey(jwtTokenDTO.getType()), uid))) {
                throw new CredentialsExpiredException(isRefreshUri ? "你已被下线" : "访问令牌已过期");
            }
//
//            if (isRefreshUri && Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisConstEnum.USER_BLOCK_TOKEN.getKey(jwtTokenDTO.getVid())))) {
//                throw new BadCredentialsException("访问令牌已失效");
//            }

            if (isRefreshUri) {
                stringRedisTemplate.opsForSet().remove(RedisConstEnum.USER_TOKEN_EXPIRE.getKey(TokenTypeEnum.ACCESS), uid);
            }

            return jwtTokenDTO;
        } catch (AuthenticationException e) {
            if (isAnonymous) {
                return null;
            }
            throw e;
        }
    }

    /**
     * UA+IP+jwtKey 生成新的sign
     *
     * @param keyBytes jwtKey byte array
     * @return sign
     */
    private String getSign(byte[] keyBytes) {
        String userAgent = httpContext.getUserAgent();
        String userIpAddress = httpContext.getUserIpAddress().orElse(CommonConst.UNKNOWN);

        byte[] dataBytes = String.join("", userAgent, userIpAddress).getBytes();
        byte[] originBytes = ByteUtils.concat(dataBytes, keyBytes);

        Arrays.sort(originBytes);
        return DigestUtils.md5DigestAsHex(originBytes);
    }
}
