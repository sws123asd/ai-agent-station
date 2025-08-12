package fun.wswj.ai.domain.agent.service.execute.auto.step;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ExecuteCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum;
import fun.wswj.ai.domain.agent.service.execute.auto.step.factory.DefaultAutoAgentExecuteFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.ApplicationContext;

/**
 * @Author sws
 * @Date 2025/7/24 09:46
 * @description: client执行 - 规则树抽象方法定义 （抽象模板）
 */
public abstract class AbstractExecuteSupport extends AbstractMultiThreadStrategyRouter<ExecuteCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext, String> {

    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";
    public static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";

    protected final ApplicationContext applicationContext;
    protected final IAgentRepository agentRepository;

    protected AbstractExecuteSupport(ApplicationContext applicationContext, IAgentRepository agentRepository) {
        this.applicationContext = applicationContext;
        this.agentRepository = agentRepository;
    }

    protected ChatClient getChatClient(String clientId){
        return getBean(AiAgentElementEnum.AI_CLIENT.getBeanName(clientId));
    }

    protected <T> T getBean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }

    @Override
    protected void multiThread(ExecuteCommandEntity executeCommandEntity, DefaultAutoAgentExecuteFactory.DynamicContext dynamicContext) {}
}
