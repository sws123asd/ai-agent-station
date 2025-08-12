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
 * @Date 2025/8/8 15:33
 * @description: 任务分析节点
 */
@Slf4j
@Component
public class Step1AnalyzerNode extends AbstractExecuteSupport {


    protected Step1AnalyzerNode(ApplicationContext applicationContext, IAgentRepository agentRepository) {
        super(applicationContext, agentRepository);
    }

    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n === 正在执行第 {} 步 ===", dynamicContext.getStep());

        log.info("\n📊 阶段1: 任务状态分析");
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.TASK_ANALYZER_CLIENT.getCode());
        String analysisPrompt = String.format(aiAgentClientFlowConfigVO.getStepPrompt(),
                executeCommandEntity.getUserMessage(),
                dynamicContext.getStep(),
                dynamicContext.getMaxStep(),
                !dynamicContext.getExecutionHistory().isEmpty() ? dynamicContext.getExecutionHistory().toString() : "[首次执行]",
                dynamicContext.getCurrentTask()
        );
        ChatClient chatClient = getChatClient(aiAgentClientFlowConfigVO.getClientId());
        String analysisResult = chatClient.prompt(analysisPrompt).advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, executeCommandEntity.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1024))
                .call().content();
        assert analysisResult != null;
        parseAnalysisResult(dynamicContext.getStep(), analysisResult);
        // 将分析结果保存到动态上下文中，供下一步使用
        dynamicContext.setValue("analysisResult", analysisResult);
        // 根据prompt定制的输入形式，判断是否完成
        if (analysisResult.contains("任务状态: COMPLETED") ||
                analysisResult.contains("完成度评估: 100%")) {
            dynamicContext.setCompleted(true);
            log.info("任务分析显示已完成！");
            return router(executeCommandEntity, dynamicContext);
        }
        return router(executeCommandEntity,dynamicContext);
    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        if(dynamicContext.isCompleted() || dynamicContext.getStep() > dynamicContext.getMaxStep()){
            return getBean("step4LogExecutionSummaryNode");
        }
        return getBean("step2PrecisionExecutorNode");
    }

    /**
     * 解析分析的结果，生成控制台日志
     */
    private void parseAnalysisResult(int step, String analysisResult) {
        log.info("\n📊 === 第 {} 步分析结果 ===", step);
        log.info("\n analysisResult:\n {}", analysisResult);
    }
}
