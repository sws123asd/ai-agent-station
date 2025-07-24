package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientToolConfig;

/**
 * 客户端-MCP关联表 DAO 接口
 */
@Mapper
public interface AiClientToolConfigDao {
    /**
     * 根据主键ID查询配置信息
     * @param id 主键ID
     * @return 配置信息
     */
    AiClientToolConfig selectById(Long id);
    
    /**
     * 插入配置信息
     * @param aiClientToolConfig 配置信息
     * @return 影响行数
     */
    int insert(AiClientToolConfig aiClientToolConfig);
    
    /**
     * 更新配置信息
     * @param aiClientToolConfig 配置信息
     * @return 影响行数
     */
    int update(AiClientToolConfig aiClientToolConfig);
    
    /**
     * 根据主键ID删除配置信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}