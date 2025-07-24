package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientToolMcp;

/**
 * MCP客户端配置表 DAO 接口
 */
@Mapper
public interface AiClientToolMcpDao {
    /**
     * 根据主键ID查询MCP配置信息
     * @param id 主键ID
     * @return MCP配置信息
     */
    AiClientToolMcp selectById(Long id);
    
    /**
     * 插入MCP配置信息
     * @param aiClientToolMcp MCP配置信息
     * @return 影响行数
     */
    int insert(AiClientToolMcp aiClientToolMcp);
    
    /**
     * 更新MCP配置信息
     * @param aiClientToolMcp MCP配置信息
     * @return 影响行数
     */
    int update(AiClientToolMcp aiClientToolMcp);
    
    /**
     * 根据主键ID删除MCP配置信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}