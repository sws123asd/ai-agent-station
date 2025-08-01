package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * 顾问配置表
 */
@Data
public class AiClientAdvisor {
    /** 主键ID */
    private Long id
            ;
    /** 顾问ID */
    private String advisorId;
    
    /** 顾问名称 */
    private String advisorName;
    
    /** 顾问类型(PromptChatMemory/RagAnswer/SimpleLoggerAdvisor等) */
    private String advisorType;
    
    /** 顺序号 */
    private Integer orderNum;
    
    /** 扩展参数配置，json 记录 */
    private String extParam;
    
    /** 状态(0:禁用,1:启用) */
    private Integer status;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;
}