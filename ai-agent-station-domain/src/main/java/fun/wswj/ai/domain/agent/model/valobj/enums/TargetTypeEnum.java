package fun.wswj.ai.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author sws
 * @Date 2025/7/30 15:31
 * @description: 配置信息目标对象类型枚举
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TargetTypeEnum {

    MODEL("模型", "model"),
    PROMPT("系统提示", "prompt"),
    TOOL_MCP("工具", "tool_mcp"),
    ADVISOR("助手", "advisor"),
    NULL("空", "");

    private String name;
    private String code;
}
