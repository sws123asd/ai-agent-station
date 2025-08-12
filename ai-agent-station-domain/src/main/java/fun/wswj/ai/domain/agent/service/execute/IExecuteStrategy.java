package fun.wswj.ai.domain.agent.service.execute;

import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;

/**
 * @Author sws
 * @Date 2025/8/8 15:27
 * @description:
 */
public interface IExecuteStrategy {

    void execute(ExecuteCommandEntity requestParameter) throws Exception;
}
