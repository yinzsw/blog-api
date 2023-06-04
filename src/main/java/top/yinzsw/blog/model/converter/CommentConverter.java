package top.yinzsw.blog.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import top.yinzsw.blog.model.po.CommentPO;
import top.yinzsw.blog.model.request.CommentReq;
import top.yinzsw.blog.model.vo.CommentReplyVO;

import java.util.List;

/**
 * 评论数据模型转换器
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentConverter {


    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "replyUserId", ignore = true)
    @Mapping(target = "replyCommentIds", ignore = true)
    @Mapping(target = "likedCount", ignore = true)
    @Mapping(target = "isTop", ignore = true)
    @Mapping(target = "isReviewed", ignore = true)
    @Mapping(target = "isModified", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    CommentPO toCommentPO(CommentReq commentReq, Long userId);

    List<CommentReplyVO> toCommentReplyVO(List<CommentPO> commentPOList);
}
