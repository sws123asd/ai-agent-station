package fun.wswj.ai.domain.agent.model.valobj;

import lombok.Data;

/**
 * @Author sws
 * @Date 2025/8/11 11:56
 * @description:
 */
@Data
public class AiAgentClientFlowConfigVO {

    private String agentId;
    private String clientId;
    private String clientName;
    private String clientType;
    private Integer sequence;
    private String stepPrompt;
}
