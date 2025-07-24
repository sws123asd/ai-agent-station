package fun.wswj.ai.domain.agent.service.armory;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.model.entity.AiAgentEngineStarterEntity;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @Author sws
 * @Date 2025/7/24 09:46
 * @description: 规则树抽象方法定义 （抽象模板）
 */
public class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<AiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext, String> {

    @Override
    protected void multiThread(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }

    @Override
    protected String doApply(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return "";
    }

    @Override
    public StrategyHandler<AiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return null;
    }
}
