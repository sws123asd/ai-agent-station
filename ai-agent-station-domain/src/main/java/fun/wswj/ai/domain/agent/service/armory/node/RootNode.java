package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.AiAgentEngineStarterEntity;
import fun.wswj.ai.domain.agent.model.valobj.*;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.Collections.emptyList;

/**
 * @Author sws
 * @Date 2025/7/24 09:46
 * @description: 根节点，负责获取数据库的配置信息存储在上下文中用于后续节点使用
 */
@Slf4j
@Component
public class RootNode extends AbstractArmorySupport {

    private final McpNode mcpNode;

    public RootNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository agentRepository, McpNode mcpNode) {
        super(applicationContext, threadPoolExecutor, agentRepository);
        this.mcpNode = mcpNode;
    }


    /**
     * 多线程并行处理方法 - 获取数据库配置信息
      * @param requestParameter 请求参数
      * @param dynamicContext 动态上下文
     */
    @Override
    protected void multiThread(AiAgentEngineStarterEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) {
        CompletableFuture<List<AiClientMcpToolVO>> aiMcpToolListFuture = CompletableFuture.supplyAsync(() -> {
            log.info("查询配置数据(ai_client_tool_mcp) {}", requestParameter.getClientIds());
            return agentRepository.queryAiClientToolMcpVOListByClientIds(requestParameter.getClientIds());
        }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_tool_mcp) {}", requestParameter.getClientIds(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientAdvisorVO>> aiAdvisorListFuture = CompletableFuture.supplyAsync(() -> {
            log.info("查询配置数据(ai_client_advisor) {}", requestParameter.getClientIds());
            return agentRepository.queryAiClientAdvisorVOListByClientIds(requestParameter.getClientIds());
        }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_tool_mcp) {}", requestParameter.getClientIds(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientPromptVO>> aiClientPromptListFuture = CompletableFuture.supplyAsync(() -> {
            log.info("查询配置数据(ai_client_system_prompt) {}", requestParameter.getClientIds());
            return agentRepository.queryAiClientSysTemPromptVOListByClientIds(requestParameter.getClientIds());
        }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_tool_mcp) {}", requestParameter.getClientIds(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientModelVO>> aiClientModelListFuture = CompletableFuture.supplyAsync(() -> {
            log.info("查询配置数据(ai_client_model) {}", requestParameter.getClientIds());
            return agentRepository.queryAiClientModelVOListByClientIds(requestParameter.getClientIds());
        }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_model) {}", requestParameter.getClientIds(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientVO>> aiClientListFuture = CompletableFuture.supplyAsync(() -> {
            log.info("查询配置数据(ai_client) {}", requestParameter.getClientIds());
            return agentRepository.queryAiClientVOListByClientIds(requestParameter.getClientIds());
        }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client) {}", requestParameter.getClientIds(), ex);
                    return emptyList();
                });


        CompletableFuture.allOf(aiMcpToolListFuture,
                                aiAdvisorListFuture,
                                aiClientPromptListFuture,
                                aiClientModelListFuture,
                                aiClientListFuture)
                .thenRun(
                () ->{
                    dynamicContext.setValue("aiClientToolMcpList", aiMcpToolListFuture.join());
                    dynamicContext.setValue("aiClientAdvisorList", aiAdvisorListFuture.join());
                    dynamicContext.setValue("aiSystemPromptConfig", aiClientPromptListFuture.join());
                    dynamicContext.setValue("aiClientModelList", aiClientModelListFuture.join());
                    dynamicContext.setValue("aiClientList", aiClientListFuture.join());
                })
                .join();
    }

    @Override
    protected String doApply(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return router(aiAgentEngineStarterEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<AiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return mcpNode;
    }
}
