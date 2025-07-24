package fun.wswj.ai.domain.agent.model.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/24 09:55
 * @description: 智能体预热参数实体
 */
@Data
public class AiAgentEngineStarterEntity {
    /**
     * 大模型客户端id列表
     */
    private List<Long> clientIds;
}
