package fun.wswj.ai.infrastructure.adapter.repository;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.valobj.*;
import fun.wswj.ai.infrastructure.dao.*;
import fun.wswj.ai.infrastructure.dao.po.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.AI_CLIENT;
import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.AI_CLIENT_MODEL;
import static fun.wswj.ai.domain.agent.model.valobj.enums.TargetTypeEnum.*;

/**
 * @Author sws
 * @Date 2025/7/8 18:04
 * @description: 仓储层
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class AgentRepository implements IAgentRepository {

    private final AiClientConfigDao configDao;
    private final AiClientToolMcpDao toolMcpDao;
    private final AiClientSystemPromptDao systemPromptDao;
    private final AiClientAdvisorDao advisorDao;
    private final AiClientModelDao modelDao;
    private final AiClientApiDao apiDao;
    private final AiClientDao clientDao;
    private final AiAgentDao aiAgentDao;
    private final AiAgentFlowConfigDao aiAgentFlowConfigDao;


    @Override
    public List<AiClientMcpToolVO> queryAiClientToolMcpVOListByClientIds(List<String> clientIdlist) {
        if(CollectionUtils.isEmpty(clientIdlist)){
            return List.of();
        }
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT.getCode(), clientIdlist);
        if(CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        Set<String> toolMcpIdList = aiClientConfigList.stream()
                .filter(aiClientConfig -> TOOL_MCP.getCode().equals(aiClientConfig.getTargetType()) && aiClientConfig.getStatus() == 1)
                .map(AiClientConfig::getTargetId)
                .collect(Collectors.toSet());
        if(CollectionUtils.isEmpty(toolMcpIdList)) return List.of();
        List<AiClientToolMcp> toolMcpList = toolMcpDao.queryValidByMcpIds(toolMcpIdList.stream().toList());
        // 转换为vo
        if(CollectionUtils.isEmpty(toolMcpList)) return List.of();
        List<AiClientMcpToolVO> result = new ArrayList<>();
        toolMcpList.forEach(aiClientToolMcp -> {
            AiClientMcpToolVO aiClientMcpToolVO = new AiClientMcpToolVO();
            aiClientMcpToolVO.setMcpId(aiClientToolMcp.getMcpId());
            aiClientMcpToolVO.setMcpName(aiClientToolMcp.getMcpName());
            aiClientMcpToolVO.setTransportType(aiClientToolMcp.getTransportType());
            aiClientMcpToolVO.setTransportConfig(aiClientToolMcp.getTransportConfig());
            aiClientMcpToolVO.setRequestTimeout(aiClientToolMcp.getRequestTimeout());
            // 设置sse和stdio对象
            String transportConfig = aiClientToolMcp.getTransportConfig();
            String transportType = aiClientToolMcp.getTransportType();
            try {
                if("sse".equals(transportType)){
                    ObjectMapper objectMapper = new ObjectMapper();
                    AiClientMcpToolVO.TransportConfigSse transportConfigSse = objectMapper.readValue(transportConfig, AiClientMcpToolVO.TransportConfigSse.class);
                    aiClientMcpToolVO.setTransportConfigSse(transportConfigSse);
                } else if("stdio".equals(transportType)){
                    Map<String, AiClientMcpToolVO.TransportConfigStdio.Stdio> stdio = JSON.parseObject(transportConfig, new TypeReference<>() {
                    });
                    AiClientMcpToolVO.TransportConfigStdio transportConfigStdio = new AiClientMcpToolVO.TransportConfigStdio();
                    transportConfigStdio.setStdio(stdio);

                    aiClientMcpToolVO.setTransportConfigStdio(transportConfigStdio);
                }
            } catch (JsonProcessingException e) {
                log.error("解析mcp传输配置失败: {}", e.getMessage(), e);
            }
            result.add(aiClientMcpToolVO);
        });
        return result;
    }

    @Override
    public List<AiClientAdvisorVO> queryAiClientAdvisorVOListByClientIds(List<String> clientIdlist) {
        if (CollectionUtils.isEmpty(clientIdlist)) {
            return List.of();
        }
        // 查询客户端配置列表
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT.getCode(), clientIdlist);
        if (CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        // 提取有效的顾问ID列表
        Set<String> advisorIdList = aiClientConfigList.stream()
                .filter(aiClientConfig -> ADVISOR.getCode().equals(aiClientConfig.getTargetType()) && aiClientConfig.getStatus() == 1)
                .map(AiClientConfig::getTargetId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(advisorIdList)) return List.of();
        // 查询顾问配置
        List<AiClientAdvisor> advisorList = advisorDao.queryValidByAdvisorIds(advisorIdList.stream().toList());
        // 转换为VO
        if (CollectionUtils.isEmpty(advisorList)) return List.of();
        List<AiClientAdvisorVO> result = new ArrayList<>();
        advisorList.forEach(aiClientAdvisor -> {
            AiClientAdvisorVO advisorVO = new AiClientAdvisorVO();
            AiClientAdvisorVO.ChatMemory chatMemory = null;
            AiClientAdvisorVO.RagAnswer ragAnswer = null;
            advisorVO.setAdvisorId(aiClientAdvisor.getAdvisorId());
            advisorVO.setAdvisorName(aiClientAdvisor.getAdvisorName());
            advisorVO.setAdvisorType(aiClientAdvisor.getAdvisorType());
            advisorVO.setOrderNum(aiClientAdvisor.getOrderNum());
            String extParam = aiClientAdvisor.getExtParam();
            try {
                if(extParam != null && !extParam.trim().isEmpty()){
                    if("ChatMemory".equals(aiClientAdvisor.getAdvisorType())){
                        chatMemory = JSON.parseObject(extParam, AiClientAdvisorVO.ChatMemory.class);
                    }else if("RagAnswer".equals(aiClientAdvisor.getAdvisorType())){
                        ragAnswer = JSON.parseObject(extParam, AiClientAdvisorVO.RagAnswer.class);
                    }
                }
            } catch (Exception e) {
                // 解析失败时忽略，使用默认值null
                log.error("解析advisor配置失败: {}", e.getMessage(), e);
            }
            advisorVO.setChatMemory(chatMemory);
            advisorVO.setRagAnswer(ragAnswer);
            result.add(advisorVO);
        });
        return result;
    }

    @Override
    public List<AiClientPromptVO> queryAiClientSysTemPromptVOListByClientIds(List<String> clientIdlist) {
        if (CollectionUtils.isEmpty(clientIdlist)) {
            return List.of();
        }
        // 查询客户端配置列表
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT.getCode(), clientIdlist);
        if (CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        // 提取有效的系统提示ID列表
        Set<String> promptIdList = aiClientConfigList.stream()
                .filter(aiClientConfig -> PROMPT.getCode().equals(aiClientConfig.getTargetType()) && aiClientConfig.getStatus() == 1)
                .map(AiClientConfig::getTargetId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(promptIdList)) return List.of();
        // 查询系统提示配置
        List<AiClientSystemPrompt> promptList = systemPromptDao.queryValidByPromptIds(promptIdList.stream().toList());
        // 转换为VO
        if (CollectionUtils.isEmpty(promptList)) return List.of();
        List<AiClientPromptVO> result = new ArrayList<>();
        promptList.forEach(aiClientSystemPrompt -> {
            AiClientPromptVO promptVO = new AiClientPromptVO();
            promptVO.setPromptId(aiClientSystemPrompt.getPromptId());
            promptVO.setPromptName(aiClientSystemPrompt.getPromptName());
            promptVO.setPromptContent(aiClientSystemPrompt.getPromptContent());
            promptVO.setDescription(aiClientSystemPrompt.getDescription());
            result.add(promptVO);
        });
        return result;
    }

    @Override
    public List<AiClientModelVO> queryAiClientModelVOListByClientIds(List<String> clientIdlist) {
        if (CollectionUtils.isEmpty(clientIdlist)) {
            return List.of();
        }
        // 查询客户端配置列表
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT.getCode(), clientIdlist);
        if (CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        // 提取有效的模型ID列表
        Set<String> modelIdList = aiClientConfigList.stream()
                .filter(aiClientConfig -> MODEL.getCode().equals(aiClientConfig.getTargetType()) && aiClientConfig.getStatus() == 1)
                .map(AiClientConfig::getTargetId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(modelIdList)) return List.of();
        // 查询模型配置
        List<AiClientModel> modelList = modelDao.queryValidByModelIds(modelIdList.stream().toList());
        // 查询model配的mcp
        Map<String, List<String>> modelMcpMap = configDao.queryBySourceTypeAndIds(AI_CLIENT_MODEL.getCode(), modelIdList.stream().toList())
                .stream()
                .filter(clientConfig -> TOOL_MCP.getCode().equals(clientConfig.getTargetType()) && clientConfig.getStatus() == 1)
                .collect(Collectors.groupingBy(AiClientConfig::getSourceId, Collectors.mapping(AiClientConfig::getTargetId, Collectors.toList())));
        // 转换为VO
        if (CollectionUtils.isEmpty(modelList)) return List.of();
        List<AiClientModelVO> result = new ArrayList<>();
        modelList.forEach(aiClientModel -> {
            AiClientModelVO modelVO = new AiClientModelVO();
            modelVO.setModelId(aiClientModel.getModelId());
            modelVO.setApiId(aiClientModel.getApiId());
            modelVO.setModelName(aiClientModel.getModelName());
            modelVO.setModelType(aiClientModel.getModelType());
            modelVO.setToolMcpIds(modelMcpMap.getOrDefault(aiClientModel.getModelId(), List.of()));
            result.add(modelVO);
        });
        return result;
    }

    @Override
    public List<AiClientApiVO> queryAiClientApiVOListByClientIds(List<String> clientIdlist) {
        if (CollectionUtils.isEmpty(clientIdlist)) {
            return List.of();
        }
        // 查询客户端配置列表
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT.getCode(), clientIdlist);
        if (CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        // 提取有效的API ID列表
        Set<String> modelIdList = aiClientConfigList.stream()
                .filter(aiClientConfig -> MODEL.getCode().equals(aiClientConfig.getTargetType()) && aiClientConfig.getStatus() == 1)
                .map(AiClientConfig::getTargetId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(modelIdList)) return List.of();
        // 从model中获取apiId
        List<AiClientModel> aiClientModels = modelDao.queryValidByModelIds(modelIdList.stream().toList());
        if (CollectionUtils.isEmpty(aiClientModels)) return List.of();
        // 查询API配置
        Set<String> apiIdList = aiClientModels.stream().map(AiClientModel::getApiId).collect(Collectors.toSet());
        List<AiClientApi> apiList = apiDao.queryValidByApiIds(apiIdList.stream().toList());
        // 转换为VO
        if (CollectionUtils.isEmpty(apiList)) return List.of();
        List<AiClientApiVO> result = new ArrayList<>();
        apiList.forEach(aiClientApi -> {
            AiClientApiVO apiVO = new AiClientApiVO();
            apiVO.setApiId(aiClientApi.getApiId());
            apiVO.setBaseUrl(aiClientApi.getBaseUrl());
            apiVO.setApiKey(aiClientApi.getApiKey());
            apiVO.setCompletionsPath(aiClientApi.getCompletionsPath());
            apiVO.setEmbeddingsPath(aiClientApi.getEmbeddingsPath());
            result.add(apiVO);
        });
        return result;
    }

    @Override
    public List<AiClientVO> queryAiClientVOListByClientIds(List<String> clientIdlist) {
        if (CollectionUtils.isEmpty(clientIdlist)) {
            return List.of();
        }
        // 查询客户端配置列表
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT.getCode(), clientIdlist);
        if (CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        
        // 按客户端ID分组配置
        Map<String, List<AiClientConfig>> configMap = aiClientConfigList.stream()
                .filter(config -> config.getStatus() == 1)
                .collect(Collectors.groupingBy(AiClientConfig::getSourceId));
        
        // 获取所有客户端ID
        Set<String> clientIds = configMap.keySet();

        // 查询生效客户端基本信息
        List<AiClient> clientList = clientDao.queryValidByClientIds(clientIds.stream().toList());
        if (CollectionUtils.isEmpty(clientList)) return List.of();
        
        // 转换为VO
        List<AiClientVO> result = new ArrayList<>();
        clientList.forEach(aiClient -> {
            AiClientVO clientVO = new AiClientVO();
            clientVO.setClientId(aiClient.getClientId());
            clientVO.setClientName(aiClient.getClientName());
            clientVO.setDescription(aiClient.getDescription());
            
            // 根据目标类型分类ID
            Map<String, List<String>> targetIdsMap = new HashMap<>();
            List<AiClientConfig> configs = configMap.get(aiClient.getClientId());
            if (CollectionUtils.isNotEmpty(configs)) {
                configs.forEach(config -> {
                    String targetType = config.getTargetType();
                    targetIdsMap.computeIfAbsent(targetType, k -> new ArrayList<>()).add(config.getTargetId());
                });
            }
            
            // 设置各类ID列表
            clientVO.setModelIds(targetIdsMap.getOrDefault(MODEL.getCode(), new ArrayList<>()));
            clientVO.setPromptIds(targetIdsMap.getOrDefault(PROMPT.getCode(), new ArrayList<>()));
            clientVO.setAdvisorIds(targetIdsMap.getOrDefault(ADVISOR.getCode(), new ArrayList<>()));
            clientVO.setToolMcpIds(targetIdsMap.getOrDefault(TOOL_MCP.getCode(), new ArrayList<>()));
            
            result.add(clientVO);
        });
        
        return result;
    }

    @Override
    public List<AiClientMcpToolVO> queryAiClientToolMcpVOListByModelIds(List<String> modelIdList) {
        if(CollectionUtils.isEmpty(modelIdList)){
            return List.of();
        }
        List<AiClientConfig> aiClientConfigList = configDao.queryBySourceTypeAndIds(AI_CLIENT_MODEL.getCode(), modelIdList);
        if(CollectionUtils.isEmpty(aiClientConfigList)) return List.of();
        Set<String> toolMcpIdList = aiClientConfigList.stream()
                .filter(aiClientConfig -> TOOL_MCP.getCode().equals(aiClientConfig.getTargetType()) && aiClientConfig.getStatus() == 1)
                .map(AiClientConfig::getTargetId)
                .collect(Collectors.toSet());
        if(CollectionUtils.isEmpty(toolMcpIdList)) return List.of();
        List<AiClientToolMcp> toolMcpList = toolMcpDao.queryValidByMcpIds(toolMcpIdList.stream().toList());
        // 转换为vo
        if(CollectionUtils.isEmpty(toolMcpList)) return List.of();
        List<AiClientMcpToolVO> result = new ArrayList<>();
        toolMcpList.forEach(aiClientToolMcp -> {
            AiClientMcpToolVO aiClientMcpToolVO = new AiClientMcpToolVO();
            aiClientMcpToolVO.setMcpId(aiClientToolMcp.getMcpId());
            aiClientMcpToolVO.setMcpName(aiClientToolMcp.getMcpName());
            aiClientMcpToolVO.setTransportType(aiClientToolMcp.getTransportType());
            aiClientMcpToolVO.setTransportConfig(aiClientToolMcp.getTransportConfig());
            aiClientMcpToolVO.setRequestTimeout(aiClientToolMcp.getRequestTimeout());
            result.add(aiClientMcpToolVO);
        });
        return result;
    }

    @Override
    public List<AiClientApiVO> queryAiClientApiVOListByModelIds(List<String> modelIdlist) {
        List<AiClientModel> aiClientModels = modelDao.queryValidByModelIds(modelIdlist);
        if (CollectionUtils.isEmpty(aiClientModels)) return List.of();
        Set<String> apiIdSet = aiClientModels.stream()
                .map(AiClientModel::getApiId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(apiIdSet)) return List.of();
        List<AiClientApi> apiList = apiDao.queryValidByApiIds(apiIdSet.stream().toList());
        // 转换为VO
        if (CollectionUtils.isEmpty(apiList)) return List.of();
        List<AiClientApiVO> result = new ArrayList<>();
        apiList.forEach(aiClientApi -> {
            AiClientApiVO apiVO = new AiClientApiVO();
            apiVO.setApiId(aiClientApi.getApiId());
            apiVO.setBaseUrl(aiClientApi.getBaseUrl());
            apiVO.setApiKey(aiClientApi.getApiKey());
            apiVO.setCompletionsPath(aiClientApi.getCompletionsPath());
            apiVO.setEmbeddingsPath(aiClientApi.getEmbeddingsPath());
            result.add(apiVO);
        });
        return result;
    }

    @Override
    public List<AiClientModelVO> queryAiClientModelVOListByModelIds(List<String> modelIdlist) {
        List<AiClientModel> aiClientModels = modelDao.queryValidByModelIds(modelIdlist);
        if (CollectionUtils.isEmpty(aiClientModels)) return List.of();
        // 转换为VO
        List<AiClientModelVO> result = new ArrayList<>();
        aiClientModels.forEach(aiClientModel -> {
            AiClientModelVO modelVO = new AiClientModelVO();
            modelVO.setModelId(aiClientModel.getModelId());
            modelVO.setApiId(aiClientModel.getApiId());
            modelVO.setModelName(aiClientModel.getModelName());
            modelVO.setModelType(aiClientModel.getModelType());
            result.add(modelVO);
        });
        return result;
    }
}