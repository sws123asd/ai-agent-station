package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClientApi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * API配置表 DAO 接口
 */
@Mapper
public interface AiClientApiDao {
    
    /**
     * 根据API ID列表查询有效的API配置
     * @param apiIds API ID列表
     * @return API配置列表
     */
    List<AiClientApi> queryValidByApiIds(List<String> apiIds);
}