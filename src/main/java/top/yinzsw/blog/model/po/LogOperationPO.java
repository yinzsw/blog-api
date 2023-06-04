package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "log_operation")
public class LogOperationPO {
    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 操作资源id
     */
    private Long resourceId;

    /**
     * 操作用户id
     */
    private Long userId;

    /**
     * 用户ip
     */
    private String ip;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 请求耗时
     */
    private Long time;

    /**
     * 请求参数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object params;

    /**
     * 响应数据
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Object data;

    /**
     * 异常信息
     */
    private String exception = "";

    /**
     * 是否发生异常(虚拟)
     */
    private Boolean isException;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

