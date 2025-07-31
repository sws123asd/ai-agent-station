package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户端配置表 DAO 接口
 */
@Mapper
public interface AiClientDao {
    
    /**
     * 根据客户端ID列表查询有效的客户端配置
     * @param clientIds 客户端ID列表
     * @return 客户端配置列表
     */
    List<AiClient> queryValidByClientIds(List<String> clientIds);
}