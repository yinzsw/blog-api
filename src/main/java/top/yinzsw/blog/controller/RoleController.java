package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.yinzsw.blog.model.request.RoleReq;
import top.yinzsw.blog.model.vo.RoleBackgroundVO;
import top.yinzsw.blog.model.vo.RoleVO;
import top.yinzsw.blog.service.RoleService;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色控制器
 *
 * @author yinzsW
 * @since 23/01/02
 */
@Tag(name = "用户角色模块")
@Validated
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "搜索用户角色")
    @GetMapping("keywords/{keywords}")
    public List<RoleVO> listSearchRoleVO(@Parameter(description = "用户名关键词", required = true)
                                         @PathVariable("keywords") String keywords) {
        return roleService.listSearchRoleVO(keywords);
    }

    @Operation(summary = "查询用户角详情")
    @GetMapping("background/keywords/{keywords}")
    public List<RoleBackgroundVO> listBackgroundRoles(@Parameter(description = "用户名关键词", required = true)
                                                      @PathVariable("keywords") String keywords) {
        return roleService.listBackgroundRoles(keywords);
    }

    @Operation(summary = "更新角色禁用状态")
    @PatchMapping("{roleId:\\d+}/{isDisabled:true|false}")
    public boolean updateRoleIsDisabled(@Parameter(description = "角色id", required = true)
                                        @PathVariable("roleId") Long roleId,
                                        @Parameter(description = "禁用状态", required = true)
                                        @PathVariable("isDisabled") Boolean isDisabled) {
        return roleService.updateRoleIsDisabled(roleId, isDisabled);
    }

    @Operation(summary = "保存或更新角色")
    @PutMapping
    public boolean saveOrUpdateRole(@Valid @RequestBody RoleReq roleReq) {
        return roleService.saveOrUpdateRole(roleReq);
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("{roleIds:\\d+(?:,\\d+)*}")
    public boolean deleteRoles(@Parameter(description = "角色id列表", required = true)
                               @PathVariable("roleIds") List<Long> roleIds) {
        return roleService.deleteRoles(roleIds);
    }
}
