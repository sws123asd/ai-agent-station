package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientModelVO;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.*;

/**
 * @Author sws
 * @Date 2025/7/24 11:47
 * @description: 装配Chatmodel节点
 */
@Slf4j
@Component
public class ModelNode extends AbstractArmorySupport {

    private final AdvisorNode advisorNode;


    protected ModelNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository, ClientNode clientNode, AdvisorNode advisorNode) {
        super(applicationContext, threadPoolExecutor, repository);

        this.advisorNode = advisorNode;
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建节点，Model 节点");
        // 从 dynamicContext 获取配置信息
        List<AiClientModelVO> ModelList = dynamicContext.getValue(dataName());
        if(CollectionUtils.isEmpty(ModelList)){
            log.warn("没有可用的AI_client_model 配置");
            return router(armoryCommandEntity, dynamicContext);
        }
        // 注册bean
        for (AiClientModelVO aiClientModelVO : ModelList) {
            // 创建实例对象
            ChatModel chatModel = createChatModel(aiClientModelVO);
            // 抽象模板自定义注册方法进行注册
            registerBean(beanName(aiClientModelVO.getModelId()), ChatModel.class, chatModel);
        }
        // 执行router进行下一节点
        return router(armoryCommandEntity, dynamicContext);
    }

    private ChatModel createChatModel(AiClientModelVO aiClientModelVO) {
        // 获取spring容器注册的OpenAiApi
        OpenAiApi openAiApi = getBean(AI_CLIENT_API.getBeanName(aiClientModelVO.getApiId()));
        // 获取Spring容器注册的MCP配置
        List<McpSyncClient> mcpSyncClients = new ArrayList<>();
        List<String> toolMcpIds = aiClientModelVO.getToolMcpIds();
        for (String toolMcpId : toolMcpIds) {
            McpSyncClient mcpSyncClient = getBean(AI_CLIENT_TOOL_MCP.getBeanName(toolMcpId));
            mcpSyncClients.add(mcpSyncClient);
        }
        // 实例化对话模型（如果有其他模型对接，可以使用 one-api 服务，转换为 openai 模型格式）
        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(
                        OpenAiChatOptions.builder()
                                .model(aiClientModelVO.getModelName())
                                .toolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients).getToolCallbacks())
                                .build())
                .build();
    }

    @Override
    protected String beanName(String id) {
        return AI_CLIENT_MODEL.getBeanName(id);
    }

    @Override
    protected String dataName() {
        return AI_CLIENT_MODEL.getDataName();
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        if(AI_CLIENT_MODEL.getCode().equals(armoryCommandEntity.getCommandType())){
            return defaultStrategyHandler;
        }else {
            return advisorNode;
        }
    }
}
