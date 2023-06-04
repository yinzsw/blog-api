package top.yinzsw.blog.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.yinzsw.blog.util.CommonUtils;

import java.util.List;

@Slf4j
@SpringBootTest
class SystemWebManagerImplTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void getUniqueVisitorCount() {

        redisTemplate.opsForValue().set("SDA", 1);

    }

    @Test
    void pip() {
        List<String> strings = List.of("x:1", "x:2", "x:3");
        List<Object> objects = stringRedisTemplate.executePipelined(CommonUtils.<String, String>getSessionCallback(operations -> {
            operations.multi();
            strings.forEach(key -> operations.opsForSet().size(key));
            operations.delete(strings);
            operations.exec();
            return null;
        }));
        log.info("{}", objects);
    }
}