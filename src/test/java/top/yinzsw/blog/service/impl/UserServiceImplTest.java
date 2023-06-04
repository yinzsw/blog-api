package top.yinzsw.blog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yinzsw.blog.service.UserService;

@Slf4j
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    void getUserRegionMap() {
        userService.getUserRegionMap(true);
    }
}