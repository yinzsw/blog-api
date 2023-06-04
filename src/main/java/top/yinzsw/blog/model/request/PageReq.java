package top.yinzsw.blog.model.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 分页模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class PageReq {

    /**
     * 页码
     */
    @Schema(defaultValue = "1")
    @Parameter(description = "页码")
    private Long page;

    /**
     * 条数
     */
    @Schema(defaultValue = "10", maximum = "30")
    @Parameter(description = "条数")
    private Long size;

    @JsonIgnore
    public <T> Page<T> getPager() {
        return new Page<>(page, size);
    }

    @JsonIgnore
    public Long getOffset() {
        return (page - 1) * size;
    }
}
