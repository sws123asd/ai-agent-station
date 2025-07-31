package fun.wswj.ai.domain.agent.model.valobj;

import lombok.Data;
import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/25 11:56
 * @description: 客户端 VO
 */
@Data
public class AiClientVO {
    private String clientId;
    
    private String clientName;
    
    private String description;
    
    private List<String> modelIds;
    
    private List<String> promptIds;
    
    private List<String> advisorIds;
    
    private List<String> toolMcpIds;
}