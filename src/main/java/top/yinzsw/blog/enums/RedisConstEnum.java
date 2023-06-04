package top.yinzsw.blog.enums;

import lombok.AllArgsConstructor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

/**
 * redis常量枚举类
 *
 * @author yinzsW
 * @since 23/02/02
 */
@AllArgsConstructor
public enum RedisConstEnum {

    /**
     * 网站配置
     * <br/> 不过期
     */
    WEBSITE_CONFIG("blog:config"),

    /**
     * 记录访问用户地区数量 {地区:数量}
     * {0}: 是否登录 boolean
     * <br/>不过期
     */
    VIEW_AREA("blog:view:area:{0}"),

    /**
     * 记录网站访问量, 统计PV
     * <br/>不过期
     */
    VIEW_COUNT_PV("blog:view:countBackgroundComments:pv"),

    /**
     * 记录网站日访问量, 统计UV
     * <br/>不过期
     */
    VIEW_COUNT_UV("blog:view:countBackgroundComments:uv"),

    /**
     * 记录访问用户 (用户)
     * <br/>定时清理 方便再次记录UV
     */
    VIEW_DAY_USERS("blog:view:users"),

    /**
     * 在线用户统计
     * <br/> {0}: uid
     * <br/>定时过期 15min
     */
    ONLINE_USER("blog:user:online:{0}"),

    /**
     * 记录token黑名单
     * <br/>{0}:token version id
     * <br/>定时过期
     */
    USER_BLOCK_TOKEN("blog:token:block:{0}"),

    /**
     * 主动使token过期
     * <br/> {0}: token类型 {@link TokenTypeEnum}
     * <br/> 下次登录时过期
     */
    USER_TOKEN_EXPIRE("blog:token:expire:{0}"),

    /**
     * 用户邮箱验证码 验证码
     * <br/>自动过期或使用后清理
     * <br/>0: 用户邮箱
     */
    EMAIL_CODE("blog:email:code:{0}"),

    /**
     * 文章热度排名
     * <br/>不清理
     */
    ARTICLE_HEAT_RANKING("blog:article:rank"),

    /**
     * 记录查看文章的用户 (用户)
     * <br/>{0}: 行为 {@link ActionTypeEnum}
     * <br/>{1}: 文章id
     * <br/>定时清理
     */
    ARTICLE_ACTION_USERS("blog:article:{0}:{1}");

    private final String key;

    public String getKey(Object... params) {
        Object[] stringParams = Arrays.stream(params).map(Objects::toString).toArray();
        return MessageFormat.format(key, stringParams);
    }

    /**
     * 网站 -> 浏览(pv/uv)
     * 文章 -> 点赞, 评论 , 转发, 收藏, 浏览   //排名
     * 评论(留言) -> 点(倒)赞
     * 相册
     * 说说
     */

}
