package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.CommentBackQueryReq;
import top.yinzsw.blog.model.request.CommentQueryReq;
import top.yinzsw.blog.model.request.CommentReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CommentBackgroundVO;
import top.yinzsw.blog.model.vo.CommentDigestVO;
import top.yinzsw.blog.model.vo.CommentPageVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CommentService;

import javax.validation.Valid;
import java.util.List;

/**
 * 评论模块
 *
 * @author yinzsW
 * @since 23/02/09
 */
@Tag(name = "评论模块")
@Validated
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "获取热门评论列表")
    @GetMapping("hot")
    public List<CommentDigestVO> listHotComment() {
        return commentService.listHotComment();
    }

    @Operation(summary = "获取评论列表(摘要)")
    @GetMapping
    public CommentPageVO pageTopicComments(@Valid PageReq pageReq,
                                           @Valid CommentQueryReq commentQueryReq) {
        return commentService.pageTopicComments(pageReq, commentQueryReq);
    }

    @Operation(summary = "获取评论回复列表")
    @GetMapping("{commentId:\\d+}")
    public CommentPageVO pageReplyComments(@Valid PageReq pageReq,
                                           @Parameter(description = "评论id", required = true)
                                           @PathVariable("commentId") Long commentId) {
        return commentService.pageReplyComments(pageReq, commentId);
    }

    @Operation(summary = "获取后台评论列表")
    @GetMapping("background")
    public PageVO<CommentBackgroundVO> pageBackgroundComment(@Valid PageReq pageReq,
                                                             @Valid CommentBackQueryReq commentBackQueryReq) {
        return commentService.pageBackgroundComments(pageReq, commentBackQueryReq);
    }

    @Operation(summary = "保存评论")
    @PostMapping
    public boolean saveComments(@Valid @RequestBody CommentReq commentReq) {
        return commentService.saveComments(commentReq);
    }

    @Operation(summary = "置顶评论")
    @PatchMapping("{commentIds:\\d+(?:,\\d+)*}/isTop/{isTop:true|false}")
    public boolean updateIsTop(@Parameter(description = "评论id列表", required = true)
                               @PathVariable("commentIds") List<Long> commentIds,
                               @Parameter(description = "是否置顶", required = true)
                               @PathVariable("isTop") Boolean isTop) {
        return commentService.updateIsTop(commentIds, isTop);
    }

    @Operation(summary = "审核评论")
    @PatchMapping("{commentIds:\\d+(?:,\\d+)*}/isReviewed/{isReviewed:true|false}")
    public boolean updateIsReviewed(@Parameter(description = "评论id列表", required = true)
                                    @PathVariable("commentIds") List<Long> commentIds,
                                    @Parameter(description = "审核通过", required = true)
                                    @PathVariable("isReviewed") Boolean isReviewed) {
        return commentService.updateIsReviewed(commentIds, isReviewed);
    }


    @Operation(summary = "删除评论")
    @DeleteMapping("{commentIds:\\d+(?:,\\d+)*}")
    public boolean deleteComments(@Parameter(description = "评论id列表", required = true)
                                  @PathVariable("commentIds") List<Long> commentIds) {
        return commentService.deleteComments(commentIds);
    }
}
