package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.TagReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundVO;
import top.yinzsw.blog.model.vo.TagVO;
import top.yinzsw.blog.service.TagService;

import javax.validation.Valid;
import java.util.List;

/**
 * 文章标签模块
 *
 * @author yinzsW
 * @since 23/01/12
 */
@Tag(name = "文章标签模块")
@Validated
@RestController
@RequestMapping("tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @Operation(summary = "查询标签列表")
    @GetMapping
    public PageVO<TagVO> pageTags(@Valid PageReq pageReq) {
        return tagService.pageTags(pageReq);
    }

    @Operation(summary = "查询热门标签列表")
    @GetMapping("hot")
    public List<TagVO> listHotTags() {
        return tagService.listHotTags();
    }

    @Operation(summary = "查询文章标签(后台)")
    @GetMapping("background")
    public PageVO<TagBackgroundVO> pageBackgroundTags(@Valid PageReq pageReq,
                                                      @Parameter(description = "标签名关键词", required = true)
                                                      @RequestParam(value = "keywords", required = false) String keywords) {
        return tagService.pageBackgroundTags(pageReq, keywords);
    }

    @Operation(summary = "添加或修改标签")
    @PutMapping
    public boolean saveOrUpdateTag(@Valid @RequestBody TagReq tagReq) {
        return tagService.saveOrUpdateTag(tagReq);
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("{tagIds:\\d+(?:,\\d+)*}")
    public boolean deleteTags(@Parameter(description = "标签id列表", required = true)
                              @PathVariable("tagIds") List<Long> tagIds) {
        return tagService.deleteTags(tagIds);
    }
}
