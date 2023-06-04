package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.manager.CommentManager;
import top.yinzsw.blog.manager.SystemWebManager;
import top.yinzsw.blog.mapper.CommentMapper;
import top.yinzsw.blog.mapper.UserMapper;
import top.yinzsw.blog.model.converter.CommentConverter;
import top.yinzsw.blog.model.po.CommentPO;
import top.yinzsw.blog.model.request.CommentBackQueryReq;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.CommentReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.CommentService;

import java.util.*;

/**
 * 评论业务接口实现
 *
 * @author yinzsW
 * @since 23/02/13
 */

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final CommentManager commentManager;
    private final SystemWebManager systemWebManager;
    private final CommentConverter commentConverter;

    @Override
    public List<CommentDigestVO> listHotComment() {
        return commentMapper.listHotComment();
    }

    @Override
    public CommentPageVO pageTopicComments(PageReq pageReq, CommentQueryReq commentQueryReq) {
        List<CommentVO> commentVOList = commentMapper.listTopicComments(pageReq, commentQueryReq);
        Long count = commentManager.lambdaQuery()
                .eq(CommentPO::getTopicId, commentQueryReq.getTopicId())
                .eq(CommentPO::getTopicType, commentQueryReq.getTopicType())
                .eq(CommentPO::getIsReviewed, true)
                .count();
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new CommentPageVO().setComments(Collections.emptyList()).setCount(count);
        }

        List<Long> commentIds = SimpleQuery.list2List(commentVOList, CommentVO::getId);
        List<CommentReplyVO> replyVOList = commentMapper.listReplyComments(commentIds);
        Map<Long, List<CommentReplyVO>> replies = SimpleQuery.listGroupBy(replyVOList, CommentReplyVO::getCommentRootId);

        List<Long> userIds = SimpleQuery.list2List(commentVOList, CommentVO::getUserId);
        userIds.addAll(SimpleQuery.list2List(replyVOList, CommentReplyVO::getUserId));
        userIds.addAll(SimpleQuery.list2List(replyVOList, CommentReplyVO::getReplyUserId));
        Map<Long, UserVO> users = userMapper.getUserMap(userIds);

        return new CommentPageVO()
                .setComments(commentVOList)
                .setReplies(replies)
                .setUsers(users)
                .setCount(count);
    }

    @Override
    public CommentPageVO pageReplyComments(PageReq pageReq, Long commentId) {
        Page<CommentPO> commentPOPage = commentManager.lambdaQuery()
                .eq(CommentPO::getIsReviewed, true)
                .likeRight(CommentPO::getReplyCommentIds, commentId.toString() + "%")
                .orderByDesc(CommentPO::getCreateTime)
                .page(pageReq.getPager());

        List<CommentPO> commentPOList = commentPOPage.getRecords();
        if (CollectionUtils.isEmpty(commentPOList)) {
            List<CommentReplyVO> commentReplyVOList = commentConverter.toCommentReplyVO(commentPOList);
            return new CommentPageVO().setReplies(Map.of(commentId, commentReplyVOList)).setCount(commentPOPage.getTotal());
        }

        List<Long> userIds = SimpleQuery.list2List(commentPOList, CommentPO::getUserId);
        userIds.addAll(SimpleQuery.list2List(commentPOList, CommentPO::getReplyUserId));
        Map<Long, UserVO> users = userMapper.getUserMap(userIds);

        List<CommentReplyVO> commentReplyVOList = commentConverter.toCommentReplyVO(commentPOList);
        return new CommentPageVO()
                .setReplies(Map.of(commentId, commentReplyVOList))
                .setUsers(users)
                .setCount(commentPOPage.getTotal());
    }

    @Override
    public PageVO<CommentBackgroundVO> pageBackgroundComments(PageReq pageReq,
                                                              CommentBackQueryReq queryReq) {
        List<CommentBackgroundVO> commentBackgroundVOList = commentMapper.listBackgroundComments(pageReq, queryReq);
        Long count = commentMapper.countBackgroundComments(queryReq);
        return PageVO.getPageVO(commentBackgroundVOList, count);
    }

    @Override
    public boolean saveComments(CommentReq commentReq) {
        commentReq.setCommentContent(HtmlUtils.htmlEscape(commentReq.getCommentContent()));
        CommentPO commentPO = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid)
                .map(uid -> commentConverter.toCommentPO(commentReq, uid))
                .orElseThrow(() -> new PreAuthenticatedCredentialsNotFoundException("用户凭据未找到"));

        Optional.ofNullable(systemWebManager.getWebSiteConfig(WebsiteConfigVO::getEnableCommentReview))
                .ifPresent(enableCommentReview -> commentPO.setIsReviewed(!enableCommentReview));

        if (Objects.nonNull(commentReq.getReplyId())) {
            CommentPO replayComment = commentManager.lambdaQuery()
                    .select(CommentPO::getId, CommentPO::getTopicId, CommentPO::getUserId, CommentPO::getReplyCommentIds)
                    .eq(CommentPO::getId, commentReq.getReplyId())
                    .one();

            String replyCommentIds = StringUtils.trimLeadingCharacter(String.format("%s,%s", replayComment.getReplyCommentIds(), replayComment.getId()), ',');
            commentPO.setReplyUserId(replayComment.getUserId())
                    .setReplyCommentIds(replyCommentIds)
                    .setTopicId(replayComment.getTopicId());
        }
        return commentManager.save(commentPO);
    }

    @Override
    public boolean updateIsTop(List<Long> commentIds, Boolean isTop) {
        return commentManager.lambdaUpdate()
                .set(CommentPO::getIsTop, isTop)
                .in(CommentPO::getId, commentIds)
                .update();
    }

    @Override
    public boolean updateIsReviewed(List<Long> commentIds, Boolean isReviewed) {
        return commentManager.lambdaUpdate()
                .set(CommentPO::getIsReviewed, isReviewed)
                .in(CommentPO::getId, commentIds)
                .update();
    }

    @Override
    public boolean deleteComments(List<Long> commentIds) {
        return commentManager.removeBatchByIds(commentIds);
    }
}
