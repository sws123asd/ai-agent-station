package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import fun.wswj.ai.domain.agent.service.armory.strategy.ILoadDataStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author sws
 * @Date 2025/7/24 09:46
 * @description: 根节点，负责获取数据库的配置信息存储在上下文中用于后续节点使用
 */
@Slf4j
@Component
public class RootNode extends AbstractArmorySupport {

    private final McpNode mcpNode;
    private final Map<String, ILoadDataStrategy> loadDataStrategyMap;

    public RootNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository agentRepository, McpNode mcpNode, Map<String, ILoadDataStrategy> loadDataStrategyMap) {
        super(applicationContext, threadPoolExecutor, agentRepository);
        this.mcpNode = mcpNode;
        this.loadDataStrategyMap = loadDataStrategyMap;
    }


    /**
     * 多线程并行处理方法 - 获取数据库配置信息
      * @param requestParameter 请求参数
      * @param dynamicContext 动态上下文
     */
    @Override
    protected void multiThread(ArmoryCommandEntity requestParameter, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) {
        // 策略模式，按照用户输入指令基于最小模块化思路进行装配
        String commandType = requestParameter.getCommandType();
        String loadDataStrategy = AiAgentElementEnum.getByCode(commandType).getLoadDataStrategy();
        ILoadDataStrategy strategyImpl = loadDataStrategyMap.get(loadDataStrategy);
        strategyImpl.loadData(requestParameter,dynamicContext);
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return mcpNode;
    }
}
