package top.yinzsw.blog.model.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * 分页数据模型
 *
 * @author yinzsW
 * @since 23/01/09
 */
@Data
@Accessors(chain = true)
@Schema(description = "分页数据")
public class PageVO<T> {

    /**
     * 分页列表
     */
    @Schema(title = "记录列表")
    private List<T> records;

    /**
     * 数据总条数
     */
    @Schema(title = "总记录数")
    private Long count;

    private PageVO() {
    }

    public static <T> PageVO<T> getEmptyPageVO() {
        return PageVO.getPageVO(Collections.emptyList(), 0L);
    }

    public static <T> PageVO<T> getPageVO(IPage<T> page) {
        return PageVO.getPageVO(page.getRecords(), page.getTotal());
    }

    public static <T> PageVO<T> getPageVO(List<T> records, Long count) {
        return new PageVO<T>().setRecords(records).setCount(count);
    }
}
