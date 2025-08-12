package fun.wswj.ai.domain.agent.service.execute.auto.step.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import fun.wswj.ai.domain.agent.service.execute.auto.step.AbstractExecuteSupport;
import fun.wswj.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author sws
 * @Date 2025/8/8 15:32
 * @description:
 */
@Slf4j
@Component("executeRootNode")
public class RootNode extends AbstractExecuteSupport {

    private final Step1AnalyzerNode step1AnalyzerNode;

    protected RootNode(ApplicationContext applicationContext, IAgentRepository agentRepository, Step1AnalyzerNode step1AnalyzerNode) {
        super(applicationContext, agentRepository);
        this.step1AnalyzerNode = step1AnalyzerNode;
    }

    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        // 1 获取 ai_agent_flow_config配置
        List<AiAgentClientFlowConfigVO> agentClientFlowConfigVOList = agentRepository.queryAgentFlowConfigByAgentId(executeCommandEntity.getAgentId());
        // 2 设置 dynamicContext
        Map<String, AiAgentClientFlowConfigVO> clientFlowConfigVOMap = agentClientFlowConfigVOList.stream().collect(Collectors.toMap(AiAgentClientFlowConfigVO::getClientType, config -> config));
        dynamicContext.setAiAgentClientFlowConfigVOMap(clientFlowConfigVOMap);
        // 上下文信息
        dynamicContext.setExecutionHistory(new StringBuilder());
        // 当前任务信息
        dynamicContext.setCurrentTask(executeCommandEntity.getUserMessage());
        // 最大任务步骤
        dynamicContext.setMaxStep(executeCommandEntity.getMaxStep());

        return router(executeCommandEntity,dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        return step1AnalyzerNode;
    }
}
