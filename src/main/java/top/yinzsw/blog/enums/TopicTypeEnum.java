package top.yinzsw.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主题类型枚举
 *
 * @author yinzsW
 * @since 23/02/04
 */

@Getter
@AllArgsConstructor
public enum TopicTypeEnum {

    WEBSITE(0, "website"),

    /**
     * 文章
     */
    ARTICLE(1, "article"),

    /**
     * 相册
     */
    ALBUM(2, "album"),

    /**
     * 说说
     */
    TALK(3, "talk"),

    /**
     * 留言
     */
    MESSAGE(4, "message"),

    /**
     * 评论
     */
    COMMENT(5, "comment");

    /**
     * 主题类型名值
     */
    @EnumValue
    private final int value;

    /**
     * 主题类型名
     */
    private final String topicName;
}
