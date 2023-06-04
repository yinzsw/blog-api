package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResourcePO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源模块
     */
    private String module;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 资源名
     */
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 是否匿名访问 0否 1是
     */
    private Boolean isAnonymous;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

