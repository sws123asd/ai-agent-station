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
 * @Date 2025/8/8 15:35
 * @description: 质量监督节点
 */
@Slf4j
@Component
public class Step3QualitySupervisorNode extends AbstractExecuteSupport {
    protected Step3QualitySupervisorNode(ApplicationContext applicationContext, IAgentRepository agentRepository) {
        super(applicationContext, agentRepository);
    }

    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n阶段3: 质量监督检查");

        String userInput = executeCommandEntity.getUserMessage();
        String executionResult = dynamicContext.getValue("executionResult");
        if (executionResult == null || executionResult.trim().isEmpty()) {
            log.warn("执行结果为空，跳过质量监督");
            return "质量监督跳过";
        }
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.QUALITY_SUPERVISOR_CLIENT.getCode());
        String supervisionPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(), userInput, executionResult);
        ChatClient qualitySupervisorClient = getChatClient(aiAgentClientFlowConfigVO.getClientId());
        String supervisionResult = qualitySupervisorClient
                .prompt(supervisionPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, executeCommandEntity.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 80))
                .call().content();

        parseSupervisionResult(dynamicContext.getStep(), supervisionResult);
        dynamicContext.setValue("supervisionResult", supervisionResult);

        assert supervisionResult != null;
        // 根据监督结果决定是否需要重新执行
        if (supervisionResult.matches("(?s).*是否通过:.*FAIL.*")) {
            log.info("❌ 质量检查未通过，需要重新执行");
            dynamicContext.setCurrentTask("根据质量监督的建议重新执行任务");
        } else if (supervisionResult.matches("(?s).*是否通过:.*OPTIMIZE.*")) {
            log.info("🔧 质量检查建议优化，继续改进");
            dynamicContext.setCurrentTask("根据质量监督的建议优化执行结果");
        } else {
            log.info("✅ 质量检查通过");
            dynamicContext.setCompleted(true);
        }

        // 更新执行历史
        String stepSummary = String.format("""
                === 第 %d 步 执行记录 ===
                【分析阶段】%s
                【执行阶段】%s
                【监督阶段】%s
                """,
                dynamicContext.getStep(),
                dynamicContext.getValue("analysisResult"),
                executionResult,
                supervisionResult);

        dynamicContext.getExecutionHistory().append(stepSummary);
        // 增加步骤计数
        dynamicContext.setStep(dynamicContext.getStep() + 1);

        return router(executeCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        // 如果任务已完成或达到最大步数，进入总结阶段
        if(dynamicContext.isCompleted() || dynamicContext.getStep() > dynamicContext.getMaxStep()){
            return getBean("step4LogExecutionSummaryNode");
        }
        // 执行下一轮分析
        return getBean("step1AnalyzerNode");
    }

    private void parseSupervisionResult(int step, String supervisionResult) {
        log.info("\n🔍 === 第 {} 步监督结果 ===", step);
        log.info("\n🔍 === supervisionResult:\n {}", supervisionResult);

    }
}
