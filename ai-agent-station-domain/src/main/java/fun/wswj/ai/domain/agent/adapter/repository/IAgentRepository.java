package fun.wswj.ai.domain.agent.adapter.repository;

import fun.wswj.ai.domain.agent.model.valobj.*;

import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/8 18:06
 * @description:
 */
public interface IAgentRepository {

    List<AiClientMcpToolVO> queryAiClientToolMcpVOListByClientIds(List<String> clientIdlist);

    List<AiClientAdvisorVO> queryAiClientAdvisorVOListByClientIds(List<String> clientIdlist);

    List<AiClientPromptVO> queryAiClientSysTemPromptVOListByClientIds(List<String> clientIdlist);

    List<AiClientModelVO> queryAiClientModelVOListByClientIds(List<String> clientIdlist);

    List<AiClientVO> queryAiClientVOListByClientIds(List<String> clientIdlist);

    List<AiClientApiVO> queryAiClientApiVOListByClientIds(List<String> clientIdlist);

    List<AiClientMcpToolVO> queryAiClientToolMcpVOListByModelIds(List<String> modelIdList);

    List<AiClientApiVO> queryAiClientApiVOListByModelIds(List<String> modelIdlist);

    List<AiClientModelVO> queryAiClientModelVOListByModelIds(List<String> modelIdlist);
}
