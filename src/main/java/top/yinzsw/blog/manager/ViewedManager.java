package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.ViewedPO;

import java.util.List;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/09
 */

public interface ViewedManager extends CommonManager<ViewedPO> {

    List<Long> listWebsiteViewCountsOfLast7Days();
}
