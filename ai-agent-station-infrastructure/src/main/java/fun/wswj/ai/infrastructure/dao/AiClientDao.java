package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClient;

/**
 * AI客户端配置表 DAO 接口
 */
@Mapper
public interface AiClientDao {
    /**
     * 根据主键ID查询客户端信息
     * @param id 主键ID
     * @return 客户端信息
     */
    AiClient selectById(Long id);
    
    /**
     * 插入客户端信息
     * @param aiClient 客户端信息
     * @return 影响行数
     */
    int insert(AiClient aiClient);
    
    /**
     * 更新客户端信息
     * @param aiClient 客户端信息
     * @return 影响行数
     */
    int update(AiClient aiClient);
    
    /**
     * 根据主键ID删除客户端信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}