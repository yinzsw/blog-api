package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.enums.LoginTypeEnum;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.UserPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.UserBackgroundVO;
import top.yinzsw.blog.model.vo.UserOnlineVO;
import top.yinzsw.blog.model.vo.UserSearchVO;
import top.yinzsw.blog.model.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author yinzsW
 * @description 针对表【user(用户表)】的数据库操作Mapper
 * @createDate 2022-12-15 14:14:31
 * @Entity top.yinzsw.blog.model.po.UserPO
 */
public interface UserMapper extends CommonMapper<UserPO> {

    List<UserSearchVO> searchUsers(@Param("keywords") String keywords);

    List<UserBackgroundVO> listBackgroundUsers(@Param("page") PageReq pageReq, @Param("keywords") String keywords);

    Page<UserOnlineVO> pageOnlineUsers(Page<Object> pager, @Param("ids") List<Long> ids, @Param("keywords") String keywords);

    Long countBackgroundUsers(@Param("keywords") String keywords);

    @MapKey("id")
    Map<Long, UserVO> getUserMap(@Param("userIds") List<Long> userIds);

    List<Long> listUserCountsOfLast7Days();

    UserPO getUserByLoginType(@Param("id") String id, @Param("type") LoginTypeEnum type);
}




