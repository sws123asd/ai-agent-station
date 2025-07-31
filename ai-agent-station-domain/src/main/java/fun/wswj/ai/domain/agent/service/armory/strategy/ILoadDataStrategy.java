package fun.wswj.ai.domain.agent.service.armory.strategy;

import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;

/**
 * @Author sws
 * @Date 2025/7/30 11:41
 * @description:
 */
public interface ILoadDataStrategy {

    void loadData(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext);
}
