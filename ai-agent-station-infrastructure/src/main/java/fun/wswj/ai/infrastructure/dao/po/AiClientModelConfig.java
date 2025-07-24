package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * AI客户端，零部件；模型配置
 */
@Data
public class AiClientModelConfig {
    /** 主键ID */
    private Long id;
    
    /** 客户端ID */
    private Long clientId;
    
    /** 模型ID */
    private Long modelId;
    
    /** 创建时间 */
    private Date createTime;
}