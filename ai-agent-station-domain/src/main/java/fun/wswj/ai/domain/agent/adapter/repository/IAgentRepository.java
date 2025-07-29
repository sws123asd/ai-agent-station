package fun.wswj.ai.domain.agent.adapter.repository;

import fun.wswj.ai.domain.agent.model.valobj.*;

import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/8 18:06
 * @description:
 */
public interface IAgentRepository {

    List<AiClientMcpToolVO> queryAiClientToolMcpVOListByClientIds(List<Long> clientIds);

    List<AiClientAdvisorVO> queryAiClientAdvisorVOListByClientIds(List<Long> clientIds);

    List<AiClientPromptVO> queryAiClientSysTemPromptVOListByClientIds(List<Long> clientIds);

    List<AiClientModelVO> queryAiClientModelVOListByClientIds(List<Long> clientIds);

    List<AiClientVO> queryAiClientVOListByClientIds(List<Long> clientIds);
}
