package fun.wswj.ai.infrastructure.dao;


import fun.wswj.ai.infrastructure.dao.po.AiClientConfig;

import java.util.List;

public interface AiClientConfigDao {

    List<AiClientConfig> queryBySourceTypeAndIds(String sourceType, List<String> ids);
    List<AiClientConfig> queryBySourceTypeAndIdsAndTargetTypes(String sourceType, List<String> ids, List<String> targetTypes);
}




