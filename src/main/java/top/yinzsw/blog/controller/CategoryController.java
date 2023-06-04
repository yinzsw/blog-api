package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryBackgroundVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

/**
 * 文章分类模块
 *
 * @author yinzsW
 * @since 23/01/27
 */
@Tag(name = "文章分类模块")
@Validated
@RestController
@RequestMapping("category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "查询分类列表")
    @GetMapping
    public PageVO<CategoryVO> pageCategories(@Valid PageReq pageReq) {
        return categoryService.pageCategories(pageReq);
    }

    @Operation(summary = "查询热门分类列表")
    @GetMapping("hot")
    public List<CategoryVO> listHotCategories() {
        return categoryService.listHotCategories();
    }

    @Operation(summary = "查询分类列表(后台)")
    @GetMapping("background")
    public PageVO<CategoryBackgroundVO> pageBackgroundCategories(@Valid PageReq pageReq,
                                                                 @Parameter(description = "分类名关键词")
                                                                 @RequestParam(value = "keywords", required = false) String keywords) {
        return categoryService.pageBackgroundCategories(pageReq, keywords);
    }

    @Operation(summary = "添加或修改分类")
    @PutMapping
    public CategoryVO saveOrUpdateCategory(@Valid @RequestBody CategoryReq categoryReq) {
        return categoryService.saveOrUpdateCategory(categoryReq);
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("{categoryIds:\\d+(?:,\\d+)*}")
    public boolean deleteCategories(@Parameter(description = "分类id列表", required = true)
                                    @PathVariable("categoryIds") List<Long> categoryIds) {
        return categoryService.deleteCategories(categoryIds);
    }
}
