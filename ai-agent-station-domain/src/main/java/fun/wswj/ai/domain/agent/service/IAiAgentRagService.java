package fun.wswj.ai.domain.agent.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author sws
 * @Date 2025/7/24 09:36
 * @description: 向量知识库服务接口
 */
public interface IAiAgentRagService {

    /**
     * 存储rag文件
     * @param name 向量文件对应名称
     * @param tag 标签
     * @param files 上传文件集合
     */
    void storeRagFile(String name, String tag, List<MultipartFile> files);
}
