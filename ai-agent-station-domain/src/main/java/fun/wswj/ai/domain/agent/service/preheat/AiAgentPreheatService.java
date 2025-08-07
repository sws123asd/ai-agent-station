package fun.wswj.ai.domain.agent.service.preheat;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import fun.wswj.ai.domain.agent.service.IAiAgentPreheatService;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AiAgentPreheatService implements IAiAgentPreheatService {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;
    @Resource
    private IAgentRepository agentRepository;
    @Override
    public void preheat() throws Exception {
        List<String> validClientIdList = agentRepository.queryAllAiClientIds();
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> strategyHandler = defaultArmoryStrategyFactory.armoryStrategyHandler();
        ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
        armoryCommandEntity.setCommandType(AiAgentElementEnum.AI_CLIENT.getCode());
        armoryCommandEntity.setCommandIdList(validClientIdList);
        strategyHandler.apply(armoryCommandEntity, new DefaultArmoryStrategyFactory.DynamicContext());
    }

    @Override
    public void preheat(String aiClientId) throws Exception {
        StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> strategyHandler = defaultArmoryStrategyFactory.armoryStrategyHandler();
        ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
        armoryCommandEntity.setCommandType(AiAgentElementEnum.AI_CLIENT.getCode());
        armoryCommandEntity.setCommandIdList(List.of(aiClientId));
        strategyHandler.apply(armoryCommandEntity, new DefaultArmoryStrategyFactory.DynamicContext());

    }
}
