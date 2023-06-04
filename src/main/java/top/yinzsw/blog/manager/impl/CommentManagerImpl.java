package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.enums.TopicTypeEnum;
import top.yinzsw.blog.manager.CommentManager;
import top.yinzsw.blog.mapper.CommentMapper;
import top.yinzsw.blog.model.po.CommentPO;

/**
 * 评论通用业务处理层实现
 *
 * @author yinzsW
 * @since 23/02/13
 */
@Service
public class CommentManagerImpl extends ServiceImpl<CommentMapper, CommentPO> implements CommentManager {

    @Override
    public Long getMessageCount() {
        return lambdaQuery().eq(CommentPO::getTopicType, TopicTypeEnum.MESSAGE).count();
    }
}
