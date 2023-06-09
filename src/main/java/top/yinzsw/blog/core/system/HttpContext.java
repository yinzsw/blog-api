package top.yinzsw.blog.core.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import top.yinzsw.blog.constant.CommonConst;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.model.vo.ResponseVO;
import top.yinzsw.blog.util.VerifyUtils;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * HTTP 上下文
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Service
@RequiredArgsConstructor
public class HttpContext {
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final ObjectMapper objectMapper;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Parser UA_PARSER = new Parser();

    public HttpServletRequest getRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getResponse() {
        return httpServletResponse;
    }

    /**
     * <p>X-Forwarded-For: (client1,proxy1,proxy2)由Squid开发的字段, 只有在通过了HTTP代理或者负载均衡服务器时才会添加该项</p>
     * <p>X-Real-IP: Nginx代理一般会加上此请求头</p>
     * <p>Proxy-Client-IP(HTTP代理)/WL-Proxy-Client-IP(WebLogic插件): 一般是经过Apache HTTP服务器的请求才会加上的请求头</p>
     *
     * @return ip地址
     */
    public Optional<String> getUserIpAddress() {
        Supplier<String> localHostAddress = () -> {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                return null;
            }
        };
        return Stream.of("X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "REMOTE_ADDR")
                .map(httpServletRequest::getHeader)
                .filter(Objects::nonNull)
                .map(ip -> ip.split(",")[0].strip())
                .filter(VerifyUtils::isIpv4)
                .findFirst()
                .or(() -> {
                    String remoteAddr = httpServletRequest.getRemoteAddr();
                    boolean isLocalHostAddr = List.of("127.0.0.1", "0:0:0:0:0:0:0:1").contains(remoteAddr);
                    return Optional.ofNullable(isLocalHostAddr ? localHostAddress.get() : remoteAddr);
                });
    }

    public String getUserAgent() {
        return httpServletRequest.getHeader(HttpHeaders.USER_AGENT);
    }

    public Client getUserClient() {
        return UA_PARSER.parse(getUserAgent());
    }

    public String getUserFlag() {
        return JwtManager.getCurrentTokenDTO()
                .map(JwtTokenDTO::getUid)
                .map(Objects::toString)
                .or(this::getUserIpAddress)
                .or(() -> Optional.ofNullable(this.getUserAgent()).map(String::getBytes).map(DigestUtils::md5DigestAsHex))
                .orElse(CommonConst.UNKNOWN);
    }

    /**
     * 获取{@link HttpHeaders#AUTHORIZATION}请求头的 Bearer token.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6750">RFC 6750</a>
     */
    public Optional<String> getBearerToken() {
        String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return Optional.ofNullable(bearerToken)
                .filter(bt -> bt.startsWith(BEARER_PREFIX))
                .map(bt -> bt.replaceFirst(BEARER_PREFIX, ""));
    }

    /**
     * 获取请求头信息
     *
     * @return 如果没有返回<code>null</code>
     */
    @Deprecated
    public Optional<String> getRequestHeader(String name) {
        return Optional.ofNullable(httpServletRequest.getHeader(name));
    }

    /**
     * 设置响应体信息
     *
     * @param responseBody 响应体内容
     */
    @SneakyThrows
    @Deprecated
    public void setResponseBody(ResponseVO<?> responseBody) {
        httpServletResponse.setCharacterEncoding("UTF8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);
        httpServletResponse.getWriter().write(jsonResponseBody);
    }

    public void setStatusCode(HttpStatus statusCode) {
        httpServletResponse.setStatus(statusCode.value());
    }
}
