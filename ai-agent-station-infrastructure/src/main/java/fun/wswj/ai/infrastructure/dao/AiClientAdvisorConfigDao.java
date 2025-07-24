package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientAdvisorConfig;

/**
 * 客户端-顾问关联表 DAO 接口
 */
@Mapper
public interface AiClientAdvisorConfigDao {
    /**
     * 根据主键ID查询关联信息
     * @param id 主键ID
     * @return 关联信息
     */
    AiClientAdvisorConfig selectById(Long id);
    
    /**
     * 插入关联信息
     * @param aiClientAdvisorConfig 关联信息
     * @return 影响行数
     */
    int insert(AiClientAdvisorConfig aiClientAdvisorConfig);
    
    /**
     * 更新关联信息
     * @param aiClientAdvisorConfig 关联信息
     * @return 影响行数
     */
    int update(AiClientAdvisorConfig aiClientAdvisorConfig);
    
    /**
     * 根据主键ID删除关联信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}