package top.yinzsw.blog.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.enums.RedisConstEnum;
import top.yinzsw.blog.manager.ViewedManager;
import top.yinzsw.blog.mapper.ViewedMapper;
import top.yinzsw.blog.model.po.ViewedPO;

import java.util.List;
import java.util.Optional;

/**
 * desc
 *
 * @author yinzsW
 * @since 23/05/09
 */
@Service
@RequiredArgsConstructor
public class ViewedManagerImpl extends ServiceImpl<ViewedMapper, ViewedPO> implements ViewedManager {
    private final ViewedMapper viewedMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Long> listWebsiteViewCountsOfLast7Days() {
        List<Long> viewCounts = viewedMapper.listWebsiteViewCountsOfLast7Days();

        Long toDayViewsCount = Optional
                .ofNullable(stringRedisTemplate.opsForSet().size(RedisConstEnum.VIEW_DAY_USERS.getKey()))
                .orElse(0L);
        viewCounts.set(6, toDayViewsCount);

        return viewCounts;
    }
}
