package fun.wswj.ai.test;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @Author sws
 * @Date 2025/8/4 17:57
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AiAgentTest {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;

    @Resource
    private ApplicationContext applicationContext;

    @Autowired
    private OpenAiChatModel openAiChatModel;
    @Test
    public void test_call() {
        Prompt prompt = new Prompt(
                "2*5=?",
                OpenAiChatOptions.builder()
                        .model("qwen-plus-2025-01-25")
                        .build());
        ChatResponse response = openAiChatModel.call(prompt);
        log.info("测试结果(call):{}", JSON.toJSONString(response));
    }
    @Test
    public void test_aiClientModelNode() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();

        ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
        armoryCommandEntity.setCommandIdList(Arrays.asList("2001","2002"));
        armoryCommandEntity.setCommandType(AiAgentElementEnum.AI_CLIENT_MODEL.getCode());
        armoryStrategyHandler.apply(armoryCommandEntity,
                new DefaultArmoryStrategyFactory.DynamicContext());
        OpenAiChatModel openAiChatModel = (OpenAiChatModel) applicationContext.getBean(AiAgentElementEnum.AI_CLIENT_MODEL.getBeanName("2002"));

        log.info("模型构建:{}", openAiChatModel);

        // 1. 有哪些工具可以使用
        // 2. 在 /Users/fuzhengwei/Desktop 创建 txt.md 文件
        Prompt prompt = Prompt.builder()
                .messages(new UserMessage(
                        """
                                  2的多少次方等于1024？
                                """))
                .build();

        ChatResponse chatResponse = openAiChatModel.call(prompt);

        log.info("测试结果(call):{}", JSON.toJSONString(chatResponse));
    }

    @Test
    public void test_aiClient() throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler =
                defaultArmoryStrategyFactory.armoryStrategyHandler();

        ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
        armoryCommandEntity.setCommandIdList(Arrays.asList("3001"));
        armoryCommandEntity.setCommandType(AiAgentElementEnum.AI_CLIENT.getCode());
        armoryStrategyHandler.apply(armoryCommandEntity,
                new DefaultArmoryStrategyFactory.DynamicContext());

        ChatClient chatClient = (ChatClient) applicationContext.getBean(AiAgentElementEnum.AI_CLIENT.getBeanName("3001"));
        log.info("客户端构建:{}", chatClient);

        ChatClientResponse chatClientResponse = chatClient.prompt(Prompt.builder()
                .messages(new UserMessage(
                        """
                                在 /Users/joyboy/Desktop 创建 txt.md 文件，文件中写入“测试MCP操作”文字信息,如果存在文件，则将文件中的信息后面加上递增序号。
                                """))
                .build()).call().chatClientResponse();

        log.info("测试结果(call):{}", JSON.toJSONString(chatClientResponse));
    }
}
