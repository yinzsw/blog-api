package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论回复模型")
public class CommentReplyVO {
    /**
     * 评论id
     */
    @Schema(title = "评论id")
    private Long id;

    /**
     * 评论根id
     */
    @Schema(title = "评论id")
    private Long commentRootId;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    private Long userId;

    @Schema(title = "回复用户id")
    private Long replyUserId;

    /**
     * 评论内容
     */
    @Schema(title = "评论内容")
    private String commentContent;

    /**
     * 点赞量
     */
    @Schema(title = "点赞量")
    private Long likedCount;

    /**
     * 是否修改过
     */
    @Schema(title = "是否修改过")
    private Boolean isModified;

    /**
     * 创建时间
     */
    @Schema(title = "发布时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
}
