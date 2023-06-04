package top.yinzsw.blog.service;

import top.yinzsw.blog.model.request.PageReq;
import top.yinzsw.blog.model.request.ResourceQueryReq;
import top.yinzsw.blog.model.vo.PageVO;
import top.yinzsw.blog.model.vo.ResourceBackgroundVO;
import top.yinzsw.blog.model.vo.ResourceModuleVO;

import java.util.List;

/**
 * @author yinzsW
 * @description 针对表【resource(资源表)】的数据库操作Service
 * @createDate 2022-12-15 15:00:20
 */
public interface ResourceService {

    List<ResourceModuleVO> listModules();

    PageVO<ResourceBackgroundVO> pageBackgroundResources(PageReq pageReq, ResourceQueryReq resourceQueryReq);

    boolean updateResourceIsAnonymous(Long resourceId, Boolean isAnonymous);

    boolean updateResourceRoles(Long resourceId, List<Long> roleIds);
}
