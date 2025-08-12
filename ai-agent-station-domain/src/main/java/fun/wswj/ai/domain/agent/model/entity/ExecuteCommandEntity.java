package fun.wswj.ai.domain.agent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author sws
 * @Date 2025/8/8 15:39
 * @description: 执行agent 命令实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCommandEntity {
    /* 智能体id */
    private String agentId;
    /* 用户输入 */
    private String userMessage;
    /* 会话ID AiClient上下文使用 */
    private String sessionId;
    /* 最大执行步长*/
    private Integer maxStep;
}
