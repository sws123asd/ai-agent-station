package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * AI客户端，零部件；模型配置
 */
@Data
public class AiClientSystemPromptConfig {
    /** 主键ID */
    private Long id;
    
    /** 客户端ID */
    private Long clientId;
    
    /** 系统提示词ID */
    private Long systemPromptId;
    
    /** 创建时间 */
    private Date createTime;
}