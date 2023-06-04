package top.yinzsw.blog.util;

import java.util.regex.Pattern;

/**
 * 校验工具类
 *
 * @author yinzsW
 * @since 23/01/02
 */

public class VerifyUtils {
    /**
     * 判断字符穿是否是邮箱
     *
     * @param email 字符串
     * @return 是否是邮箱
     */
    public static boolean isEmail(String email) {
        return Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").matcher(email).matches();
    }

    /**
     * 判断字符串是不是全字母
     *
     * @param alpha 字符串
     * @return 是否为全字母
     */
    public static boolean isAlpha(String alpha) {
        return Pattern.compile("^[a-zA-Z]+$").matcher(alpha).matches();
    }

    /**
     * 判断字符串是否时手机号
     *
     * @param phone 字符串
     * @return 是否时手机号
     */
    public static boolean isPhone(String phone) {
        return Pattern.compile("^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$").matcher(phone).matches();
    }

    /**
     * 判断字符串是否是合法的ipv4地址
     *
     * @param ipAddress ip地址
     * @return 是否为ipv4地址
     */
    public static boolean isIpv4(String ipAddress) {
        return Pattern.compile("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$").matcher(ipAddress).matches();
    }
}
