package top.yinzsw.blog.model.request;


import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springdoc.api.annotations.ParameterObject;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 文章查询模型
 *
 * @author yinzsW
 * @since 23/01/13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ParameterObject
public class ArticleQueryReq {

    /**
     * 搜索关键字
     */
    @Parameter(description = "搜索关键字(标题)")
    private String title;

    /**
     * 文章分类id
     */
    @Min(value = 1, message = "不合法的分类id: ${validatedValue}")
    @Parameter(description = "文章分类id")
    private Long categoryId;

    /**
     * 标签id
     */
    @Min(value = 1, message = "不合法的标签id: ${validatedValue}")
    @Parameter(description = "标签id")
    private Long tagId;

    /**
     * 是否已经删除
     */
    @NotNull(message = "是否删除是必选项")
    @Parameter(description = "是否已经删除", required = true)
    private Boolean isDeleted;

    /**
     * 文章状态
     */
    @Parameter(description = "文章状态")
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型
     */
    @Parameter(description = "文章类型")
    private ArticleTypeEnum articleType;
}
