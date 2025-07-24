package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * 智能体任务调度配置表
 */
@Data
public class AiAgentTaskSchedule {
    /** 主键ID */
    private Long id;
    
    /** 智能体ID */
    private Long agentId;
    
    /** 任务名称 */
    private String taskName;
    
    /** 任务描述 */
    private String description;
    
    /** 时间表达式(如: 0/30 * * * * *) */
    private String cronExpression;
    
    /** 任务入参配置(JSON格式) */
    private String taskParam;
    
    /** 状态(0:无效,1:有效) */
    private Integer status;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;
}