package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件保存路径枚举
 *
 * @author yinzsW
 * @since 22/12/29
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 头像路径
     */
    AVATAR("avatar", "头像路径"),
    COVER("cover", "文章封面图片"),
    ARTICLE("article", "文章图片"),
    PHOTO("photo", "相册"),
    CONFIG("config", "配置图片"),
    MARKDOWN("markdown", "文章");

    private final String path;
    private final String desc;
}
