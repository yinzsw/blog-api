package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.FriendLinkPO;
import top.yinzsw.blog.model.vo.FriendLinkBackgroundVO;

/**
 * @author yinzsW
 * @description 针对表【friend_link(友链表)】的数据库操作Mapper
 * @createDate 2023-01-27 23:08:47
 * @Entity top.yinzsw.blog.model.po.FriendLinkPO
 */
public interface FriendLinkMapper extends CommonMapper<FriendLinkPO> {

    /**
     * 根据关键词搜索友链
     *
     * @param pager    分页器
     * @param keywords 关键词
     * @return 友链 分页模型
     */
    Page<FriendLinkBackgroundVO> pageSearchFriendLinks(Page<FriendLinkPO> pager, @Param("keywords") String keywords);
}




