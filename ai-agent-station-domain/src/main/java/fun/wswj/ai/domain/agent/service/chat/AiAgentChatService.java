package fun.wswj.ai.domain.agent.service.chat;

import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.service.IAiAgentChatService;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class AiAgentChatService implements IAiAgentChatService {
    public static final String CHAT_MEMORY_CONVERSATION_ID_KEY = "chat_memory_conversation_id";
    public static final String CHAT_MEMORY_RETRIEVE_SIZE_KEY = "chat_memory_response_size";

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;
    @Resource
    IAgentRepository agentRepository;
    @Override
    public String chat(Long aiAgentId, String message) {
       List<String> clientIdList = agentRepository.queryAiClientIdsByAiAgentId(aiAgentId);
       if (CollectionUtils.isEmpty(clientIdList)) return "当前智能体没有可用的Ai Client";
        String content = "";
       for (String clientId : clientIdList) {
           ChatClient chatClient = defaultArmoryStrategyFactory.chatClient(clientId);
           content = chatClient.prompt(message + "," + content)
                   .system(s -> s.param("current_date", LocalDate.now().toString()))
                   .advisors(a -> a
                           .param(CHAT_MEMORY_CONVERSATION_ID_KEY,"chatId-101")
                           .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                   )
                   .call().content();
       }
       return content;
    }

    @Override
    public Flux<ChatResponse> chatStream(Long aiAgentId, Long ragId, String message) {
        return null;
    }
}
