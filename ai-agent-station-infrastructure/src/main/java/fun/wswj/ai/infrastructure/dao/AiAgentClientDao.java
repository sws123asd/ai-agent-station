package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiAgentClient;

/**
 * 智能体-客户端关联表 DAO 接口
 */
@Mapper
public interface AiAgentClientDao {
    /**
     * 根据主键ID查询关联信息
     * @param id 主键ID
     * @return 关联信息
     */
    AiAgentClient selectById(Long id);
    
    /**
     * 插入关联信息
     * @param aiAgentClient 关联信息
     * @return 影响行数
     */
    int insert(AiAgentClient aiAgentClient);
    
    /**
     * 更新关联信息
     * @param aiAgentClient 关联信息
     * @return 影响行数
     */
    int update(AiAgentClient aiAgentClient);
    
    /**
     * 根据主键ID删除关联信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}