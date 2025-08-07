package fun.wswj.ai.infrastructure.dao;


import fun.wswj.ai.infrastructure.dao.po.AiAgentFlowConfig;

import java.util.List;

public interface AiAgentFlowConfigDao {

    List<AiAgentFlowConfig> queryByAiAgentId(Long agentId);
}




