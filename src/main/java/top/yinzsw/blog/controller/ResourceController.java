package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.ResourceQueryReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.ResourceBackgroundVO;
import top.yinzsw.blog.model.vo.ResourceModuleVO;
import top.yinzsw.blog.service.ResourceService;

import javax.validation.Valid;
import java.util.List;

/**
 * 接口资源模块
 *
 * @author yinzsW
 * @since 23/05/05
 */
@Tag(name = "接口资源模块")
@Validated
@RestController
@RequestMapping("resource")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @Operation(summary = "查询资源模块列表")
    @GetMapping("module")
    public List<ResourceModuleVO> listModules() {
        return resourceService.listModules();
    }

    @Operation(summary = "查询资源列表")
    @GetMapping("background")
    public PageVO<ResourceBackgroundVO> pageBackgroundResources(@Valid PageReq pageReq,
                                                                @Valid ResourceQueryReq resourceQueryReq) {
        return resourceService.pageBackgroundResources(pageReq, resourceQueryReq);
    }

    @Operation(summary = "是否允许资源匿名访问")
    @PatchMapping("{resourceId:\\d+}/isAnonymous/{isAnonymous:true|false}")
    public boolean updateResourceIsAnonymous(@Parameter(description = "资源id", required = true)
                                             @PathVariable("resourceId") Long resourceId,
                                             @Parameter(description = "允许匿名", required = true)
                                             @PathVariable("isAnonymous") Boolean isAnonymous) {
        return resourceService.updateResourceIsAnonymous(resourceId, isAnonymous);
    }

    @Operation(summary = "更新资源授权角色")
    @PatchMapping("{resourceId:\\d+}/roleIds/{roleIds:\\d+(?:,\\d+)*}")
    public boolean updateResourceRoles(@Parameter(description = "资源id", required = true)
                                       @PathVariable("resourceId") Long resourceId,
                                       @Parameter(description = "角色id列表", required = true)
                                       @PathVariable("roleIds") List<Long> roleIds) {
        return resourceService.updateResourceRoles(resourceId, roleIds);
    }
}
