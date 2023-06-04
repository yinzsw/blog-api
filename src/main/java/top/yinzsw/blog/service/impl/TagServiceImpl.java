package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.TagManager;
import top.yinzsw.blog.mapper.TagMapper;
import top.yinzsw.blog.model.converter.TagConverter;
import top.yinzsw.blog.model.po.ArticleMtmTagPO;
import top.yinzsw.blog.model.po.TagPO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.TagReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.TagBackgroundVO;
import top.yinzsw.blog.model.vo.TagVO;
import top.yinzsw.blog.service.TagService;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【tag(标签表)】的数据库操作Service实现
 * @createDate 2023-01-12 22:11:32
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagMapper tagMapper;
    private final TagManager tagManager;
    private final TagConverter tagConverter;

    @Override
    public PageVO<TagVO> pageTags(PageReq pageReq) {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);
        List<TagVO> tagVOList = tagMapper.listTags(pageReq, userId);
        Long count = tagMapper.countTags(userId);
        return PageVO.getPageVO(tagVOList, count);
    }

    @Override
    public List<TagVO> listHotTags() {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);
        return tagMapper.listHotTagsLimit10(userId);
    }

    @Override
    public PageVO<TagBackgroundVO> pageBackgroundTags(PageReq pageReq, String keywords) {
        List<TagBackgroundVO> tagBackgroundVOList = tagMapper.listBackgroundTags(pageReq, keywords);
        Long count = tagMapper.countBackgroundTags(keywords);
        return PageVO.getPageVO(tagBackgroundVOList, count);
    }

    @Override
    public boolean saveOrUpdateTag(TagReq tagReq) {
        TagPO tagPO = tagConverter.toTagPO(tagReq);
        try {
            return tagManager.saveOrUpdate(tagPO);
        } catch (DuplicateKeyException e) {
            throw new BizException("标签名已存在");
        }
    }

    @Override
    public boolean deleteTags(List<Long> tagIds) {
        if (Db.lambdaQuery(ArticleMtmTagPO.class).in(ArticleMtmTagPO::getTagId, tagIds).count() > 0) {
            throw new BizException("有正在使用的文章标签, 删除失败");
        }
        return tagManager.removeByIds(tagIds);
    }
}




