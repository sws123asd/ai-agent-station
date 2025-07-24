package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiClientSystemPrompt;

/**
 * 系统提示词配置表 DAO 接口
 */
@Mapper
public interface AiClientSystemPromptDao {
    /**
     * 根据主键ID查询提示词信息
     * @param id 主键ID
     * @return 提示词信息
     */
    AiClientSystemPrompt selectById(Long id);
    
    /**
     * 插入提示词信息
     * @param aiClientSystemPrompt 提示词信息
     * @return 影响行数
     */
    int insert(AiClientSystemPrompt aiClientSystemPrompt);
    
    /**
     * 更新提示词信息
     * @param aiClientSystemPrompt 提示词信息
     * @return 影响行数
     */
    int update(AiClientSystemPrompt aiClientSystemPrompt);
    
    /**
     * 根据主键ID删除提示词信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}