package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.enums.ArticleStatusEnum;
import top.yinzsw.blog.enums.ArticleTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章表
 *
 * @TableName article
 */
@TableName(value = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticlePO implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 作者id
     */
    private Long userId;

    /**
     * 文章分类id
     */
    private Long categoryId;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章内容
     */
    private String articleContent;

    /**
     * 状态值 1公开 2私密 3.草稿
     */
    private ArticleStatusEnum articleStatus;

    /**
     * 文章类型 1原创 2转载 3.翻译
     */
    private ArticleTypeEnum articleType;

    /**
     * 原文链接
     */
    private String originalUrl;

    /**
     * 点赞量
     */
    private Long likesCount;

    /**
     * 浏览量
     */
    private Long viewsCount;

    /**
     * 是否置顶 0否 1是
     */
    private Boolean isTop;

    /**
     * 是否删除  0否 1是
     */
    private Boolean isDeleted;

    /**
     * 发表时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}