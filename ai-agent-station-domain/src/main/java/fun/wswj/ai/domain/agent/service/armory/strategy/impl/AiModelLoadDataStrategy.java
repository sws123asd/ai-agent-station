package fun.wswj.ai.domain.agent.service.armory.strategy.impl;

import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientApiVO;
import fun.wswj.ai.domain.agent.model.valobj.AiClientMcpToolVO;
import fun.wswj.ai.domain.agent.model.valobj.AiClientModelVO;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import fun.wswj.ai.domain.agent.service.armory.strategy.ILoadDataStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.Collections.emptyList;

/**
 * @Author sws
 * @Date 2025/7/30 11:42
 * @description:
 */
@Component("aiModelLoadDataStrategy")
@Slf4j
public class AiModelLoadDataStrategy implements ILoadDataStrategy {

    @Resource
    private IAgentRepository agentRepository;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void loadData(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) {
        CompletableFuture<List<AiClientMcpToolVO>> aiMcpToolListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_model_tool_mcp) {}", armoryCommandEntity.getCommandIdList());
                    return agentRepository.queryAiClientToolMcpVOListByModelIds(armoryCommandEntity.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_model_tool_mcp) {}", armoryCommandEntity.getCommandIdList(), ex);
                    return emptyList();
                });
        
        CompletableFuture<List<AiClientApiVO>> aiClientApiListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_model_api) {}", armoryCommandEntity.getCommandIdList());
                    return agentRepository.queryAiClientApiVOListByModelIds(armoryCommandEntity.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_model_api) {}", armoryCommandEntity.getCommandIdList(), ex);
                    return emptyList();
                });
        CompletableFuture<List<AiClientModelVO>> aiClientModelListFuture = CompletableFuture.supplyAsync(() -> {
                    log.info("查询配置数据(ai_model) {}", armoryCommandEntity.getCommandIdList());
                    return agentRepository.queryAiClientModelVOListByModelIds(armoryCommandEntity.getCommandIdList());
                }, threadPoolExecutor)
                .exceptionally(ex ->{
                    log.error("查询配置数据(ai_model) {}", armoryCommandEntity.getCommandIdList(), ex);
                    return emptyList();
                });

        CompletableFuture.allOf(aiMcpToolListFuture,
                        aiClientApiListFuture,
                        aiClientModelListFuture)
                .thenRun(
                        () ->{
                            dynamicContext.setValue("aiClientToolMcpList", aiMcpToolListFuture.join());
                            dynamicContext.setValue("aiClientApiList", aiClientApiListFuture.join());
                            dynamicContext.setValue("aiClientModelList", aiClientModelListFuture.join());
                        })
                .join();
    }
}
