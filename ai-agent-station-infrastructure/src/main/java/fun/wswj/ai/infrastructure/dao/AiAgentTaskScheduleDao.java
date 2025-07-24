package fun.wswj.ai.infrastructure.dao;

import org.apache.ibatis.annotations.Mapper;
import fun.wswj.ai.infrastructure.dao.po.AiAgentTaskSchedule;

/**
 * 智能体任务调度配置表 DAO 接口
 */
@Mapper
public interface AiAgentTaskScheduleDao {
    /**
     * 根据主键ID查询任务调度信息
     * @param id 主键ID
     * @return 任务调度信息
     */
    AiAgentTaskSchedule selectById(Long id);
    
    /**
     * 插入任务调度信息
     * @param aiAgentTaskSchedule 任务调度信息
     * @return 影响行数
     */
    int insert(AiAgentTaskSchedule aiAgentTaskSchedule);
    
    /**
     * 更新任务调度信息
     * @param aiAgentTaskSchedule 任务调度信息
     * @return 影响行数
     */
    int update(AiAgentTaskSchedule aiAgentTaskSchedule);
    
    /**
     * 根据主键ID删除任务调度信息
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(Long id);
}