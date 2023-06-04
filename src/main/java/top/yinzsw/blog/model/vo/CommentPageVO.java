package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 评论分页模型
 *
 * @author yinzsW
 * @since 23/05/05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论分页模型")
public class CommentPageVO {

    /**
     * 评论记录
     */
    @Schema(title = "评论列表")
    private List<CommentVO> comments;

    /**
     * 回复评论
     */
    @Schema(title = "回复评论")
    private Map<Long, List<CommentReplyVO>> replies;

    /**
     * 用户信息
     */
    @Schema(title = "用户信息")
    private Map<Long, UserVO> users;

    /**
     * 评论总数
     */
    @Schema(title = "评论总数")
    private Long count;
}
    