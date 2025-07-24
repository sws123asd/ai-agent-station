package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiAgent;

/**
 * AI智能体配置表 DAO 接口
 */
@Mapper
public interface AiAgentDao {
    /**
     * 根据主键ID查询智能体信息
     * @param id 主键ID
     * @return 智能体信息
     */
    AiAgent selectById(Long id);
    
    /**
     * 插入智能体信息
     * @param aiAgent 智能体信息
     * @return 影响行数
     */
    int insert(AiAgent aiAgent);
    
    /**
     * 更新智能体信息
     * @param aiAgent 智能体信息
     * @return 影响行数
     */
    int update(AiAgent aiAgent);
    
    /**
     * 根据主键ID删除智能体信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}