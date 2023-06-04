package top.yinzsw.blog.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
public class UserPO {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户头像
     */
    private String github;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * 用户个人网站
     */
    private String website;

    /**
     * 是否禁用
     */
    private Boolean isDisabled;

    /**
     * 账号绑定信息
     * qq
     * weibo
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserAccount accounts;

    /**
     * 上次登录信息
     * ip: ip地址
     * address: 位置
     * time: 时间
     * device: 设备
     * platform: 软件平台
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private UserLogin lastLogin;

    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserAccount {
        private String qq;
        private String weibo;
        private String github;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserLogin {
        private String ip;
        private String address;
        private LocalDateTime time;
        private String device;
        private String platform;
    }
}

