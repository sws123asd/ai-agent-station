package fun.wswj.ai.domain.agent.model.valobj;

import lombok.Data;

/**
 * @Author sws
 * @Date 2025/8/7 17:53
 * @description:
 */
@Data
public class AiClientRagVo {
    private String ragId;
    private String ragName;
    private String knowledgeTag;
    private Integer status;
}
