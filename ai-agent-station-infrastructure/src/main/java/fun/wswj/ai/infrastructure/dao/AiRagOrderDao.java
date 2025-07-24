package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiRagOrder;

/**
 * 知识库配置表 DAO 接口
 */
@Mapper
public interface AiRagOrderDao {
    /**
     * 根据主键ID查询知识库信息
     * @param id 主键ID
     * @return 知识库信息
     */
    AiRagOrder selectById(Long id);
    
    /**
     * 插入知识库信息
     * @param aiRagOrder 知识库信息
     * @return 影响行数
     */
    int insert(AiRagOrder aiRagOrder);
    
    /**
     * 更新知识库信息
     * @param aiRagOrder 知识库信息
     * @return 影响行数
     */
    int update(AiRagOrder aiRagOrder);
    
    /**
     * 根据主键ID删除知识库信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}