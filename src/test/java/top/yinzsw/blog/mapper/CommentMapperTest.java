package top.yinzsw.blog.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class CommentMapperTest {
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void listReplyComments() {
        var replyVOList = commentMapper.listReplyComments(List.of(725L, 728L));
        log.info("{}", replyVOList);
    }
}