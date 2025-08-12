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
 * @Date 2025/8/8 15:36
 * @description:
 */
@Slf4j
@Component
public class Step4LogExecutionSummaryNode extends AbstractExecuteSupport {

    protected Step4LogExecutionSummaryNode(ApplicationContext applicationContext, IAgentRepository agentRepository) {
        super(applicationContext, agentRepository);
    }

    @Override
    protected String doApply(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        log.info("\n📊 === 执行第 {} 步 ===", dynamicContext.getStep() -1 );

        // 第四阶段：执行总结
        log.info("\n📊 阶段4: 执行总结分析");

        // 记录执行总结
        logExecutionSummary(dynamicContext.getMaxStep(), dynamicContext.getExecutionHistory(), dynamicContext.isCompleted());

        // 生成最终总结报告（无论任务是否完成都需要生成）
        generateFinalReport(executeCommandEntity, dynamicContext);

        log.info("\n🏁 === 动态多轮执行结束 ====");

        return "ai agent execution summary completed!";
    }

    private void logExecutionSummary(int maxStep, StringBuilder executionHistory, boolean completed) {
        log.info("\n📊 === 动态多轮执行总结 ====");

        int actualSteps = Math.min(maxStep, executionHistory.toString().split("=== 第").length - 1);
        log.info("📈 总执行步数: {} 步", actualSteps);

        if (completed) {
            log.info("✅ 任务完成状态: 已完成");
        } else {
            log.info("⏸️ 任务完成状态: 未完成（达到最大步数限制）");
        }

        // 计算执行效率
        double efficiency = completed ? 100.0 : (double) actualSteps / maxStep * 100;
        log.info("📊 执行效率: {}%", efficiency);
    }

    private void generateFinalReport(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) {
        // 根据完成标识输出不同的报告
        boolean isCompleted = dynamicContext.isCompleted();
        log.info("\n--- 生成{}任务的最终答案 ---", isCompleted ? "已完成" : "未完成");
        // 生成prompt
        String summaryPrompt = getSummaryPrompt(executeCommandEntity, dynamicContext, isCompleted);
        AiAgentClientFlowConfigVO aiAgentClientFlowConfigVO = dynamicContext.getAiAgentClientFlowConfigVOMap().get(AiClientTypeEnumVO.RESPONSE_ASSISTANT.getCode());
        ChatClient chatClient = getChatClient(aiAgentClientFlowConfigVO.getClientId());
        String summaryResult = chatClient.prompt(summaryPrompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, executeCommandEntity.getSessionId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 50))
                .call().content();

        assert summaryResult != null;
        logFinalReport(dynamicContext, summaryResult, executeCommandEntity.getSessionId());
        // 将总结结果保存到动态上下文中
        dynamicContext.setValue("finalSummary", summaryResult);
    }

    private String getSummaryPrompt(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext, boolean isCompleted) {
        String summaryPrompt;
        if (isCompleted) {
            summaryPrompt = String.format("""
                    基于以下执行过程，请直接回答用户的原始问题，提供最终的答案和结果：
                    
                    **用户原始问题:** %s
                    
                    **执行历史和过程:**
                    %s
                    
                    **要求:**
                    1. 直接回答用户的原始问题
                    2. 基于执行过程中获得的信息和结果
                    3. 提供具体、实用的最终答案
                    4. 如果是要求制定计划、列表等，请直接给出完整的内容
                    5. 避免只描述执行过程，重点是最终答案
                    6. 以MD语法的表格形式，优化展示结果数据
                    
                    请直接给出用户问题的最终答案：
                    """,
                    executeCommandEntity.getUserMessage(),
                    dynamicContext.getExecutionHistory().toString());
        } else {
            summaryPrompt = String.format("""
                    虽然任务未完全执行完成，但请基于已有的执行过程，尽力回答用户的原始问题：
                    
                    **用户原始问题:** %s
                    
                    **已执行的过程和获得的信息:**
                    %s
                    
                    **要求:**
                    1. 基于已有信息，尽力回答用户的原始问题
                    2. 如果信息不足，说明哪些部分无法完成并给出原因
                    3. 提供已能确定的部分答案
                    4. 给出完成剩余部分的具体建议
                    5. 以MD语法的表格形式，优化展示结果数据
                    
                    请基于现有信息给出用户问题的答案：
                    """,
                    executeCommandEntity.getUserMessage(),
                    dynamicContext.getExecutionHistory().toString());
        }
        return summaryPrompt;
    }

    private void logFinalReport(DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext, String summaryResult, String sessionId) {
        boolean isCompleted = dynamicContext.isCompleted();
        log.info("\n📋 === {}任务最终总结报告 ===", isCompleted ? "已完成" : "未完成");
        log.info("\n任务最终答案：\n{}", summaryResult);

    }

    @Override
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> get(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }
}
