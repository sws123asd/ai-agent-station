package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientModelConfig;

/**
 * AI客户端模型配置 DAO 接口
 */
@Mapper
public interface AiClientModelConfigDao {
    /**
     * 根据主键ID查询配置信息
     * @param id 主键ID
     * @return 配置信息
     */
    AiClientModelConfig selectById(Long id);
    
    /**
     * 插入配置信息
     * @param aiClientModelConfig 配置信息
     * @return 影响行数
     */
    int insert(AiClientModelConfig aiClientModelConfig);
    
    /**
     * 更新配置信息
     * @param aiClientModelConfig 配置信息
     * @return 影响行数
     */
    int update(AiClientModelConfig aiClientModelConfig);
    
    /**
     * 根据主键ID删除配置信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}