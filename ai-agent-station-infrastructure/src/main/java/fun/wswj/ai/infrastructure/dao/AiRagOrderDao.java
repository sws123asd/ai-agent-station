package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClientRagOrder;

/**
 * 知识库配置表 DAO 接口
 */
public interface AiRagOrderDao {

    void createTagOrder(AiClientRagOrder aiClientRagOrder);
}