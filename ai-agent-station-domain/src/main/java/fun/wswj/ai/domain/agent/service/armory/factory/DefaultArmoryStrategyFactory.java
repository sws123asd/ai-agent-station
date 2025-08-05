package fun.wswj.ai.domain.agent.service.armory.factory;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.service.armory.node.RootNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author sws
 * @Date 2025/7/24 10:01
 * @description: 装配工厂
 */
@Component
public class DefaultArmoryStrategyFactory {

    private final RootNode rootNode;

    private final ApplicationContext applicationContext;

    public DefaultArmoryStrategyFactory(RootNode rootNode, ApplicationContext applicationContext) {
        this.rootNode = rootNode;
        this.applicationContext = applicationContext;
    }


    public ChatClient chatClient(Long clientId) {
        return (ChatClient) applicationContext.getBean("ChatClient_" + clientId);
    }

    public ChatModel chatModel(Long modelId) {
        return (ChatModel) applicationContext.getBean("AiClientModel_" + modelId);
    }

    /**
     * 返回根节点 - 开始初始化树结构
     */
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> armoryStrategyHandler() {
        return rootNode;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private int level;

        private Map<String, Object> dataObjects = new HashMap<>();

        public <T> void setValue(String key, T value) {
            dataObjects.put(key, value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }

    }
}
