package top.yinzsw.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;
import top.yinzsw.blog.constant.CommonConst;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP 信息模型
 *
 * @author yinzsW
 * @since 22/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IpInfoDTO {
    private String status;

    @JsonAlias("t")
    private String timestamp;

    @JsonAlias("set_cache_time")
    private String cacheTime;

    private List<Detail> data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        @JsonAlias("ExtendedLocation")
        private String extendedLocation;

        @JsonAlias("origip")
        private String originIp;

        @JsonAlias("resourceid")
        private String resourceId;

        @JsonAlias("titlecont")
        private String title;

        private String location;
    }

    @JsonIgnore
    public Optional<String> getFirstLocation() {
        if (CollectionUtils.isEmpty(data)) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.data.get(0).location);
    }

    @JsonIgnore
    public String getProvince() {
        String location = getFirstLocation().orElse(CommonConst.UNKNOWN);

        String subLocation = location.substring(0, 2);
        if (List.of("北京", "天津", "上海", "重庆").contains(subLocation)) {
            return subLocation;
        }

        Pattern pattern = Pattern.compile("^(?<province>.*?)省");
        Matcher matcher = pattern.matcher(location);
        return matcher.find() ? matcher.group("province") : location.split(" ")[0];
    }
}
