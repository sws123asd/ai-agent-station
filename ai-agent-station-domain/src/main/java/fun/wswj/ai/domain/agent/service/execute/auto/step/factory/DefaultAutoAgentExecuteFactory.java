package fun.wswj.ai.domain.agent.service.execute.auto.step.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiAgentClientFlowConfigVO;
import fun.wswj.ai.domain.agent.service.execute.auto.step.node.RootNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author sws
 * @Date 2025/8/8 15:31
 * @description:
 */
@Slf4j
@Component
@AllArgsConstructor
public class DefaultAutoAgentExecuteFactory {

    private final RootNode executeRootNode;
    public StrategyHandler<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> getExecuteHaandler() {
        return executeRootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        // 任务执行步骤
        private int step = 1;

        // 最大任务步骤
        private int maxStep = 1;

        private StringBuilder executionHistory;

        private String currentTask;

        boolean isCompleted = false;

        private Map<String, AiAgentClientFlowConfigVO> aiAgentClientFlowConfigVOMap;

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }
    }
}
