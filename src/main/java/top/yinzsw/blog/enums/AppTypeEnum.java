package top.yinzsw.blog.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/06/01
 */
@Getter
@AllArgsConstructor
public enum AppTypeEnum {
    FRONT(1, "前台"),
    ADMIN(2, "后台");

    @JsonValue
    private final int value;

    /**
     * 主题类型名
     */
    private final String desc;
}
