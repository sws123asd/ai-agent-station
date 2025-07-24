package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientAdvisor;

/**
 * 顾问配置表 DAO 接口
 */
@Mapper
public interface AiClientAdvisorDao {
    /**
     * 根据主键ID查询顾问信息
     * @param id 主键ID
     * @return 顾问信息
     */
    AiClientAdvisor selectById(Long id);
    
    /**
     * 插入顾问信息
     * @param aiClientAdvisor 顾问信息
     * @return 影响行数
     */
    int insert(AiClientAdvisor aiClientAdvisor);
    
    /**
     * 更新顾问信息
     * @param aiClientAdvisor 顾问信息
     * @return 影响行数
     */
    int update(AiClientAdvisor aiClientAdvisor);
    
    /**
     * 根据主键ID删除顾问信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}