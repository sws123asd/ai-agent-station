package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientPromptVO;
import fun.wswj.ai.domain.agent.model.valobj.AiClientVO;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.AI_CLIENT;

/**
 * @Author sws
 * @Date 2025/7/24 11:47
 * @description: 装配ChatClient节点
 */
@Slf4j
@Component
public class ClientNode extends AbstractArmorySupport {

    protected ClientNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository) {
        super(applicationContext, threadPoolExecutor, repository);
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，client 节点");
        List<AiClientVO> clientList = dynamicContext.getValue(dataName());
        if(CollectionUtils.isEmpty(clientList)){
            log.warn("没有可用的AI客户端配置");
            return router(armoryCommandEntity, dynamicContext);
        }
        Map<String, AiClientPromptVO> systemPromptMap = dynamicContext.getValue(AiAgentElementEnum.AI_CLIENT_SYSTEM_PROMPT.getDataName());
        for (AiClientVO clientVO : clientList){
            // 1、系统提示词
            StringBuilder defaultSystem = new StringBuilder("Ai 智能体 \r\n");
            List<String> promptIdList = clientVO.getPromptIdList();
            if (CollectionUtils.isNotEmpty(promptIdList)) {
                for (String promptId : promptIdList) {
                    AiClientPromptVO promptVO = systemPromptMap.get(promptId);
                    if (promptVO != null) {
                        defaultSystem.append(promptVO.getPromptContent());
                    }
                }
            }
            // 2、模型
            ChatModel chatModel = getBean(clientVO.getChatModelName());
            // 3、工具 （可选）model上已经配置
            List<McpSyncClient> mcpSyncClients = new ArrayList<>();
            List<String> mcpBeanNameList = clientVO.getMcpBeanNameList();
            for (String mcpBeanName : mcpBeanNameList) {
                mcpSyncClients.add(getBean(mcpBeanName));
            }
            // 4、顾问角色
            List<Advisor> advisors = new ArrayList<>();
            List<String> advisorBeanNameList = clientVO.getAdvisorBeanNameList();
            for (String advisorName : advisorBeanNameList) {
                advisors.add(getBean(advisorName));
            }
            // 构建client
            ChatClient client = ChatClient.builder(chatModel)
                    .defaultSystem(defaultSystem.toString())
                    .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
                    .defaultAdvisors(advisors)
                    .build();
            registerBean(beanName(clientVO.getClientId()), ChatClient.class, client);
        }
        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    protected String beanName(String id) {
        return AI_CLIENT.getBeanName(id);
    }

    @Override
    protected String dataName() {
        return AI_CLIENT.getDataName();
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }
}
