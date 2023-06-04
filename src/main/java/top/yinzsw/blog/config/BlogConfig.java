package top.yinzsw.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 博客个性化配置
 *
 * @author yinzsW
 * @since 23/05/13
 */
@Data
@Configuration
@ConfigurationProperties("blog")
public class BlogConfig {
    private boolean initWebsiteConfig = true;

    private boolean initResources = true;

    private boolean enableMessageQueue = false;

    private boolean enableElasticSearch = false;
}
