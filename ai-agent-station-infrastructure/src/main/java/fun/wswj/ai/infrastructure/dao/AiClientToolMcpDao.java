package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClientToolMcp;

import java.util.List;

/**
 * MCP客户端配置表 DAO 接口
 */
public interface AiClientToolMcpDao {

    List<AiClientToolMcp> queryValidByMcpIds(List<String> mcpIdList);
}