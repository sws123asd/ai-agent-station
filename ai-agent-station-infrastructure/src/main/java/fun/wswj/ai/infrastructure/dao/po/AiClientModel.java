package fun.wswj.ai.infrastructure.dao.po;

import lombok.Data;
import java.util.Date;

/**
 * AI接口模型配置表
 */
@Data
public class AiClientModel {
    /** 主键ID */
    private Long id;
    
    /** 模型名称 */
    private String modelName;
    
    /** 基础URL */
    private String baseUrl;
    
    /** API密钥 */
    private String apiKey;
    
    /** 完成路径 */
    private String completionsPath;
    
    /** 嵌入路径 */
    private String embeddingsPath;
    
    /** 模型类型(openai/azure等) */
    private String modelType;
    
    /** 模型版本 */
    private String modelVersion;
    
    /** 超时时间(秒) */
    private Integer timeout;
    
    /** 状态(0:禁用,1:启用) */
    private Integer status;
    
    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;
}