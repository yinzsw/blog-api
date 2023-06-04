package top.yinzsw.blog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.service.CategoryService;

@Slf4j
@SpringBootTest
class CategoryServiceImplTest {
    @Autowired
    CategoryService categoryService;

    @Test
    void listCategories() {
    }
}