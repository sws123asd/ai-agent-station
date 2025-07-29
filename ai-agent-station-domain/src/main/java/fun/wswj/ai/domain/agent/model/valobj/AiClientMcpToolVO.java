package fun.wswj.ai.domain.agent.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author sws
 * @Date 2025/7/25 11:56
 * @description:
 */
@Data
public class AiClientMcpToolVO {
    private Long id;
    private String mcpName;
    private String transportType;
    private Integer requestTimeout;
    private Integer status;
    private TransportConfigSse transportConfigSse;
    private TransportConfigStdio transportConfigStdio;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransportConfigSse {
        private String baseUri;
        private String sseEndpoint;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransportConfigStdio {

        private Map<String, Stdio> stdio;

        @Data
        public static class Stdio {
            private String command;
            private List<String> args;
            private Map<String, String> env;
        }
    }
}
