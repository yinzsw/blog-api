package top.yinzsw.blog.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yinzsw.blog.manager.ResourceManager;
import top.yinzsw.blog.mapper.ResourceMapper;
import top.yinzsw.blog.model.converter.ResourceConverter;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;
import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.ResourceQueryReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.ResourceBackgroundVO;
import top.yinzsw.blog.model.vo.ResourceModuleVO;
import top.yinzsw.blog.service.ResourceService;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Service实现
 * @createDate 2022-12-15 15:00:20
 */
@Service
@CacheConfig(cacheNames = "resource")
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceMapper resourceMapper;
    private final ResourceManager resourceManager;
    private final ResourceConverter resourceConverter;

    @SuppressWarnings("unchecked")
    @Override
    public List<ResourceModuleVO> listModules() {
        List<ResourcePO> resourcePOList = resourceManager.lambdaQuery()
                .select(ResourcePO::getModule, ResourcePO::getModuleName)
                .groupBy(ResourcePO::getModule, ResourcePO::getModuleName)
                .orderByAsc(ResourcePO::getModuleName)
                .list();
        return resourceConverter.toResourceModuleVO(resourcePOList);
    }

    @Override
    public PageVO<ResourceBackgroundVO> pageBackgroundResources(PageReq pageReq, ResourceQueryReq resourceQueryReq) {
        List<ResourceBackgroundVO> resources = resourceMapper.listBackgroundResources(pageReq, resourceQueryReq);
        Long count = resourceMapper.countBackgroundResources(resourceQueryReq);
        return PageVO.getPageVO(resources, count);
    }

    @Override
    public boolean updateResourceIsAnonymous(Long resourceId, Boolean isAnonymous) {
        return resourceManager.updateById(new ResourcePO().setId(resourceId).setIsAnonymous(isAnonymous));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateResourceRoles(Long resourceId, List<Long> roleIds) {
        Db.lambdaUpdate(RoleMtmResourcePO.class).eq(RoleMtmResourcePO::getResourceId, resourceId).remove();
        List<RoleMtmResourcePO> roleMtmResourcePOS = SimpleQuery
                .list2List(roleIds, roleId -> new RoleMtmResourcePO().setRoleId(roleId).setResourceId(resourceId));
        return Db.saveBatch(roleMtmResourcePOS);
    }
}




