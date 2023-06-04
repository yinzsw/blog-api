package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举
 *
 * @author yinzsW
 * @since 22/12/15
 */

@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 默认登录
     */
    DEFAULT(0, null),
    /**
     * 邮箱登录
     */
    EMAIL(1, null),
    /**
     * 手机登录
     */
    PHONE(2, null),
    /**
     * QQ登录
     */
    QQ(3, "qqOauth2"),
    /**
     * 微博登录
     */
    WEIBO(4, "weiboOauth2"),
    /**
     * github登录
     */
    GITHUB(5, "githubOauth2");

    /**
     * 登录方式
     */
    private final int value;

    /**
     * 描述
     */
    private final String strategyName;
}
