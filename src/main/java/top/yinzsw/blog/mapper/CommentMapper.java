package top.yinzsw.blog.mapper;

import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.CommentPO;
import top.yinzsw.blog.model.request.CommentBackQueryReq;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CommentBackgroundVO;
import top.yinzsw.blog.model.vo.CommentDigestVO;
import top.yinzsw.blog.model.vo.CommentReplyVO;
import top.yinzsw.blog.model.vo.CommentVO;

import java.util.List;

public interface CommentMapper extends CommonMapper<CommentPO> {

    List<CommentDigestVO> listHotComment();

    List<CommentVO> listTopicComments(@Param("page") PageReq pageReq, @Param("query") CommentQueryReq commentQueryReq);

    List<CommentReplyVO> listReplyComments(@Param("commentIds") List<Long> commentIds);

    List<CommentBackgroundVO> listBackgroundComments(@Param("page") PageReq pageReq, @Param("query") CommentBackQueryReq queryReq);

    Long countBackgroundComments(@Param("query") CommentBackQueryReq queryReq);
}