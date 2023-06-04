package top.yinzsw.blog.core.aop.aspect;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.client.IpClient;
import top.yinzsw.blog.constant.CommonConst;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtResourceDTO;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.core.system.HttpContext;
import top.yinzsw.blog.manager.LogOperationManager;
import top.yinzsw.blog.model.po.LogOperationPO;
import top.yinzsw.blog.util.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/23
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {
    private final IpClient ipClient;
    private final HttpContext httpContext;
    private final LogOperationManager logOperationManager;

    @Around(value = "top.yinzsw.blog.core.aop.PointCutProvider.isOperationApi()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        var ref = new Object() {
            Object data = null;
            Throwable exception = null;
            final long start = SystemClock.now();
        };

        try {
            ref.data = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            ref.exception = e;
            throw e;
        } finally {
            long time = SystemClock.now() - ref.start;
            Long resourceId = JwtManager.getCurrentResourceDTO().map(JwtResourceDTO::getId)
                    .orElseThrow(() -> new ResourceAccessException("未发现资源信息"));
            Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);

            HttpServletRequest request = httpContext.getRequest();
            String uri = request.getRequestURI();
            String ip = httpContext.getUserIpAddress().orElse(CommonConst.UNKNOWN);

            Object[] args = proceedingJoinPoint.getArgs();
            Serializable params = args.length == 0 ? null : (args[0] instanceof MultipartFile ? "file" : args);
            Object data = RequestMethod.GET.name().equalsIgnoreCase(request.getMethod()) ? null : ref.data;

            CompletableFuture.runAsync(() -> {
                String address = ipClient.getIpInfo(ip).getFirstLocation().orElse(CommonConst.UNKNOWN);
                LogOperationPO operationLog = new LogOperationPO()
                        .setResourceId(resourceId)
                        .setUserId(userId)
                        .setUri(uri)
                        .setIp(ip)
                        .setAddress(address)
                        .setTime(time)
                        .setParams(params)
                        .setData(data)
                        .setException(CommonUtils.getStackTrace(ref.exception));
                logOperationManager.save(operationLog);
            });
        }

        return ref.data;
    }

}
