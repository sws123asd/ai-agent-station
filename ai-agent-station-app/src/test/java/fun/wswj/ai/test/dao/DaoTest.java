package fun.wswj.ai.test.dao;

import fun.wswj.ai.infrastructure.dao.AiAgentDao;
import fun.wswj.ai.infrastructure.dao.po.AiAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/29 17:49
 * @description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DaoTest {
    @Resource
    private AiAgentDao aiAgentDao;

    @Test
    public void test_queryById() {
        List<AiAgent> aiAgents = aiAgentDao.queryAll();
        aiAgents.forEach(aiAgent -> log.info("查询结果: {}", aiAgent));

    }



}
