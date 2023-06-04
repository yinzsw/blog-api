package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.TopicTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论")
public class CommentReq {

    @Schema(title = "回复id")
    private Long replyId;

    @Schema(title = "主题id")
    private Long topicId;

    @NotNull(message = "主题类型不能为空")
    @Schema(title = "主题类型")
    private TopicTypeEnum topicType;

    @NotBlank(message = "评论内容不能为空")
    @Schema(title = "评论内容")
    private String commentContent;

}
