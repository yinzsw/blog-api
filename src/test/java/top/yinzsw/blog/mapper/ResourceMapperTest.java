package top.yinzsw.blog.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ResourceMapperTest {
    private @Autowired ResourceMapper resourceMapper;

    @Test
    void getResourceRoleMap() {
        var resourceRoleMap = resourceMapper.listRoleResources();
        log.info("{}", resourceRoleMap);
    }

    @Test
    void listRoleResources() {
    }
}