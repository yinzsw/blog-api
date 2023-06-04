package top.yinzsw.blog.core.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 切入点提供器
 *
 * @author yinzsW
 * @since 23/01/10
 */

public interface PointCutProvider {
    /**
     * 判断是否是自己定义的 REST-ful控制器
     */
    @Pointcut("execution(public * top.yinzsw.blog.controller..*(..))) && @target(org.springframework.web.bind.annotation.RestController)")
    default void isSelfRestController() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    default void isGetMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    default void isPostMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    default void isPatchMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    default void isPutMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    default void isDeleteMapping() {
    }

    /**
     * 判断接口是不是自己定义的分页接口
     */
    @Pointcut("isSelfRestController() && isGetMapping() && execution(* *(..,top.yinzsw.blog.model.request.PageReq,..)))")
    default void isPagingApi() {
    }

    @Pointcut("isSelfRestController() && isPatchMapping() && execution(public * updateResource*(..)) ")
    default void isUpdateResourceApi() {
    }

    @Pointcut("isSelfRestController() &&(isGetMapping() || isPostMapping() || isPatchMapping() || isPutMapping() || isDeleteMapping())")
    default void isOperationApi() {
    }
}
