package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.TopicTypeEnum;

import java.time.LocalDateTime;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "评论后台模型")
public class CommentBackgroundVO {

    /**
     * 评论id
     */
    @Schema(title = "评论id")
    private Long id;

    /**
     * 评论用户id
     */
    @Schema(title = "评论用户id")
    private UserVO user;

    /**
     * 评论类型 1.文章 2.相册 3.说说
     */
    @Schema(title = "评论类型")
    private TopicTypeEnum topicType;

    /**
     * 评论内容
     */
    @Schema(title = "评论内容")
    private String commentContent;

    /**
     * 评论点赞量
     */
    @Schema(title = "评论点赞量")
    private Long likedCount;

    /**
     * 回复路径枚举
     */
    @Schema(title = "评论等级")
    private Integer level;

    /**
     * 是否置顶
     */
    @Schema(title = "是否置顶")
    private Boolean isTop;

    /**
     * 是否审核
     */
    @Schema(title = "是否审核")
    private Boolean isReviewed;

    /**
     * 是否修改过
     */
    @Schema(title = "是否修改过")
    private Boolean isModified;

    /**
     * 是否修改过
     */
    @Schema(title = "是否删除")
    private Boolean isDeleted;

    /**
     * 评论时间
     */
    @Schema(title = "评论时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
}
