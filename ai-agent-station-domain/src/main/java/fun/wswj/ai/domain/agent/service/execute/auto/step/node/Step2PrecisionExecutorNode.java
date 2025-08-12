package fun.wswj.ai.domain.agent.service.execute.auto.step.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiClientTypeEnumVO;
import fun.wswj.ai.domain.agent.service.execute.auto.step.AbstractExecuteSupport;
import fun.wswj.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author sws
 * @Date 2025/8/8 15:34
 * @description: 精准任务执行节点
 */
@Slf4j
@Component
public class Step2PrecisionExecutorNode extends AbstractExecuteSupport {
    protected Step2PrecisionExecutorNode(ApplicationContext applicationContext, IAgentRepository agentRepository) {
        super(applicationContext, agentRepository);
    }

    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n⚡ 阶段2: 精准任务执行");

        String analysisResult = dynamicContext.getValue("analysisResult");
        if (analysisResult == null || analysisResult.trim().isEmpty()) {
            log.warn("分析结果为空，使用默认执行策略");
            analysisResult = "执行当前任务步骤";
        }
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.PRECISION_EXECUTOR_CLIENT.getCode());
        String executionPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(), executeCommandEntity.getUserMessage(), analysisResult);

        String clientId = aiAgentClientFlowConfigVO.getClientId();
        ChatClient precisionExecutorClient = getChatClient(clientId);
        String executionResult = precisionExecutorClient
                .prompt(executionPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, executeCommandEntity.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 120))
                .call().content();

        assert executionResult != null;
        parseExecutionResult(dynamicContext.getStep(), executionResult);
        dynamicContext.setValue("executionResult", executionResult);
        return router(executeCommandEntity, dynamicContext);
    }

    private void parseExecutionResult(int step, String executionResult) {
        log.info("\n⚡ === 第 {} 步执行结果 ===", step);
        log.info("\n executionResult:\n {}", executionResult);

    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        return getBean("step3QualitySupervisorNode");
    }
}
