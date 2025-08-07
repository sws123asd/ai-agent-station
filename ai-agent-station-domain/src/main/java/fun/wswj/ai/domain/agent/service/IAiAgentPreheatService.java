package fun.wswj.ai.domain.agent.service;

/**
 * @Author sws
 * @Date 2025/7/24 09:35
 * @description: 智能体预热 - 初始实例化接口
 */
public interface IAiAgentPreheatService {

    /**
     * 服务预热，启动时触达
     */
    void preheat() throws Exception;

    void preheat(String aiClientId) throws Exception;
}
