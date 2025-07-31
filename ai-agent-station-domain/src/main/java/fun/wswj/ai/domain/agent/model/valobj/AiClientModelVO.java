package fun.wswj.ai.domain.agent.model.valobj;


import lombok.Data;

import java.util.List;

@Data
public class AiClientModelVO {
    /**
     * 全局唯一模型ID
     */
    private String modelId;

    /**
     * 关联的API配置ID
     */
    private String apiId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型类型：openai、deepseek、claude
     */
    private String modelType;

    /**
     * 工具 mcp ids
     */
    private List<String> toolMcpIds;
}
