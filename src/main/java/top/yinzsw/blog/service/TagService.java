package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.TagReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundVO;
import top.yinzsw.blog.model.vo.TagVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service
 * @createDate 2023-01-12 22:11:32
 */
public interface TagService {

    PageVO<TagVO> pageTags(PageReq pageReq);

    List<TagVO> listHotTags();

    /**
     * 根据关键词搜索文章标签
     *
     * @param pageReq  分页信息
     * @param keywords 标签名关键词
     * @return 标签列表
     */
    PageVO<TagBackgroundVO> pageBackgroundTags(PageReq pageReq, String keywords);

    /**
     * 保存或更新标签
     *
     * @param tagReq 标签信息
     * @return 是否成功
     */
    boolean saveOrUpdateTag(TagReq tagReq);

    /**
     * 删除标签
     *
     * @param tagIds 标签id列表
     * @return 是否成功
     */
    boolean deleteTags(List<Long> tagIds);
}
