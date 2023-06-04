package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户角色
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "用户角色")
public class RoleBackgroundVO {
    /**
     * 角色id
     */
    @Schema(title = "角色id")
    private Long id;

    /**
     * 角色名
     */
    @Schema(title = "角色名")
    private String name;

    /**
     * 是否禁用
     */
    @Schema(title = "是否禁用")
    private Boolean isDisabled;

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
