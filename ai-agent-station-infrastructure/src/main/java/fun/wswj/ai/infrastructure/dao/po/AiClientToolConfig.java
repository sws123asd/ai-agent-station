package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * 客户端-MCP关联表
 */
@Data
public class AiClientToolConfig {
    /** 主键ID */
    private Long id;
    
    /** 客户端ID */
    private Long clientId;
    
    /** 工具类型(mcp/function call) */
    private String toolType;
    
    /** MCP ID/ function call ID */
    private Long toolId;
    
    /** 创建时间 */
    private Date createTime;
}