package top.yinzsw.blog.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;
import top.yinzsw.blog.enums.TopicTypeEnum;

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
@ParameterObject
public class CommentBackQueryReq {

    @Parameter(description = "用户id")
    private Long userId;

    @Parameter(description = "评论主题类型")
    private TopicTypeEnum topicType;

    @Parameter(description = "是否置顶")
    private Boolean isTop;

    @Parameter(description = "是否审核")
    private Boolean isReviewed;

    @Parameter(description = "是否修改过")
    private Boolean isModified;

    @Parameter(description = "关键词")
    private String keywords;
}
