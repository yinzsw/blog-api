package top.yinzsw.blog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.core.security.jwt.JwtManager;
import top.yinzsw.blog.core.security.jwt.JwtTokenDTO;
import top.yinzsw.blog.exception.BizException;
import top.yinzsw.blog.manager.ArticleManager;
import top.yinzsw.blog.manager.CategoryManager;
import top.yinzsw.blog.mapper.CategoryMapper;
import top.yinzsw.blog.model.converter.CategoryConverter;
import top.yinzsw.blog.model.po.ArticlePO;
import top.yinzsw.blog.model.po.CategoryPO;
import top.yinzsw.blog.model.request.CategoryReq;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.vo.CategoryBackgroundVO;
import top.yinzsw.blog.model.vo.CategoryVO;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.service.CategoryService;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【category(文章分类表)】的数据库操作Service实现
 * @createDate 2023-01-13 09:57:14
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final ArticleManager articleManager;
    private final CategoryManager categoryManager;
    private final CategoryConverter categoryConverter;

    @Override
    public PageVO<CategoryVO> pageCategories(PageReq pageReq) {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);
        List<CategoryVO> categoryVOList = categoryMapper.listCategories(pageReq, userId);
        Long count = categoryMapper.countCategories(userId);
        return PageVO.getPageVO(categoryVOList, count);
    }

    @Override
    public List<CategoryVO> listHotCategories() {
        Long userId = JwtManager.getCurrentTokenDTO().map(JwtTokenDTO::getUid).orElse(null);
        return categoryMapper.listHotCategoriesLimit10(userId);
    }

    @Override
    public PageVO<CategoryBackgroundVO> pageBackgroundCategories(PageReq pageReq, String keywords) {
        List<CategoryBackgroundVO> categoryBackgroundVOList = categoryMapper.listBackgroundCategories(pageReq, keywords);
        Long count = categoryMapper.countBackgroundCategories(keywords);
        return PageVO.getPageVO(categoryBackgroundVOList, count);
    }

    @Override
    public CategoryVO saveOrUpdateCategory(CategoryReq categoryReq) {
        CategoryPO categoryPO = categoryConverter.toCategoryPO(categoryReq);
        try {
            categoryManager.saveOrUpdate(categoryPO);
            return categoryConverter.toCategoryVO(categoryPO);
        } catch (DuplicateKeyException e) {
            throw new BizException("分类名已存在");
        }
    }

    @Override
    public boolean deleteCategories(List<Long> categoryIds) {
        if (articleManager.lambdaQuery().in(ArticlePO::getCategoryId, categoryIds).count() > 0L) {
            throw new BizException("有正在使用的文章分类, 删除失败");
        }
        return categoryManager.removeByIds(categoryIds);
    }
}