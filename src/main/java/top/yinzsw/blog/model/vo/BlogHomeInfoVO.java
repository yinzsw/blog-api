package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 博客信息模型(首页)
 *
 * @author yinzsW
 * @since 23/05/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "博客信息模型(首页)")
public class BlogHomeInfoVO {
    private Long articleCount;

    private Long talkCount;

    private Long categoryCount;

    private Long tagCount;

    private Long viewCount;

    private WebsiteConfigVO config;
}
