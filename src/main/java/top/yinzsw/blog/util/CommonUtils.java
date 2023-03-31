package top.yinzsw.blog.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;

import java.util.function.Consumer;

/**
 * 通用工具类
 *
 * @author yinzsW
 * @since 23/01/23
 */

public class CommonUtils {

    public static <C1, C2> SessionCallback<Object> getSessionCallback(Consumer<RedisOperations<C1, C2>> consumer) {
        return new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                RedisOperations<C1, C2> newOperations = (RedisOperations<C1, C2>) operations;
                consumer.accept(newOperations);
                return null;
            }
        };
    }
}
