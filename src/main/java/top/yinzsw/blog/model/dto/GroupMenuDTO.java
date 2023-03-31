package top.yinzsw.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.yinzsw.blog.model.po.RolePO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yinzsW
 * @since 23/03/02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GroupMenuDTO {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * icon
     */
    private String iconPath;

    /**
     * 是否隐藏
     */
    private Boolean isHidden;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 角色列表
     */
    private List<RolePO> roles;
}
