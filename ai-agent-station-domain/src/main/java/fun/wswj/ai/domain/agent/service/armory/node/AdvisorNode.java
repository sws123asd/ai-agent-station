package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author sws
 * @Date 2025/7/24 11:46
 * @description: 访问角色装配节点
 */
@Slf4j
@Component
public class AdvisorNode extends AbstractArmorySupport {

    private final ClientNode clientNode;

    protected AdvisorNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository, ModelNode modelNode, ClientNode clientNode) {
        super(applicationContext, threadPoolExecutor, repository);
        this.clientNode = clientNode;
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        // 从 dynamicContext 获取配置信息
        // 注册bean
        // 执行router进行下一节点
        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return clientNode;
    }
}
