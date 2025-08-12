package fun.wswj.ai.domain.agent.service.execute.auto;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;
import fun.wswj.ai.domain.agent.service.execute.IExecuteStrategy;
import fun.wswj.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author sws
 * @Date 2025/8/8 15:30
 * @description:
 */
@Slf4j
@Service
public class AutoExectueStrategy implements IExecuteStrategy {

    @Resource
    private DefaultAutoAgentExecuteFactory autoAgentExecuteFactory;
    @Override
    public void execute(ExecuteCommandEntity requestParameter) throws Exception {
        // 从工厂获取决策链头节点
        StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> executeHaandler = autoAgentExecuteFactory.getExecuteHaandler();
        // 开始执行
        log.info("auto execute strategy request:{}", requestParameter);
        executeHaandler.apply(requestParameter, new DefaultAutoAgentExecuteFactory.DynamicContext());
    }
}
