package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClientModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 模型配置表 DAO 接口
 */
@Mapper
public interface AiClientModelDao {
    
    /**
     * 根据模型ID列表查询有效的模型配置
     * @param modelIds 模型ID列表
     * @return 模型配置列表
     */
    List<AiClientModel> queryValidByModelIds(List<String> modelIds);
}