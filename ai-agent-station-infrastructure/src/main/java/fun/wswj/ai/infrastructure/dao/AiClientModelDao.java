package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientModel;

/**
 * AI接口模型配置表 DAO 接口
 */
@Mapper
public interface AiClientModelDao {
    /**
     * 根据主键ID查询模型信息
     * @param id 主键ID
     * @return 模型信息
     */
    AiClientModel selectById(Long id);
    
    /**
     * 插入模型信息
     * @param aiClientModel 模型信息
     * @return 影响行数
     */
    int insert(AiClientModel aiClientModel);
    
    /**
     * 更新模型信息
     * @param aiClientModel 模型信息
     * @return 影响行数
     */
    int update(AiClientModel aiClientModel);
    
    /**
     * 根据主键ID删除模型信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}