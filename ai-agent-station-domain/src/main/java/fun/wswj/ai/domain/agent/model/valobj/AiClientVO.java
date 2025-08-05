package fun.wswj.ai.domain.agent.model.valobj;

import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/25 11:56
 * @description: 客户端 VO
 */
@Data
public class AiClientVO {
    private String clientId;
    
    private String clientName;
    
    private String description;

    /**
     * 全局唯一模型ID
     */
    private String modelId;

    /**
     * Prompt ID List
     */
    private List<String> promptIdList;

    /**
     * MCP ID List
     */
    private List<String> mcpIdList;

    /**
     * 顾问ID List
     */
    private List<String> advisorIdList;

    public String getChatModelName() {
        return AiAgentElementEnum.AI_CLIENT_MODEL.getBeanName(modelId);
    }

    public List<String> getMcpBeanNameList() {
        List<String> mcpBeanNameList = new ArrayList<>();
        if(CollectionUtils.isEmpty(mcpIdList)) return Collections.emptyList();
        for (String mcpId : mcpIdList) {
            mcpBeanNameList.add(AiAgentElementEnum.AI_CLIENT_TOOL_MCP.getBeanName(mcpId));
        }
        return mcpBeanNameList;
    }

    public List<String> getAdvisorBeanNameList() {
        List<String> advisorBeanNameList = new ArrayList<>();
        if(CollectionUtils.isEmpty(advisorIdList)) return Collections.emptyList();
        for (String advisorId : advisorIdList) {
            advisorBeanNameList.add(AiAgentElementEnum.AI_CLIENT_ADVISOR.getBeanName(advisorId));
        }
        return advisorBeanNameList;
    }
}