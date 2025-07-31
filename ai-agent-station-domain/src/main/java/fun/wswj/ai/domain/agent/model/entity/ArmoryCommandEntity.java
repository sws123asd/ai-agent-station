package fun.wswj.ai.domain.agent.model.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/24 09:55
 * @description: 智能体预热参数实体
 */
@Data
public class ArmoryCommandEntity {
    /**
     * 命令类型
     */
    private String commandType;

    /**
     * 命令索引（clientId、modelId、apiId...）
     */
    private List<String> commandIdList;
}
