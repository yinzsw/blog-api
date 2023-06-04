package top.yinzsw.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/06/01
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Oauth2UserInfoDTO {

    @JsonAlias("id")
    private String id;

    @JsonAlias("login")
    private String nickname;

    @JsonAlias("avatar_url")
    private String avatar;
}
