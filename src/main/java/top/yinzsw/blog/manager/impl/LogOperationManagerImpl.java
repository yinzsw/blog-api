package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.manager.LogOperationManager;
import top.yinzsw.blog.mapper.LogOperationMapper;
import top.yinzsw.blog.model.po.LogOperationPO;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/24
 */
@Service
@RequiredArgsConstructor
public class LogOperationManagerImpl extends ServiceImpl<LogOperationMapper, LogOperationPO> implements LogOperationManager {
}
