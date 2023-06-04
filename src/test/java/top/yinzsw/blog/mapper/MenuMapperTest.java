package top.yinzsw.blog.mapper;

import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.model.vo.MenuBackgroundVO;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
class MenuMapperTest {
    @Autowired
    private MenuMapper menuMapper;

    @Test
    void listBackgroundMenus() {
        List<MenuBackgroundVO> menuBackgroundVOS = menuMapper.listBackgroundMenus();

        Map<Long, List<MenuBackgroundVO>> menusMap = SimpleQuery
                .listGroupBy(menuBackgroundVOS, MenuBackgroundVO::getParentId);
        System.out.println(menusMap.get(null));
    }
}