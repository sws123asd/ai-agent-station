package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * AI客户端配置表
 */
@Data
public class AiClient {
    /** 主键ID */
    private Long id;
    
    /** 客户端名称 */
    private String clientName;
    
    /** 描述 */
    private String description;
    
    /** 状态(0:禁用,1:启用) */
    private Integer status;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;
}