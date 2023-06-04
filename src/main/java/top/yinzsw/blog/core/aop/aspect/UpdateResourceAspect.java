package top.yinzsw.blog.core.aop.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.yinzsw.blog.core.security.jwt.JwtManager;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/23
 */
@Aspect
@Component
@RequiredArgsConstructor
public class UpdateResourceAspect {
    private final JwtManager jwtManager;

    @AfterReturning(value = "top.yinzsw.blog.core.aop.PointCutProvider.isUpdateResourceApi()")
    public void afterReturning() {
        jwtManager.loadResource();
    }
}
