package fun.wswj.ai.test.domain;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import fun.wswj.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static fun.wswj.ai.domain.agent.service.execute.auto.step.AbstractExecuteSupport.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static fun.wswj.ai.domain.agent.service.execute.auto.step.AbstractExecuteSupport.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @Author sws
 * @Date 2025/8/12 10:45
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AutoAgentTest {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;

    @Resource
    private DefaultAutoAgentExecuteFactory defaultAutoAgentExecuteStrategyFactory;

    @Resource
    private ApplicationContext applicationContext;

    private ChatModel chatModel;
    @Resource
    private PgVectorStore vectorStore;


    @Before
    public void init() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();

        ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
        armoryCommandEntity.setCommandIdList(Arrays.asList("3101", "3102", "3103", "3104"));
        armoryCommandEntity.setCommandType(AiAgentElementEnum.AI_CLIENT.getCode());
        armoryStrategyHandler.apply(armoryCommandEntity,
                new DefaultArmoryStrategyFactory.DynamicContext());

    }

    @Test
    public void test_search_agent() {
        ChatClient chatClient = (ChatClient) applicationContext.getBean(AiAgentElementEnum.AI_CLIENT.getBeanName("3101"));
        String content = chatClient.prompt(new Prompt(new UserMessage("搜索小傅哥")))
                .call().content();
        log.info("搜索结果：{}", content);
    }

    @Test
    public void test_aiClient() throws Exception {

        String analysisPrompt = String.format("""
                        **原始用户需求:** %s
                        
                        **当前执行步骤:** 第 %d 步 (最大 %d 步)
                        
                        **历史执行记录:**
                        %s
                        
                        **当前任务:** %s
                        
                        请分析当前任务状态，评估执行进度，并制定下一步策略。
                        """,
                "搜索小傅哥，技术项目列表。编写成一份文档，说明不同项目的学习目标，以及不同阶段的伙伴应该学习哪个项目。",
                1,
                3,
                "[首次执行]",
                "搜索小傅哥，技术项目列表。编写成一份文档，说明不同项目的学习目标，以及不同阶段的伙伴应该学习哪个项目。"
        );
        ChatClient chatClient = (ChatClient) applicationContext.getBean(AiAgentElementEnum.AI_CLIENT.getBeanName("3101"));
        String analysisResult = chatClient.prompt(analysisPrompt).advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, "session-id-" + System.currentTimeMillis())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1024))
                .call().content();

        log.info("测试结果:{}", analysisResult);
    }

    @Test
    public void autoAgent() throws Exception {
        StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> executeHaandler = defaultAutoAgentExecuteStrategyFactory.getExecuteHaandler();
        ExecuteCommandEntity executeCommandEntity = new ExecuteCommandEntity("3",
                "搜索并思考2025年Java3年工作经验的后端开发人员面试该准备哪些内容，请你编写一份md文档，将文档保存在/Users/joyboy/Desktop下。",
                "session-id-" + System.currentTimeMillis(),
                3);
        String apply = executeHaandler.apply(executeCommandEntity, new DefaultAutoAgentExecuteFactory.DynamicContext());
        log.info("测试结果:{}", apply);
    }
}
