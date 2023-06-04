package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.FriendLinkReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.FriendLinkBackgroundVO;
import top.yinzsw.blog.model.vo.FriendLinkVO;
import top.yinzsw.blog.model.vo.PageVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【friend_link(友链表)】的数据库操作Service
 * @createDate 2023-01-27 23:08:47
 */
public interface FriendLinkService {

    PageVO<FriendLinkVO> pageFriendLinks(PageReq pageReq);

    /**
     * 格局关键词获取友链
     *
     * @param pageReq  分页信息
     * @param keywords 关键词
     * @return 友链信息
     */
    PageVO<FriendLinkBackgroundVO> pageBackgroundFriendLinks(PageReq pageReq, String keywords);

    /**
     * 保存或修改友链地址
     *
     * @param friendLinkReq 友链信息
     * @return 是否成功
     */
    boolean saveOrUpdateFriendLink(FriendLinkReq friendLinkReq);

    /**
     * 删除友链
     *
     * @param friendLinkIds 友链id列表
     * @return 是否成功
     */
    boolean deleteFriendLinks(List<Long> friendLinkIds);
}
