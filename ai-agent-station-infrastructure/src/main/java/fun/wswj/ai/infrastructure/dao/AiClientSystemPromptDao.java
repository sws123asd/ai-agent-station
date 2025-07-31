package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClientSystemPrompt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统提示词配置表 DAO 接口
 */
@Mapper
public interface AiClientSystemPromptDao {
    
    /**
     * 根据提示词ID列表查询有效的系统提示配置
     * @param promptIds 提示词ID列表
     * @return 系统提示配置列表
     */
    List<AiClientSystemPrompt> queryValidByPromptIds(List<String> promptIds);
}