package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.ResourceManager;
import top.yinzsw.blog.mapper.ResourceMapper;
import top.yinzsw.blog.model.po.ResourcePO;
import top.yinzsw.blog.model.po.RoleMtmResourcePO;

import java.util.List;

/**
 * 资源通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceManagerImpl extends ServiceImpl<ResourceMapper, ResourcePO> implements ResourceManager {


    @Async
    @Override
    public void saveInitialResources(List<ResourcePO> resources) {
        remove(null);
        saveBatch(resources);

        List<RoleMtmResourcePO> mtmResourcePOList = SimpleQuery.list2List(resources, resourceId -> new RoleMtmResourcePO().setResourceId(resourceId.getId()).setRoleId(1L));
        Db.lambdaUpdate(RoleMtmResourcePO.class).remove();
        Db.saveBatch(mtmResourcePOList);
    }
}
