package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientModelToolConfig;

/**
 * AI客户端模型工具配置 DAO 接口
 */
@Mapper
public interface AiClientModelToolConfigDao {
    /**
     * 根据主键ID查询配置信息
     * @param id 主键ID
     * @return 配置信息
     */
    AiClientModelToolConfig selectById(Long id);
    
    /**
     * 插入配置信息
     * @param aiClientModelToolConfig 配置信息
     * @return 影响行数
     */
    int insert(AiClientModelToolConfig aiClientModelToolConfig);
    
    /**
     * 更新配置信息
     * @param aiClientModelToolConfig 配置信息
     * @return 影响行数
     */
    int update(AiClientModelToolConfig aiClientModelToolConfig);
    
    /**
     * 根据主键ID删除配置信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}