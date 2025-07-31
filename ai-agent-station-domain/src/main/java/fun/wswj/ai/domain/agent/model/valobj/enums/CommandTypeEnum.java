package fun.wswj.ai.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author sws
 * @Date 2025/7/30 14:44
 * @description:
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommandTypeEnum {

    AI_CLIENT("客户端", "client", "ai_client_", "aiClientLoadDataStrategy"),
    AI_CLIENT_MODEL("对话模型", "model", "ai_client_model_", "aiModelLoadDataStrategy"),
    NULL("空", "", "", ""),
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
     * 装配数据策略
     */
    private String loadDataStrategy;

    public static CommandTypeEnum getByCode(String code) {
        for (CommandTypeEnum value : CommandTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NULL;
    }
}
