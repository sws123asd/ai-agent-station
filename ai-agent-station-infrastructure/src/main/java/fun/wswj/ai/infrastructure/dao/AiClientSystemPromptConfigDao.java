package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientSystemPromptConfig;

/**
 * AI客户端系统提示词配置 DAO 接口
 */
@Mapper
public interface AiClientSystemPromptConfigDao {
    /**
     * 根据主键ID查询配置信息
     * @param id 主键ID
     * @return 配置信息
     */
    AiClientSystemPromptConfig selectById(Long id);
    
    /**
     * 插入配置信息
     * @param aiClientSystemPromptConfig 配置信息
     * @return 影响行数
     */
    int insert(AiClientSystemPromptConfig aiClientSystemPromptConfig);
    
    /**
     * 更新配置信息
     * @param aiClientSystemPromptConfig 配置信息
     * @return 影响行数
     */
    int update(AiClientSystemPromptConfig aiClientSystemPromptConfig);
    
    /**
     * 根据主键ID删除配置信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}