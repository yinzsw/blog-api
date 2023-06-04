package top.yinzsw.blog.manager;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import top.yinzsw.blog.model.vo.WebsiteConfigVO;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/08
 */

public interface SystemWebManager {

    void initWebSiteConfig();

    WebsiteConfigVO getWebSiteConfig();

    boolean updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    <T, R> R getWebSiteConfig(SFunction<T, R> sFunction);

    void report();

    Long getUniqueVisitorCount();
}
