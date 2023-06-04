package top.yinzsw.blog.manager;

import top.yinzsw.blog.extension.mybatisplus.CommonManager;
import top.yinzsw.blog.model.po.CommentPO;

/**
 * 评论通用业务处理层
 *
 * @author yinzsW
 * @since 23/02/13
 */

public interface CommentManager extends CommonManager<CommentPO> {
    Long getMessageCount();
}
