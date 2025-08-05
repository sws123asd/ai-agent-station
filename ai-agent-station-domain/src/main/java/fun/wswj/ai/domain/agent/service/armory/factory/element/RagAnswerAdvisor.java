package fun.wswj.ai.domain.agent.service.armory.factory.element;

import com.alibaba.fastjson2.JSON;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionTextParser;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RagAnswerAdvisor implements BaseAdvisor {

    private final VectorStore vectorStore;
    private final SearchRequest searchRequest;
    private final String userTextAdvise;

    public RagAnswerAdvisor(VectorStore vectorStore, SearchRequest searchRequest) {
        this.vectorStore = vectorStore;
        this.searchRequest = searchRequest;
        this.userTextAdvise =  """
                
                Context information is below, surrounded by ---------------------
                
                ---------------------
                {question_answer_context}
                ---------------------
                
                Given the context and provided history information and not prior knowledge,
                reply to the user comment. If the answer is not in the context, inform
                the user that you can't answer the question.
                """;
    }


    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        HashMap<String, Object> context = new HashMap<>(chatClientRequest.context());
        String userContext = chatClientRequest.prompt().getUserMessage().getText();
        // 将用户输入的与定义的顾问文本结合构造新的输入
        String userAdvisorText = userContext + System.lineSeparator() + userTextAdvise;
        String query = new PromptTemplate(userContext).render();
        SearchRequest searchRequestToUse = SearchRequest.from(searchRequest).query(query).filterExpression(this.doGetFilterExpression(context)).build();
        List<Document> documents = vectorStore.similaritySearch(searchRequestToUse);
        // 将查询的知识库文档添加到输入中
        context.put("qa_retrieved_documents", documents);
        String documentContext = documents.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));

        context.put("question_answer_context", documentContext);
        return ChatClientRequest.builder()
                .prompt(Prompt.builder()
                        .messages(new UserMessage(userAdvisorText), new AssistantMessage(JSON.toJSONString(context)))
                        .build())
                .context(context)
                .build();
    }

    private Filter.Expression doGetFilterExpression(Map<String, Object> context) {
        return context.containsKey("qa_filter_expression") && StringUtils.hasText(context.get("qa_filter_expression").toString()) ?
                new FilterExpressionTextParser().parse(context.get("qa_filter_expression").toString()) : this.searchRequest.getFilterExpression();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        ChatResponse.Builder chatResponseBuilder = ChatResponse.builder().from(chatClientResponse.chatResponse());
        chatResponseBuilder.metadata("qa_retrieved_documents", chatClientResponse.context().get("qa_retrieved_documents"));
        ChatResponse chatResponse = chatResponseBuilder.build();
        return ChatClientResponse.builder()
                .chatResponse(chatResponse)
                .context(chatClientResponse.context())
                .build();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(this.before(chatClientRequest, callAdvisorChain));
        return this.after(chatClientResponse, callAdvisorChain);
    }
}
