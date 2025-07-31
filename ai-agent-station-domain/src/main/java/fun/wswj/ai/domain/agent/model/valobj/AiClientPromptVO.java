package fun.wswj.ai.domain.agent.model.valobj;

import lombok.Data;

/**
 * @Author sws
 * @Date 2025/7/25 11:56
 * @description: 系统提示词VO
 */
@Data
public class AiClientPromptVO {
    /** 提示词ID */
    private String promptId;
    
    /** 提示词名称 */
    private String promptName;
    
    /** 提示词内容 */
    private String promptContent;
    
    /** 描述 */
    private String description;
}