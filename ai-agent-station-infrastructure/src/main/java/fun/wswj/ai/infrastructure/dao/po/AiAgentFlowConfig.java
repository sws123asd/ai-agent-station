package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;

import java.util.Date;

/**
 * 智能体-客户端关联表
 */
@Data
public class AiAgentFlowConfig {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 智能体ID
     */
    private String agentId;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 序列号(执行顺序)
     */
    private Integer sequence;

    /**
     * 步骤提示
     */
    private String stepPrompt;

    /**
     * 创建时间
     */
    private Date createTime;
}