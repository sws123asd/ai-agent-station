package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientAdvisorVO;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiClientAdvisorTypeEnum;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.AI_CLIENT_ADVISOR;

/**
 * @Author sws
 * @Date 2025/7/24 11:46
 * @description: 访问角色装配节点
 */
@Slf4j
@Component
public class AdvisorNode extends AbstractArmorySupport {

    private final VectorStore vectorStore;
    @Resource
    private ClientNode clientNode;

    protected AdvisorNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository, VectorStore vectorStore) {
        super(applicationContext, threadPoolExecutor, repository);
        this.vectorStore = vectorStore;
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，advisor 节点");
        // 从 dynamicContext 获取配置信息
        List<AiClientAdvisorVO> AdvisorList = dynamicContext.getValue(dataName());
        if(CollectionUtils.isEmpty(AdvisorList)){
            log.warn("没有可用的AI客户端顾问角色配置");
            return router(armoryCommandEntity, dynamicContext);
        }
        // 注册bean
        for (AiClientAdvisorVO advisorVO : AdvisorList){
            Advisor advisor = createAdvisor(advisorVO);
            registerBean(beanName(advisorVO.getAdvisorId()), Advisor.class, advisor);
        }
        // 执行router进行下一节点
        return router(armoryCommandEntity, dynamicContext);
    }

    private Advisor createAdvisor(AiClientAdvisorVO advisorVO) {
        var advisorType = advisorVO.getAdvisorType();
        AiClientAdvisorTypeEnum advisorTypeEnumVO = AiClientAdvisorTypeEnum.getByType(advisorType);
        return advisorTypeEnumVO.createAdvisor(advisorVO, vectorStore);
    }

    @Override
    protected String dataName() {
        return AI_CLIENT_ADVISOR.getDataName();
    }

    @Override
    protected String beanName(String id) {
        return AI_CLIENT_ADVISOR.getBeanName(id);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return clientNode;
    }
}
