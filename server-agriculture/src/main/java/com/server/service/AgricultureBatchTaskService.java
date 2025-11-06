package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureBatchTask;

import java.util.List;

/**
 * 批次任务Service接口
 *
 * @author bxwy
 * @date 2025-05-29
 */
public interface AgricultureBatchTaskService extends IService<AgricultureBatchTask> {

    /**
     * 查询批次任务列表
     *
     * @param agricultureBatchTask 批次任务查询条件
     * @return 批次任务集合
     */
    List<AgricultureBatchTask> selectBatchTaskList(AgricultureBatchTask agricultureBatchTask);

    /**
     * 查询批次任务
     *
     * @param taskId 批次任务主键
     * @return 批次任务
     */
    AgricultureBatchTask selectBatchTaskByTaskId(Long taskId);

    /**
     * 删除批次任务
     *
     * @param taskId 批次任务主键
     * @return 结果
     */
    int deleteAgricultureCropBatchByBatchIds(Long taskId);

    /**
     * 修改批次任务
     *
     * @param agricultureBatchTask 批次任务信息
     * @return 结果
     */
    int updateBatchTask(AgricultureBatchTask agricultureBatchTask);

    /**
     * 新增批次任务
     *
     * @param agricultureBatchTask 批次任务信息
     * @return 结果
     */
    int insertBatchTask(AgricultureBatchTask agricultureBatchTask);
}
