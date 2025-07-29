package fun.wswj.ai.domain.agent.model.valobj;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AiClientModelVO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 基础URL
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 完成路径
     */
    private String completionsPath;

    /**
     * 嵌入路径
     */
    private String embeddingsPath;

    /**
     * 模型类型(openai/azure等)
     */
    private String modelType;

    /**
     * 模型版本
     */
    private String modelVersion;

    /**
     * 超时时间(秒)
     */
    private Integer timeout;

    /**
     * 工具配置
     */
    private List<AIClientModelToolConfigVO> aiClientModelToolConfigs;

    @Data
    public static class AIClientModelToolConfigVO {

        /** 主键ID */
        private Integer id;
        /** 模型ID */
        private Long modelId;
        /** 工具类型(mcp/function call) */
        private String toolType;
        /** MCP ID/ function call ID */
        private Long toolId;
        /** 创建时间 */
        private Date createTime;

    }
}
