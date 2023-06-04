package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import top.yinzsw.blog.model.po.LogOperationPO;
import top.yinzsw.blog.model.request.LogQueryReq;
import top.yinzsw.blog.model.vo.LogBackgroundVO;

public interface LogOperationMapper extends BaseMapper<LogOperationPO> {
    Page<LogBackgroundVO> pageLogs(Page<LogBackgroundVO> pager, @Param("query") LogQueryReq logQueryReq);
}