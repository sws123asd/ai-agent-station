package fun.wswj.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import fun.wswj.ai.domain.agent.adapter.repository.IAgentRepository;
import fun.wswj.ai.domain.agent.model.entity.ArmoryCommandEntity;
import fun.wswj.ai.domain.agent.model.valobj.AiClientMcpToolVO;
import fun.wswj.ai.domain.agent.service.armory.AbstractArmorySupport;
import fun.wswj.ai.domain.agent.service.armory.factory.DefaultArmoryStrategyFactory;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import static fun.wswj.ai.domain.agent.model.valobj.enums.AiAgentElementEnum.AI_CLIENT_TOOL_MCP;

/**
 * @Author sws
 * @Date 2025/7/24 11:46
 * @description: 装配mcp节点
 */
@Slf4j
@Component
public class McpNode extends AbstractArmorySupport {

    private final ModelNode modelNode;

    protected McpNode(ApplicationContext applicationContext, ThreadPoolExecutor threadPoolExecutor, IAgentRepository repository, ModelNode modelNode) {
        super(applicationContext, threadPoolExecutor, repository);
        this.modelNode = modelNode;
    }

    @Override
    protected String doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 构建，tool mcp 节点");
        // 从 dynamicContext 获取配置信息
        List<AiClientMcpToolVO> aiClientToolMcpList = dynamicContext.getValue(dataName());
        if(CollectionUtils.isEmpty(aiClientToolMcpList)){
            log.warn("没有可用的AI客户端工具配置 MCP");
            return router(armoryCommandEntity, dynamicContext);
        }

        for (AiClientMcpToolVO aiClientMcpToolVO : aiClientToolMcpList) {
            // 创建实例对象
            McpSyncClient mcpSyncClient = createMcpSyncClient(aiClientMcpToolVO);
            // 抽象模板自定义注册方法进行注册
            registerBean(beanName(aiClientMcpToolVO.getMcpId()), McpSyncClient.class, mcpSyncClient);
        }
        // 执行router进行下一节点
        return router(armoryCommandEntity, dynamicContext);
    }

    private McpSyncClient createMcpSyncClient(AiClientMcpToolVO aiClientMcpToolVO) {
        String transportType = aiClientMcpToolVO.getTransportType();
        if("stdio".equals(transportType)){
            AiClientMcpToolVO.TransportConfigStdio transportConfigStdio = aiClientMcpToolVO.getTransportConfigStdio();
            Map<String, AiClientMcpToolVO.TransportConfigStdio.Stdio> stdioConfig = transportConfigStdio.getStdio();
            AiClientMcpToolVO.TransportConfigStdio.Stdio stdio = stdioConfig.get(aiClientMcpToolVO.getMcpName());
            ServerParameters parameters = ServerParameters.builder(stdio.getCommand())
                    .args(stdio.getArgs())
                    .env(stdio.getEnv())
                    .build();
            McpSyncClient stdioMcpClient = McpClient.sync(new StdioClientTransport(parameters))
                    .requestTimeout(Duration.ofSeconds(aiClientMcpToolVO.getRequestTimeout()))
                    .build();
            McpSchema.InitializeResult initialize = stdioMcpClient.initialize();
            log.info("Tool Stdio MCP Initialized {}", initialize);
            return stdioMcpClient;
        }else if("sse".equals(transportType)){
            AiClientMcpToolVO.TransportConfigSse transportConfigSse = aiClientMcpToolVO.getTransportConfigSse();
            String originalBaseUri = transportConfigSse.getBaseUri();
            String baseUri;
            String sseEndpoint;
            int sseIndex = originalBaseUri.indexOf("sse");
            if (sseIndex != -1) {
                baseUri = originalBaseUri.substring(0, sseIndex - 1);
                sseEndpoint = originalBaseUri.substring(sseIndex -1 );
            }else {
                baseUri = originalBaseUri;
                sseEndpoint = transportConfigSse.getSseEndpoint();
            }
            sseEndpoint = StringUtils.isBlank(sseEndpoint) ? "/sse" : sseEndpoint;
            HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport
                    .builder(baseUri) // 使用截取后的 baseUri
                    .sseEndpoint(sseEndpoint) // 使用截取或默认的 sseEndpoint
                    .build();
            McpSyncClient sseMcpClient = McpClient.sync(sseClientTransport)
                    .requestTimeout(Duration.ofSeconds(aiClientMcpToolVO.getRequestTimeout()))
                    .build();
            McpSchema.InitializeResult initialize = sseMcpClient.initialize();
            log.info("Tool SSE MCP Initialized {}", initialize);
            return sseMcpClient;
        }
        throw new RuntimeException("err! transportType " + transportType + " not exist!");
    }

    @Override
    protected String beanName(String id) {
        return AI_CLIENT_TOOL_MCP.getBeanName(id);
    }

    @Override
    protected String dataName() {
        return AI_CLIENT_TOOL_MCP.getDataName();
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext, String> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return modelNode;
    }
}
