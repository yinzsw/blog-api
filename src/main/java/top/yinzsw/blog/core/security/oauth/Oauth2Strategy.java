package top.yinzsw.blog.core.security.oauth;

import top.yinzsw.blog.model.vo.UserInfoVO;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/06/01
 */
@FunctionalInterface
public interface Oauth2Strategy {

    UserInfoVO authorize(String code);
}
