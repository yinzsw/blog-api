package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.FriendLinkReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.FriendLinkBackgroundVO;
import top.yinzsw.blog.model.vo.FriendLinkVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.FriendLinkService;

import javax.validation.Valid;
import java.util.List;

/**
 * 友链模块
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Tag(name = "友链模块")
@Validated
@RestController
@RequestMapping("link")
@RequiredArgsConstructor
public class FriendLinkController {
    private final FriendLinkService friendLinkService;

    @Operation(summary = "查看友链列表")
    @GetMapping
    public PageVO<FriendLinkVO> pageFriendLinks(@Valid PageReq pageReq) {
        return friendLinkService.pageFriendLinks(pageReq);
    }


    @Operation(summary = "查看友链后台列表")
    @GetMapping("background")
    public PageVO<FriendLinkBackgroundVO> pageBackgroundFriendLinks(@Valid PageReq pageReq,
                                                                    @Parameter(description = "友链关键词", required = true)
                                                                    @RequestParam("keywords") String keywords) {
        return friendLinkService.pageBackgroundFriendLinks(pageReq, keywords);
    }

    @Operation(summary = "保存或修改友链")
    @PutMapping
    public boolean saveOrUpdateFriendLink(@Valid @RequestBody FriendLinkReq friendLinkReq) {
        return friendLinkService.saveOrUpdateFriendLink(friendLinkReq);
    }

    @Operation(summary = "删除友链")
    @DeleteMapping("{friendLinkIds:\\d+(?:,\\d+)*}")
    public boolean deleteFriendLinks(@Parameter(description = "友链id列表", required = true)
                                     @PathVariable("friendLinkIds") List<Long> friendLinkIds) {
        return friendLinkService.deleteFriendLinks(friendLinkIds);
    }
}
