package fun.wswj.ai.domain.agent.model.valobj;

import lombok.Data;

/**
 * @Author sws
 * @Date 2025/7/30 14:53
 * @description:
 */
@Data
public class AiClientApiVO {
    /**
     * API ID
     */
    private String apiId;

    /**
     * 基础URL
     */
    private String baseUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 对话补全路径
     */
    private String completionsPath;

    /**
     * 嵌入向量路径
     */
    private String embeddingsPath;
}
