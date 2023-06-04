package top.yinzsw.blog.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.model.request.PageReq;

@SpringBootTest
class CategoryMapperTest {
    @Autowired
    CategoryMapper categoryMapper;

    @Test
    void listCategories() {
        categoryMapper.listCategories(new PageReq(1L, 10L), 996L);
    }

    @Test
    void listBackgroundCategories() {
        categoryMapper.listBackgroundCategories(new PageReq(1L, 10L), null);
    }
}