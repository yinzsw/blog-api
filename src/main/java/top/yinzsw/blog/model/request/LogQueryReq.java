package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class LogQueryReq {

    @Parameter(description = "用户id")
    private Long userId;

    /**
     * 分组名
     */
    @Parameter(description = "分组名")
    private String module;

    /**
     * 请求方法
     */
    @Parameter(description = "请求方法")
    private RequestMethod method;

    /**
     * 是否允许匿名访问
     */
    @Parameter(description = "是否允许匿名访问")
    private Boolean isAnonymous;

    /**
     * 是否允许匿名访问
     */
    @Parameter(description = "是否发生异常")
    private Boolean isException;

    /**
     * 起始日期
     */
    @Parameter(description = "起始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @Parameter(description = "结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
