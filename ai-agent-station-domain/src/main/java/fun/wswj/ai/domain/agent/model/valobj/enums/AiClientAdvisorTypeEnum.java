package fun.wswj.ai.domain.agent.model.valobj.enums;

import fun.wswj.ai.domain.agent.model.valobj.AiClientAdvisorVO;
import fun.wswj.ai.domain.agent.service.armory.factory.element.RagAnswerAdvisor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author sws
 * @Date 2025/8/1 11:01
 * @description:
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AiClientAdvisorTypeEnum {
    CHAT_MEMORY("ChatMemory", "上下文记忆（内存模式）") {
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO advisorVO, VectorStore vectorStore) {
            AiClientAdvisorVO.ChatMemory chatMemory = advisorVO.getChatMemory();
            return PromptChatMemoryAdvisor.builder(
                    MessageWindowChatMemory.builder()
                            .maxMessages(chatMemory.getMaxMessages())
                            .build()
            ).build();
        }
    },
    RAG_ANSWER("RagAnswer", "知识库") {
        @Override
        public Advisor createAdvisor(AiClientAdvisorVO advisorVO, VectorStore vectorStore) {
            AiClientAdvisorVO.RagAnswer ragAnswer = advisorVO.getRagAnswer();
            return new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
                    .topK(ragAnswer.getTopK())
                    .filterExpression(ragAnswer.getFilterExpression())
                    .build());
        }
    };

    private String type;
    private String info;

    /**
     * 静态代码块 初始化加载
     */
    private static final Map<String, AiClientAdvisorTypeEnum> CODE_MAP = new HashMap<>();

    static {
        for (AiClientAdvisorTypeEnum enumVO : values()) {
            CODE_MAP.put(enumVO.getType(), enumVO);
        }
    }

    public static AiClientAdvisorTypeEnum getByType(String type) {
        AiClientAdvisorTypeEnum aiClientAdvisorTypeEnum = CODE_MAP.get(type);
        if (aiClientAdvisorTypeEnum == null) {
            throw new RuntimeException("err! advisorType " + type + " not exist!");
        }
        return aiClientAdvisorTypeEnum;
    }

    public abstract Advisor createAdvisor(AiClientAdvisorVO advisorVO, VectorStore vectorStore);
}
