package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 网站配置模型
 *
 * @author yinzsW
 * @since 23/05/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "网站配置模型")
public class WebsiteConfigVO {

    @Schema(title = "QQ")
    private String qq = "";

    @Schema(title = "Github")
    private String github = "";

    @Schema(title = "Gitee")
    private String gitee = "";


    @Schema(title = "网站名称")
    private String websiteName = "blog";

    @Schema(title = "网站logo")
    private String websiteLogo = "";

    @Schema(title = "网站公告")
    private String websiteNotice = "";

    @Schema(title = "网站创建时间")
    private LocalDateTime websiteCreateTime = LocalDateTime.now();

    @Schema(title = "网站备案号")
    private String websiteRecordNo = "蜀ICP备2021019758号-1";


    @Schema(title = "网站作者昵称")
    private String authorName = "yinzsw";

    @Schema(title = "网站作者头像")
    private String authorAvatar = "";

    @Schema(title = "网站作者介绍")
    private String authorIntro = "coder";


    @Schema(title = "是否开启qq登录")
    private Boolean enableQQLogin = false;

    @Schema(title = "是否开启邮箱通知")
    private Boolean enableEmailNotice = false;

    @Schema(title = "是否评论审核")
    private Boolean enableCommentReview = false;

    @Schema(title = "是否开启打赏")
    private Boolean enableReward = false;

    @Schema(title = "微信二维码")
    private String weiXinQRCode = "";

    @Schema(title = "支付宝二维码")
    private String alipayQRCode = "";


    @Schema(title = "用户头像")
    private String defaultUserAvatar = "https://files.yinzsw.top/avatar/Github.jpg";

    @Schema(title = "游客头像")
    private String defaultAnonymousUserAvatar = "https://files.yinzsw.top/avatar/Github.jpg";

    @Schema(title = "文章封面")
    private String defaultArticleCover = "https://files.yinzsw.top/cover/104531.42aab4a00eb6972767e0396a30bd6415.jpg";
}
