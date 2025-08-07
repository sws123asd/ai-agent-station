package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiAgent;

import java.util.List;

/**
 * AI智能体配置表 DAO 接口
 */
public interface AiAgentDao {

    List<AiAgent> queryAll();

    List<String> queryValidClientIds();
}