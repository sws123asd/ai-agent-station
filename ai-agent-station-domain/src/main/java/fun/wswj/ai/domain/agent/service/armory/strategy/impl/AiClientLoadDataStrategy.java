package fun.wswj.ai.domain.agent.service.armory.strategy.impl;

import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.*;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import fun.wswj.ai.domain.agent.service.armory.strategy.ILoadDataStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.*;
import static java.util.Collections.emptyList;

/**
 * @Author sws
 * @Date 2025/7/30 11:42
 * @description:
 */
@Component("aiClientLoadDataStrategy")
@Slf4j
public class AiClientLoadDataStrategy implements ILoadDataStrategy {
    @Resource
    private IAgentRepository agentRepository;

    @Resource
    protected ThreadPoolExecutor threadPoolExecutor;
    @Override
    public void loadData(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) {
        CompletableFuture<List<AiClientMcpToolVO>> aiMcpToolListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_client_tool_mcp) {}", requestParameter.getCommandIdList());
                    return agentRepository.queryAiClientToolMcpVOListByClientIds(requestParameter.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_tool_mcp) {}", requestParameter.getCommandIdList(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientAdvisorVO>> aiAdvisorListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_client_advisor) {}", requestParameter.getCommandIdList());
                    return agentRepository.queryAiClientAdvisorVOListByClientIds(requestParameter.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_advisor) {}", requestParameter.getCommandIdList(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientPromptVO>> aiClientPromptListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_client_system_prompt) {}", requestParameter.getCommandIdList());
                    return agentRepository.queryAiClientSysTemPromptVOListByClientIds(requestParameter.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_system_prompt) {}", requestParameter.getCommandIdList(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientApiVO>> aiClientApiListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_client_api) {}", requestParameter.getCommandIdList());
                    return agentRepository.queryAiClientApiVOListByClientIds(requestParameter.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_api) {}", requestParameter.getCommandIdList(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientModelVO>> aiClientModelListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_client_model) {}", requestParameter.getCommandIdList());
                    return agentRepository.queryAiClientModelVOListByClientIds(requestParameter.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client_model) {}", requestParameter.getCommandIdList(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientVO>> aiClientListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_client) {}", requestParameter.getCommandIdList());
                    return agentRepository.queryAiClientVOListByClientIds(requestParameter.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_client) {}", requestParameter.getCommandIdList(), ex);
                    return emptyList();
                });

        CompletableFuture.allOf(aiMcpToolListFuture,
                        aiAdvisorListFuture,
                        aiClientPromptListFuture,
                        aiClientModelListFuture,
                        aiClientListFuture)
                .thenRun(
                        () ->{
                            dynamicContext.setValue(AI_CLIENT_TOOL_MCP.getDataName(), aiMcpToolListFuture.join());
                            dynamicContext.setValue(AI_CLIENT_ADVISOR.getDataName(), aiAdvisorListFuture.join());
                            dynamicContext.setValue(AI_CLIENT_SYSTEM_PROMPT.getDataName(), aiClientPromptListFuture.join());
                            dynamicContext.setValue(AI_CLIENT_API.getDataName(), aiClientApiListFuture.join());
                            dynamicContext.setValue(AI_CLIENT_MODEL.getDataName(), aiClientModelListFuture.join());
                            dynamicContext.setValue(AI_CLIENT.getDataName(), aiClientListFuture.join());
                        })
                .join();
    }
}
