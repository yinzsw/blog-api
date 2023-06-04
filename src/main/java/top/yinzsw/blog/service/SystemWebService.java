package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.AboutReq;
import top.yinzsw.blog.model.request.LogQueryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.*;

/**
 * 网站系统业务层
 *
 * @author yinzsW
 * @since 23/05/06
 */

public interface SystemWebService {
    PageVO<LogBackgroundVO> pageLogs(PageReq pageReq, LogQueryReq logQueryReq);

    boolean report();

    BlogHomeInfoVO getBlogHomeInfo();

    BlogBackInfoVO getBlogBackInfo();

    WebsiteConfigVO getWebsiteConfig();

    boolean updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    String getAbout();

    boolean updateAbout(AboutReq aboutReq);
}
