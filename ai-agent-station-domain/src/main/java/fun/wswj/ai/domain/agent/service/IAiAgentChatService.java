package fun.wswj.ai.domain.agent.service;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * @Author sws
 * @Date 2025/7/24 09:33
 * @description: 对话服务接口
 */
public interface IAiAgentChatService {

    String chat(Long aiAgentId, String message);

    Flux<ChatResponse> chatStream(Long aiAgentId, Long ragId, String message);

}
