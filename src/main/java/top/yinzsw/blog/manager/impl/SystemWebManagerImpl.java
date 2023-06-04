package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.constant.CommonConst;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.core.system.HttpContext;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.extension.mybatisplus.SqlUtils;
import top.yinzsw.blog.manager.SystemWebManager;
import top.yinzsw.blog.model.vo.WebsiteConfigVO;

import java.util.Map;
import java.util.Optional;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/08
 */
@Service
@RequiredArgsConstructor
public class SystemWebManagerImpl implements SystemWebManager {
    private final IpClient ipClient;
    private final HttpContext httpContext;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Async
    @Override
    public void initWebSiteConfig() {
        updateWebsiteConfig(new WebsiteConfigVO());
    }

    @Override
    public WebsiteConfigVO getWebSiteConfig() {
        Map<String, Object> websiteConfigMap = redisTemplate.<String, Object>opsForHash().entries(RedisConstEnum.WEBSITE_CONFIG.getKey());
        return objectMapper.convertValue(websiteConfigMap, WebsiteConfigVO.class);
    }

    @Override
    public boolean updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        Map<String, Object> config = objectMapper.convertValue(websiteConfigVO, new TypeReference<>() {
        });
        redisTemplate.<String, Object>opsForHash().putAll(RedisConstEnum.WEBSITE_CONFIG.getKey(), config);
        return true;
    }

    @Override
    public <T, R> R getWebSiteConfig(SFunction<T, R> sFunction) {
        String propertyName = SqlUtils.getPropertyName(sFunction);
        return redisTemplate.<String, R>opsForHash().get(RedisConstEnum.WEBSITE_CONFIG.getKey(), propertyName);
    }

    @Override
    public void report() {
        Boolean isLogin = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).isPresent();
        String ipAddress = httpContext.getUserIpAddress().orElse(CommonConst.UNKNOWN);
        String userFlag = httpContext.getUserFlag();

        stringRedisTemplate.opsForValue().increment(RedisConstEnum.VIEW_COUNT_PV.getKey(), 1);
        if (SqlHelper.retBool(stringRedisTemplate.opsForSet().add(RedisConstEnum.VIEW_DAY_USERS.getKey(), userFlag))) {
            String province = ipClient.getIpInfo(ipAddress).getProvince();

            stringRedisTemplate.opsForHash().increment(RedisConstEnum.VIEW_AREA.getKey(isLogin), province, 1);
            stringRedisTemplate.opsForValue().increment(RedisConstEnum.VIEW_COUNT_UV.getKey(), 1);
        }
    }

    @Override
    public Long getUniqueVisitorCount() {
        Object count = stringRedisTemplate.opsForValue().get(RedisConstEnum.VIEW_COUNT_UV.getKey());
        return Optional.ofNullable(count).map(Object::toString).map(Long::parseLong).orElse(0L);
    }
}
