package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.ResourcePO;

import java.util.List;

/**
 * 资源通用业务处理层
 *
 * @author yinzsW
 * @since 23/01/22
 */

public interface ResourceManager extends CommonManager<ResourcePO> {
    /**
     * 初始化资源列表
     */

    void saveInitialResources(List<ResourcePO> resources);
}
