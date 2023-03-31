package top.yinzsw.blog.exception;


import lombok.Getter;
import top.yinzsw.blog.enums.ResponseCodeEnum;


/**
 * 业务异常
 *
 * @author yinzsW
 * @since 22/12/15
 **/
@Getter
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误信息
     */
    private final String message;

    public BizException() {
        this.code = ResponseCodeEnum.FAIL.getCode();
        this.message = ResponseCodeEnum.FAIL.getDesc();
    }

    public BizException(String message) {
        this.code = ResponseCodeEnum.FAIL.getCode();
        this.message = message;
    }
}
