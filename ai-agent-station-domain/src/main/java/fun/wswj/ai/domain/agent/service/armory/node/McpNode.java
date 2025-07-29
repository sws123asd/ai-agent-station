package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.alibaba.fastjson.JSON;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.AiAgentEngineStarterEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientMcpToolVO;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import io.modelcontextprotocol.client.McpSyncClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author sws
 * @Date 2025/7/24 11:46
 * @description: 装配mcp节点
 */
@Slf4j
@Component
public class McpNode extends AbstractArmorySupport {
    private final AdvisorNode advisorNode;

    protected McpNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository, AdvisorNode advisorNode) {
        super(applicationContext, threadPoolExecutor, repository);
        this.advisorNode = advisorNode;
    }

    @Override
    protected String doApply(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，tool mcp 节点 {}", JSON.toJSONString(aiAgentEngineStarterEntity));
        // 从 dynamicContext 获取配置信息
        List<AiClientMcpToolVO> aiClientToolMcpList = dynamicContext.getValue("aiClientToolMcpList");
        if(CollectionUtils.isEmpty(aiClientToolMcpList)){
            log.warn("没有可用的AI客户端工具配置 MCP");
            return router(aiAgentEngineStarterEntity, dynamicContext);
        }

        for (AiClientMcpToolVO aiClientMcpToolVO : aiClientToolMcpList) {
            // 创建实例对象
            McpSyncClient mcpSyncClient = createMcpSyncClient(aiClientMcpToolVO);
            // 抽象模板自定义注册方法进行注册
            registerBean(beanName(aiClientMcpToolVO.getId()), McpSyncClient.class, mcpSyncClient);
        }
        // 执行router进行下一节点
        return router(aiAgentEngineStarterEntity, dynamicContext);
    }

    private McpSyncClient createMcpSyncClient(AiClientMcpToolVO aiClientMcpToolVO) {
        String transportType = aiClientMcpToolVO.getTransportType();
        return null;
    }

    @Override
    protected String beanName(Long id) {
        return "AiClientToolMcp_" + id;
    }

    @Override
    public StrategyHandler<AiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(AiAgentEngineStarterEntity aiAgentEngineStarterEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return advisorNode;
    }
}
