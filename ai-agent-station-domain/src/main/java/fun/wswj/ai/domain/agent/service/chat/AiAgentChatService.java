package fun.wswj.ai.domain.agent.service.chat;

import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.service.IAiAgentChatService;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AiAgentChatService implements IAiAgentChatService {

    @Resource
    private DefaultArmoryStrategyFactory defaultArmoryStrategyFactory;
    @Resource
    IAgentRepository agentRepository;
    @Override
    public String chat(Long aiAgentId, String message) {
       List<String> clientIdList = agentRepository.queryAiClientIdsByAiAgentId(aiAgentId);
       if (CollectionUtils.isEmpty(clientIdList)) return "当前智能体没有可用的Ai Client";
       String content = "";
       return content;
    }

    @Override
    public Flux<ChatResponse> chatStream(Long aiAgentId, Long ragId, String message) {
        return null;
    }
}
