package top.yinzsw.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Oauth2AccessTokenDTO {
    @JsonAlias("access_token")
    private String accessToken = "";

    @JsonIgnore
    public String getBearerToken() {
        return "Bearer ".concat(accessToken);
    }
}
