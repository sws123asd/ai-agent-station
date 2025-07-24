package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * AI客户端，零部件；模型工具配置
 */
@Data
public class AiClientModelToolConfig {
    /** 主键ID */
    private Long id;
    
    /** 模型ID */
    private Long modelId;
    
    /** 工具类型(mcp/function call) */
    private String toolType;
    
    /** MCP ID/ function call ID */
    private Long toolId;
    
    /** 创建时间 */
    private Date createTime;
}