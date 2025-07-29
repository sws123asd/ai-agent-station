package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.AiAgentEngineStarterEntity;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author sws
 * @Date 2025/7/24 11:47
 * @description: 装配Chatmodel节点
 */
@Component
public class ModelNode extends AbstractArmorySupport {

    private final ClientNode clientNode;

    protected ModelNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository, ClientNode clientNode) {
        super(applicationContext, threadPoolExecutor, repository);
        this.clientNode = clientNode;
    }

    @Override
    protected String doApply(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 从 dynamicContext 获取配置信息
        // 注册bean
        // 执行router进行下一节点
        return router(aiAgentEngineStarterEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<AiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return clientNode;
    }
}
