package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 日志后台模型
 *
 * @author yinzsW
 * @since 23/05/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "日志后台模型")
public class LogBackgroundVO {

    /**
     * 日志id
     */
    @Schema(title = "日志id")
    private Long id;

    /**
     * 操作资源id
     */
    @Schema(title = "操作资源id")
    private ResourceVO resource;

    /**
     * 操作用户id
     */
    @Schema(title = "操作用户id")
    private UserVO user;

    /**
     * 用户ip
     */
    @Schema(title = "用户ip")
    private String ip;

    /**
     * 用户地址
     */
    @Schema(title = "用户地址")
    private String address;

    /**
     * 资源路径
     */
    @Schema(title = "资源路径")
    private String uri;

    /**
     * 请求耗时
     */
    @Schema(title = "请求耗时")
    private Long time;

    /**
     * 请求参数
     */
    @Schema(title = "请求参数")
    private Object params;

    /**
     * 响应数据
     */
    @Schema(title = "响应数据")
    private Object data;

    /**
     * 异常信息
     */
    @Schema(title = "异常信息")
    private String exception;

    /**
     * 有异常
     */
    @Schema(title = "是否发生异常")
    private Boolean isException;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
