package top.yinzsw.blog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yinzsw.blog.core.upload.UploadProvider;
import top.yinzsw.blog.enums.FilePathEnum;
import top.yinzsw.blog.extension.validation.MatchFileType;
import top.yinzsw.blog.model.request.AboutReq;
import top.yinzsw.blog.model.request.LogQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;
import top.yinzsw.blog.service.SystemWebService;

import javax.validation.Valid;

/**
 * 系统模块
 *
 * @author yinzsW
 * @since 23/05/06
 */
@Tag(name = "系统模块")
@Validated
@RestController
@RequestMapping("system")
@RequiredArgsConstructor
public class SystemWebController {
    private final UploadProvider uploadProvider;
    private final SystemWebService systemWebService;

    @Operation(summary = "系统操作日志")
    @GetMapping(value = "logs")
    public PageVO<LogBackgroundVO> pageLogs(@Valid PageReq pageReq,
                                            @Valid LogQueryReq logQueryReq) {
        return systemWebService.pageLogs(pageReq, logQueryReq);
    }

    @Operation(summary = "上报访客信息")
    @PostMapping("report")
    public boolean report() {
        return systemWebService.report();
    }

    @Operation(summary = "获取博客信息(首页)")
    @GetMapping("blog/home")
    public BlogHomeInfoVO getBlogHomeInfo() {
        return systemWebService.getBlogHomeInfo();
    }

    @Operation(summary = "获取博客信息(后台)")
    @GetMapping("blog/back")
    public BlogBackInfoVO getBlogBackInfo() {
        return systemWebService.getBlogBackInfo();
    }

    @Operation(summary = "获取网站配置")
    @GetMapping("website/config")
    public WebsiteConfigVO getWebsiteConfig() {
        return systemWebService.getWebsiteConfig();
    }

    @Operation(summary = "上传配置相关图片")
    @PostMapping(value = "configImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadArticleImage(@Parameter(description = "文章图片", required = true)
                                     @MatchFileType(mimeType = "image/*", message = "仅支持上传图片类型{mimeType}的文件")
                                     @RequestPart("file") MultipartFile file) {
        return uploadProvider.uploadFile(FilePathEnum.CONFIG.getPath(), file);
    }

    @Operation(summary = "更新网站配置")
    @PutMapping("website/config")
    public boolean updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        return systemWebService.updateWebsiteConfig(websiteConfigVO);
    }

    @Operation(summary = "查看关于我信息")
    @GetMapping("about")
    public String getAbout() {
        return systemWebService.getAbout();
    }

    @Operation(summary = "修改关于我信息")
    @PutMapping("about")
    public boolean updateAbout(@Valid @RequestBody AboutReq aboutReq) {
        return systemWebService.updateAbout(aboutReq);
    }
}
