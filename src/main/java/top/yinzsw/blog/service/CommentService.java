package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.CommentBackQueryReq;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.CommentReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CommentBackgroundVO;
import top.yinzsw.blog.model.vo.CommentDigestVO;
import top.yinzsw.blog.model.vo.CommentPageVO;
import top.yinzsw.blog.model.vo.PageVO;

import java.util.List;

/**
 * 评论业务接口
 *
 * @author yinzsW
 * @since 23/02/09
 */

public interface CommentService {

    List<CommentDigestVO> listHotComment();

    CommentPageVO pageTopicComments(PageReq pageReq, CommentQueryReq commentQueryReq);

    CommentPageVO pageReplyComments(PageReq pageReq, Long commentId);

    PageVO<CommentBackgroundVO> pageBackgroundComments(PageReq pageReq, CommentBackQueryReq commentBackQueryReq);

    boolean saveComments(CommentReq commentReq);

    boolean updateIsTop(List<Long> commentIds, Boolean isTop);

    boolean updateIsReviewed(List<Long> commentIds, Boolean isReviewed);

    boolean deleteComments(List<Long> commentIds);
}
