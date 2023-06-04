package top.yinzsw.blog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 博客信息模型(后台)
 *
 * @author yinzsW
 * @since 23/05/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(description = "博客信息模型(后台)")
public class BlogBackInfoVO {
    private Long viewsCount;

    private Long articleCount;

    private Long usersCount;

    private Long messageCount;

    private List<Long> weekViewsCount;

    private List<Long> weekUsersCount;

    private List<StatisticsDayCountVO> yearArticleCount;

    private List<StatisticsNameCountVO> tags;

    private List<StatisticsNameCountVO> categories;

    private List<ArticleRankVO> articles;
}
