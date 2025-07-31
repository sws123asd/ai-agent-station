package fun.wswj.ai.infrastructure.dao;

import fun.wswj.ai.infrastructure.dao.po.AiClientAdvisor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * 顾问配置表 DAO 接口
 */
@Mapper
public interface AiClientAdvisorDao {
    
    /**
     * 根据顾问ID列表查询有效的顾问配置
     * @param advisorIds 顾问ID列表
     * @return 顾问配置列表
     */
    List<AiClientAdvisor> queryValidByAdvisorIds(List<String> advisorIds);
}