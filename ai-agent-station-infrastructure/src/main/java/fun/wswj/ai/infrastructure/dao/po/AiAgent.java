package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * AI智能体配置表
 */
@Data
public class AiAgent {
    /** 主键ID */
    private Long id;

    /** 智能体ID */
    private String agentId;
    
    /** 智能体名称 */
    private String agentName;
    
    /** 描述 */
    private String description;
    
    /** 渠道类型(agent，chat_stream) */
    private String channel;
    
    /** 状态(0:禁用,1:启用) */
    private Integer status;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;
}