package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientApiVO;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.AI_CLIENT_API;

/**
 * @Author sws
 * @Date 2025/7/31 10:16
 * @description:
 */
@Component
@Slf4j
public class ApiNode extends AbstractArmorySupport {

    private final McpNode mcpNode;
    protected ApiNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository agentRepository, McpNode mcpNode) {
        super(applicationContext, threadPoolExecutor, agentRepository);
        this.mcpNode = mcpNode;
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，api 节点");
        List<AiClientApiVO> aiClientApiList = dynamicContext.getValue(dataName());
        if(CollectionUtils.isEmpty(aiClientApiList)){
            log.warn("没有可用的AI客户端API配置");
            return router(armoryCommandEntity, dynamicContext);
        }
        for (AiClientApiVO aiClientApiVO : aiClientApiList) {
            // 构建实例
            OpenAiApi.Builder builder = OpenAiApi.builder()
                    .baseUrl(aiClientApiVO.getBaseUrl())
                    .apiKey(aiClientApiVO.getApiKey());
            if (aiClientApiVO.getCompletionsPath() != null && !aiClientApiVO.getCompletionsPath().trim().isEmpty()) {
                builder.completionsPath(aiClientApiVO.getCompletionsPath());
            }

            if (aiClientApiVO.getEmbeddingsPath() != null && !aiClientApiVO.getEmbeddingsPath().trim().isEmpty()) {
                builder.embeddingsPath(aiClientApiVO.getEmbeddingsPath());
            }
            OpenAiApi openAiApi = builder.build();
            // 注册beanDefinition
            registerBean(beanName(aiClientApiVO.getApiId()), OpenAiApi.class, openAiApi);
        }
        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return mcpNode;
    }

    @Override
    protected String beanName(String id) {
        return AI_CLIENT_API.getBeanName(id);
    }

    @Override
    protected String dataName() {
        return AI_CLIENT_API.getDataName();
    }
}
