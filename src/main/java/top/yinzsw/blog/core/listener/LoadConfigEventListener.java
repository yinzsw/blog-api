package top.yinzsw.blog.core.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.config.BlogConfig;
import top.yinzsw.blog.manager.SystemWebManager;

/**
 * 监听SpringBoot启动
 *
 * @author yinzsW
 * @since 23/01/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoadConfigEventListener implements ApplicationListener<ApplicationStartedEvent> {
    private final SystemWebManager systemWebManager;
    private final BlogConfig blogConfig;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        if (blogConfig.isInitWebsiteConfig()) {
            log.info("网站配置文件加载中...");
            systemWebManager.initWebSiteConfig();
            log.info("网站配置文件加载完毕");
        }
    }
}
