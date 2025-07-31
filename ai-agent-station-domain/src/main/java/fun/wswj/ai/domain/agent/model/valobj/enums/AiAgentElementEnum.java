package fun.wswj.ai.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author sws
 * @Date 2025/7/30 14:44
 * @description:
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiAgentElementEnum {

    AI_CLIENT_API("对话API", "api", "ai_client_api_", "ai_client_api_data_list", "aiClientApiLoadDataStrategy"),
    AI_CLIENT_MODEL("对话模型", "model", "ai_client_model_", "ai_client_model_data_list", "aiClientModelLoadDataStrategy"),
    AI_CLIENT_SYSTEM_PROMPT("提示词", "prompt", "ai_client_system_prompt_", "ai_client_system_prompt_data_list", "aiClientSystemPromptLoadDataStrategy"),
    AI_CLIENT_TOOL_MCP("mcp工具", "tool_mcp", "ai_client_tool_mcp_", "ai_client_tool_mcp_data_list", "aiClientToolMCPLoadDataStrategy"),
    AI_CLIENT_ADVISOR("顾问角色", "advisor", "ai_client_advisor_", "ai_client_advisor_data_list", "aiClientAdvisorLoadDataStrategy"),
    AI_CLIENT("客户端", "client", "ai_client_", "ai_client_data_list", "aiClientLoadDataStrategy"),
    NULL("空", "", "", "", "")
    ;

    /**
     * 名称
     */
    private String name;

    /**
     * code
     */
    private String code;

    /**
     * Bean 对象名称标签
     */
    private String beanNameTag;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 装配数据策略
     */
    private String loadDataStrategy;

    // 静态Map用于O(1)时间复杂度查找
    private static final Map<String, AiAgentElementEnum> CODE_MAP = new HashMap<>();

    // 静态初始化块，在类加载时构建Map
    static {
        for (AiAgentElementEnum enumVO : AiAgentElementEnum.values()) {
            CODE_MAP.put(enumVO.getCode(), enumVO);
        }
    }

    public static AiAgentElementEnum getByCode(String code) {
        if (code == null) {
            return NULL;
        }

        AiAgentElementEnum result = CODE_MAP.get(code);
        if (result == null) {
            throw new RuntimeException("code value " + code + " not exist!");
        }
        return result;
    }

    /**
     * 获取Bean名称
     *
     * @param id 传入的参数
     * @return beanNameTag + id 拼接的Bean名称
     */
    public String getBeanName(String id) {
        return this.beanNameTag + id;
    }
}
