package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureResourceUsage;

import java.util.List;

/**
 * 农资使用记录Service接口
 * 
 * @author server
 * @date 2025-01-XX
 */
public interface AgricultureResourceUsageService extends IService<AgricultureResourceUsage> {

    /**
     * 查询农资使用记录列表
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 农资使用记录集合
     */
    List<AgricultureResourceUsage> selectAgricultureResourceUsageList(AgricultureResourceUsage agricultureResourceUsage);

    /**
     * 新增农资使用记录（自动扣减库存）
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 结果
     */
    int addAgricultureResourceUsage(AgricultureResourceUsage agricultureResourceUsage);

    /**
     * 修改农资使用记录
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 结果
     */
    int updateAgricultureResourceUsage(AgricultureResourceUsage agricultureResourceUsage);

    /**
     * 删除农资使用记录信息
     * 
     * @param usageId 农资使用记录主键
     * @return 结果
     */
    int deleteAgricultureResourceUsageById(Long usageId);

    /**
     * 根据批次ID查询使用记录
     * 
     * @param batchId 批次ID
     * @return 使用记录列表
     */
    List<AgricultureResourceUsage> selectByBatchId(Long batchId);

    /**
     * 根据任务ID查询使用记录
     * 
     * @param taskId 任务ID
     * @return 使用记录列表
     */
    List<AgricultureResourceUsage> selectByTaskId(Long taskId);

    /**
     * 归还机械类型农资
     * 
     * @param usageId 使用记录ID
     * @return 结果
     */
    int returnAgricultureResource(Long usageId);
}

