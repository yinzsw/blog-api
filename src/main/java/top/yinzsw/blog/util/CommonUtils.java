package top.yinzsw.blog.util;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 通用工具类
 *
 * @author yinzsW
 * @since 23/01/23
 */

public class CommonUtils {

    public static <C1, C2> SessionCallback<List<?>> getSessionCallback(Function<RedisOperations<C1, C2>, List<?>> fn) {
        return new SessionCallback<>() {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> List<?> execute(@NonNull RedisOperations<K, V> operations) throws DataAccessException {
                RedisOperations<C1, C2> newOperations = (RedisOperations<C1, C2>) operations;
                return fn.apply(newOperations);
            }
        };
    }

    public static String getStackTrace(Throwable t) {
        if (Objects.isNull(t)) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
