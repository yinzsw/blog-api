package top.yinzsw.blog.mapper;

import top.yinzsw.blog.extension.mybatisplus.CommonMapper;
import top.yinzsw.blog.model.po.ViewedPO;

import java.util.List;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/09
 */

public interface ViewedMapper extends CommonMapper<ViewedPO> {

    List<Long> listWebsiteViewCountsOfLast7Days();
}
