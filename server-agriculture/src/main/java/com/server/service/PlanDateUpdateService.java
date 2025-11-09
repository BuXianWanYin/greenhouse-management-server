package com.server.service;

/**
 * 计划日期更新服务接口
 * 用于处理计划实际日期的自动计算和联动更新
 */
public interface PlanDateUpdateService {
    
    /**
     * 根据批次任务更新相关计划的实际日期
     * 当批次任务的 actual_start 或 actual_finish 变更时调用
     * 
     * @param batchId 批次ID
     */
    void updatePlanDatesByBatchTask(Long batchId);
    
    /**
     * 更新季度计划的实际日期
     * 根据关联批次的所有批次任务计算
     * 
     * @param planId 季度计划ID
     */
    void updateSeasonalPlanActualDates(Long planId);
    
    /**
     * 更新年度计划的实际日期
     * 根据其下所有季度计划的实际日期计算
     * 
     * @param planId 年度计划ID
     */
    void updateAnnualPlanActualDates(Long planId);
    
    /**
     * 更新轮作计划明细的实际日期
     * 根据关联批次的所有批次任务计算
     * 
     * @param detailId 轮作计划明细ID
     */
    void updatePlanDetailActualDates(Long detailId);
    
    /**
     * 更新轮作计划的实际日期
     * 根据其下所有轮作计划明细的实际日期计算
     * 
     * @param planId 轮作计划ID
     */
    void updateRotationPlanActualDates(Long planId);
}

