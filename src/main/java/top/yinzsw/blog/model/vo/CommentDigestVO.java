package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 评论摘要信息模型
 *
 * @author yinzsW
 * @since 23/05/08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论摘要信息模型")
public class CommentDigestVO {

    @Schema(title = "评论id")
    private Long id;

    @Schema(title = "评论内容")
    private String commentContent;

    @Schema(title = "用户")
    private UserVO user;

    @Schema(title = "发布时间")
    private LocalDateTime createTime;
}
