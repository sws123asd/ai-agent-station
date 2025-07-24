package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.model.entity.AiAgentEngineStarterEntity;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;

/**
 * @Author sws
 * @Date 2025/7/24 09:46
 * @description:
 */
public class RootNode extends AbstractArmorySupport {
    @Override
    protected String doApply(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return super.doApply(aiAgentEngineStarterEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<AiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return super.get(aiAgentEngineStarterEntity, dynamicContext);
    }
}
