package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.server.domain.AgricultureBatchTask;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgriculturePlanDetail;
import com.server.domain.AgriculturePlantingPlan;
import com.server.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * 计划日期更新服务实现类
 * 实现计划实际日期的自动计算和联动更新
 */
@Slf4j
@Service
public class PlanDateUpdateServiceImpl implements PlanDateUpdateService {

    @Autowired
    private AgricultureBatchTaskService batchTaskService;
    
    @Autowired
    private AgricultureCropBatchService cropBatchService;
    
    @Autowired
    private AgriculturePlantingPlanService plantingPlanService;
    
    @Autowired
    private AgriculturePlanDetailService planDetailService;

    /**
     * 根据批次任务更新相关计划的实际日期
     * 当批次任务的 actual_start 或 actual_finish 变更时调用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlanDatesByBatchTask(Long batchId) {
        if (batchId == null) {
            return;
        }

        log.info("开始更新批次 {} 相关的计划实际日期", batchId);

        // 查询批次信息
        AgricultureCropBatch batch = cropBatchService.getById(batchId);
        if (batch == null) {
            log.warn("批次 {} 不存在", batchId);
            return;
        }

        // 更新季度计划的实际日期
        updateSeasonalPlanIfNeeded(batch);
        
        // 更新轮作计划明细的实际日期
        updateRotationPlanDetailIfNeeded(batch);

        log.info("完成批次 {} 相关的计划实际日期更新", batchId);
    }

    /**
     * 更新批次状态和关联计划状态
     * 当批次任务开始后，更新批次状态为"进行中"，并更新关联计划状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchAndPlanStatus(Long batchId) {
        if (batchId == null) {
            return;
        }

        log.info("开始更新批次 {} 的状态和关联计划状态", batchId);

        // 查询批次信息
        AgricultureCropBatch batch = cropBatchService.getById(batchId);
        if (batch == null) {
            log.warn("批次 {} 不存在", batchId);
            return;
        }

        // 查询该批次的所有任务（使用LambdaQueryWrapper确保查询到最新数据）
        LambdaQueryWrapper<AgricultureBatchTask> taskQuery = new LambdaQueryWrapper<>();
        taskQuery.eq(AgricultureBatchTask::getBatchId, batchId)
                .orderByAsc(AgricultureBatchTask::getPlanStart)
                .orderByAsc(AgricultureBatchTask::getTaskId);
        List<AgricultureBatchTask> tasks = batchTaskService.list(taskQuery);
        if (tasks == null || tasks.isEmpty()) {
            log.info("批次 {} 没有任务", batchId);
            return;
        }

        log.info("批次 {} 共有 {} 个任务", batchId, tasks.size());
        
        // 检查任务状态
        boolean hasTaskStarted = false;
        boolean allTasksCompleted = true;
        int totalTaskCount = tasks.size();
        int completedTaskCount = 0;
        
        for (AgricultureBatchTask task : tasks) {
            log.info("检查任务 {} - status: {}, actual_start: {}, actual_finish: {}", 
                    task.getTaskId(), task.getStatus(), task.getActualStart(), task.getActualFinish());
            
            // 检查是否有任务已开始
            if (task.getActualStart() != null) {
                hasTaskStarted = true;
            }
            
            // 检查是否所有任务都已完成（status="3"）
            if (!"3".equals(task.getStatus())) {
                allTasksCompleted = false;
            } else {
                completedTaskCount++;
            }
        }

        String currentStatus = batch.getStatus();
        boolean statusChanged = false;

        // 如果所有任务都已完成，更新批次状态为"已完成"（2）
        if (allTasksCompleted && totalTaskCount > 0) {
            if (!"2".equals(currentStatus)) {
                log.info("批次 {} 下所有任务都已完成（{}/{}），当前状态: {}，更新批次状态为已完成（2）", 
                        batchId, completedTaskCount, totalTaskCount, currentStatus);
                batch.setStatus("2");
                boolean updateResult = cropBatchService.updateById(batch);
                log.info("批次 {} 状态更新结果: {}", batchId, updateResult);
                statusChanged = true;
            } else {
                log.info("批次 {} 状态已经是已完成（2），无需更新", batchId);
            }
        }
        // 如果有任务已开始但未全部完成，更新批次状态为"进行中"（1）
        else if (hasTaskStarted) {
            if (!"1".equals(currentStatus) && !"2".equals(currentStatus)) {
                log.info("批次 {} 有任务已开始，当前状态: {}，更新批次状态为进行中（1）", batchId, currentStatus);
                batch.setStatus("1");
                boolean updateResult = cropBatchService.updateById(batch);
                log.info("批次 {} 状态更新结果: {}", batchId, updateResult);
                statusChanged = true;
            } else {
                log.info("批次 {} 状态已经是进行中（1）或已完成（2），无需更新", batchId);
            }
        } else {
            log.info("批次 {} 没有任务已开始，当前状态: {}", batchId, currentStatus);
        }

        // 如果批次状态发生变化，更新关联计划的状态
        if (statusChanged) {
            updateRelatedPlanStatus(batch);
        }

        log.info("完成批次 {} 的状态和关联计划状态更新，最终状态: {}", batchId, batch.getStatus());
    }

    /**
     * 更新关联计划的状态
     * 根据批次关联的计划类型，更新对应的计划状态
     */
    private void updateRelatedPlanStatus(AgricultureCropBatch batch) {
        // 如果批次关联了季度计划，更新季度计划状态
        if (batch.getPlanId() != null) {
            AgriculturePlantingPlan plan = plantingPlanService.getById(batch.getPlanId());
            if (plan != null) {
                String planType = plan.getPlanType();
                
                // 更新季度计划状态
                if ("seasonal".equals(planType)) {
                    updateSeasonalPlanStatus(batch.getPlanId(), batch.getBatchId());
                }
                
                // 更新轮作计划状态
                if ("rotation".equals(planType)) {
                    updateRotationPlanStatusByBatches(batch.getPlanId());
                }
            }
        }
        
        // 如果批次关联了轮作计划明细，更新轮作计划状态
        if (batch.getDetailId() != null) {
            AgriculturePlanDetail detail = planDetailService.getById(batch.getDetailId());
            if (detail != null && detail.getPlanId() != null) {
                updateRotationPlanStatusByDetails(detail.getPlanId());
            }
        }
    }

    /**
     * 更新季度计划状态
     * 当所有批次都已完成时，更新季度计划状态为已完成
     * 当有批次进行中时，更新季度计划状态为执行中
     */
    private void updateSeasonalPlanStatus(Long planId, Long batchId) {
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !"seasonal".equals(plan.getPlanType())) {
            return;
        }

        // 查询该季度计划关联的所有批次
        LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
        batchQuery.eq(AgricultureCropBatch::getPlanId, planId)
                .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                        .or()
                        .isNull(AgricultureCropBatch::getDelFlag));
        List<AgricultureCropBatch> batches = cropBatchService.list(batchQuery);

        if (batches == null || batches.isEmpty()) {
            return;
        }

        // 检查批次状态
        boolean hasExecutingBatch = false;
        boolean allBatchesCompleted = true;
        int totalBatchCount = batches.size();
        int completedBatchCount = 0;

        for (AgricultureCropBatch batch : batches) {
            String batchStatus = batch.getStatus();
            if ("1".equals(batchStatus)) {
                hasExecutingBatch = true;
                allBatchesCompleted = false;
            } else if (!"2".equals(batchStatus)) {
                allBatchesCompleted = false;
            } else {
                completedBatchCount++;
            }
        }

        String currentPlanStatus = plan.getPlanStatus();
        boolean needUpdate = false;

        // 如果所有批次都已完成，更新季度计划状态为"已完成"（2）
        if (allBatchesCompleted && totalBatchCount > 0) {
            if (!"2".equals(currentPlanStatus)) {
                log.info("季度计划 {} 下所有批次都已完成（{}/{}），当前状态: {}，更新季度计划状态为已完成（2）", 
                        planId, completedBatchCount, totalBatchCount, currentPlanStatus);
                plan.setPlanStatus("2");
                needUpdate = true;
            }
        }
        // 如果有批次进行中，更新季度计划状态为"执行中"（1）
        else if (hasExecutingBatch) {
            if (!"1".equals(currentPlanStatus) && !"2".equals(currentPlanStatus)) {
                log.info("批次 {} 关联季度计划 {}，更新季度计划状态为执行中", batchId, planId);
                plan.setPlanStatus("1");
                needUpdate = true;
            }
        }

        if (needUpdate) {
            plantingPlanService.updateById(plan);
            
            // 如果季度计划有父年度计划，更新年度计划状态
            if (plan.getParentPlanId() != null) {
                updateAnnualPlanStatusBySeasonalPlans(plan.getParentPlanId());
            }
        }
    }

    /**
     * 更新年度计划状态（根据季度计划状态）
     * 当所有季度计划都已完成时，更新年度计划状态为已完成
     * 当有季度计划进行中时，更新年度计划状态为执行中
     */
    private void updateAnnualPlanStatusBySeasonalPlans(Long parentPlanId) {
        if (parentPlanId == null) {
            return;
        }

        AgriculturePlantingPlan annualPlan = plantingPlanService.getById(parentPlanId);
        if (annualPlan == null || !"annual".equals(annualPlan.getPlanType())) {
            return;
        }

        // 查询该年度计划下的所有季度计划（未删除的）
        LambdaQueryWrapper<AgriculturePlantingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgriculturePlantingPlan::getParentPlanId, parentPlanId)
                .eq(AgriculturePlantingPlan::getPlanType, "seasonal")
                .and(wrapper -> wrapper.eq(AgriculturePlantingPlan::getDelFlag, "0")
                        .or()
                        .isNull(AgriculturePlantingPlan::getDelFlag));
        List<AgriculturePlantingPlan> seasonalPlans = plantingPlanService.list(queryWrapper);

        if (seasonalPlans == null || seasonalPlans.isEmpty()) {
            return;
        }

        // 检查季度计划状态
        boolean hasExecutingPlan = false;
        boolean allPlansCompleted = true;
        int totalPlanCount = seasonalPlans.size();
        int completedPlanCount = 0;

        for (AgriculturePlantingPlan seasonalPlan : seasonalPlans) {
            String planStatus = seasonalPlan.getPlanStatus();
            if ("1".equals(planStatus)) {
                hasExecutingPlan = true;
                allPlansCompleted = false;
            } else if (!"2".equals(planStatus)) {
                allPlansCompleted = false;
            } else {
                completedPlanCount++;
            }
        }

        String currentStatus = annualPlan.getPlanStatus();
        boolean needUpdate = false;

        // 如果所有季度计划都已完成，更新年度计划状态为"已完成"（2）
        if (allPlansCompleted && totalPlanCount > 0) {
            if (!"2".equals(currentStatus)) {
                log.info("年度计划 {} 下所有季度计划都已完成（{}/{}），当前状态: {}，更新年度计划状态为已完成（2）", 
                        parentPlanId, completedPlanCount, totalPlanCount, currentStatus);
                annualPlan.setPlanStatus("2");
                needUpdate = true;
            }
        }
        // 如果有季度计划为"执行中"，更新年度计划状态为"执行中"（1）
        else if (hasExecutingPlan) {
            if (!"1".equals(currentStatus) && !"2".equals(currentStatus)) {
                log.info("年度计划 {} 下有季度计划执行中，更新年度计划状态为执行中", parentPlanId);
                annualPlan.setPlanStatus("1");
                needUpdate = true;
            }
        }

        if (needUpdate) {
            plantingPlanService.updateById(annualPlan);
        }
    }

    /**
     * 更新轮作计划状态（根据批次状态）
     * 当所有批次都已完成时，更新轮作计划状态为已完成
     */
    private void updateRotationPlanStatusByBatches(Long planId) {
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !"rotation".equals(plan.getPlanType())) {
            return;
        }

        // 查询该轮作计划关联的所有批次
        LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
        batchQuery.eq(AgricultureCropBatch::getPlanId, planId)
                .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                        .or()
                        .isNull(AgricultureCropBatch::getDelFlag));
        List<AgricultureCropBatch> batches = cropBatchService.list(batchQuery);

        if (batches == null || batches.isEmpty()) {
            return;
        }

        // 检查批次状态
        boolean hasExecutingBatch = false;
        boolean allBatchesCompleted = true;
        int totalBatchCount = batches.size();
        int completedBatchCount = 0;

        for (AgricultureCropBatch batch : batches) {
            String batchStatus = batch.getStatus();
            if ("1".equals(batchStatus)) {
                hasExecutingBatch = true;
                allBatchesCompleted = false;
            } else if (!"2".equals(batchStatus)) {
                allBatchesCompleted = false;
            } else {
                completedBatchCount++;
            }
        }

        String currentStatus = plan.getPlanStatus();
        boolean needUpdate = false;

        // 如果所有批次都已完成，更新轮作计划状态为"已完成"（2）
        if (allBatchesCompleted && totalBatchCount > 0) {
            if (!"2".equals(currentStatus)) {
                log.info("轮作计划 {} 下所有批次都已完成（{}/{}），当前状态: {}，更新轮作计划状态为已完成（2）", 
                        planId, completedBatchCount, totalBatchCount, currentStatus);
                plan.setPlanStatus("2");
                needUpdate = true;
            }
        }
        // 如果有批次进行中，更新轮作计划状态为"执行中"（1）
        else if (hasExecutingBatch) {
            if (!"1".equals(currentStatus) && !"2".equals(currentStatus)) {
                log.info("轮作计划 {} 下有批次执行中，更新轮作计划状态为执行中", planId);
                plan.setPlanStatus("1");
                needUpdate = true;
            }
        }

        if (needUpdate) {
            plantingPlanService.updateById(plan);
        }
    }

    /**
     * 更新轮作计划状态（根据明细的实际结束日期）
     * 当所有明细都有实际结束日期时，更新轮作计划状态为已完成
     */
    private void updateRotationPlanStatusByDetails(Long planId) {
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !"rotation".equals(plan.getPlanType())) {
            return;
        }

        // 查询该轮作计划下的所有明细
        LambdaQueryWrapper<AgriculturePlanDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(AgriculturePlanDetail::getPlanId, planId);
        List<AgriculturePlanDetail> details = planDetailService.list(detailQuery);

        if (details == null || details.isEmpty()) {
            return;
        }

        // 检查是否所有明细都有实际结束日期
        boolean allDetailsFinished = true;
        int totalDetailCount = details.size();
        int finishedDetailCount = 0;

        for (AgriculturePlanDetail detail : details) {
            if (detail.getActualEndDate() != null) {
                finishedDetailCount++;
            } else {
                allDetailsFinished = false;
            }
        }

        String currentStatus = plan.getPlanStatus();
        boolean needUpdate = false;

        // 如果所有明细都已完成（都有实际结束日期），更新轮作计划状态为"已完成"（2）
        if (allDetailsFinished && totalDetailCount > 0) {
            if (!"2".equals(currentStatus)) {
                log.info("轮作计划 {} 下所有明细都已完成（{}/{}），当前状态: {}，更新轮作计划状态为已完成（2）", 
                        planId, finishedDetailCount, totalDetailCount, currentStatus);
                plan.setPlanStatus("2");
                needUpdate = true;
            }
        }
        // 如果有明细已开始但未全部完成，更新轮作计划状态为"执行中"（1）
        else if (finishedDetailCount > 0 || details.stream().anyMatch(d -> d.getActualStartDate() != null)) {
            if (!"1".equals(currentStatus) && !"2".equals(currentStatus)) {
                log.info("轮作计划 {} 下有明细执行中，更新轮作计划状态为执行中", planId);
                plan.setPlanStatus("1");
                needUpdate = true;
            }
        }

        if (needUpdate) {
            plantingPlanService.updateById(plan);
        }
    }

    /**
     * 更新季度计划的实际日期
     * 根据关联批次的所有批次任务计算
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSeasonalPlanActualDates(Long planId) {
        if (planId == null) {
            return;
        }

        log.info("开始更新季度计划 {} 的实际日期", planId);

        // 查询并验证季度计划
        AgriculturePlantingPlan plan = getAndValidatePlan(planId, "seasonal", "季度计划");
        if (plan == null) {
            return;
        }

        // 查询该季度计划关联的所有批次
        LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
        batchQuery.eq(AgricultureCropBatch::getPlanId, planId)
                .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                        .or()
                        .isNull(AgricultureCropBatch::getDelFlag));
        List<AgricultureCropBatch> batches = cropBatchService.list(batchQuery);

        if (batches == null || batches.isEmpty()) {
            log.info("季度计划 {} 没有关联批次，清空实际日期", planId);
            clearPlanActualDates(planId);
            return;
        }

        // 收集所有批次的任务实际日期
        LocalDate minStartDate = null;
        LocalDate maxEndDate = null;
        int totalTaskCount = 0;
        int finishedTaskCount = 0;

        for (AgricultureCropBatch batch : batches) {
            // 查询该批次的所有任务
            List<AgricultureBatchTask> tasks = batchTaskService.selectBatchTaskListByBatchId(batch.getBatchId());
            
            for (AgricultureBatchTask task : tasks) {
                totalTaskCount++;
                minStartDate = updateMinDateFromDate(task.getActualStart(), minStartDate);
                // 判断任务是否完成：任务状态为"3"（已完成）且有实际结束日期
                if ("3".equals(task.getStatus()) && task.getActualFinish() != null) {
                    finishedTaskCount++;
                    maxEndDate = updateMaxDateFromDate(task.getActualFinish(), maxEndDate);
                    log.debug("批次 {} 的任务 {} 已完成，status: {}, actual_finish: {}", 
                            batch.getBatchId(), task.getTaskId(), task.getStatus(), task.getActualFinish());
                } else {
                    log.debug("批次 {} 的任务 {} 未完成，status: {}, actual_finish: {}", 
                            batch.getBatchId(), task.getTaskId(), task.getStatus(), task.getActualFinish());
                }
            }
        }

        log.info("季度计划 {} 任务统计: 总数={}, 已完成={}, 最早开始日期={}, 最晚结束日期={}", 
                planId, totalTaskCount, finishedTaskCount, minStartDate, maxEndDate);

        // 更新季度计划的实际开始日期（只要有任务开始就更新）
        // 更新季度计划的实际结束日期（只有当所有任务都完成后才更新）
        LocalDate actualEndDate = null;
        if (totalTaskCount > 0 && finishedTaskCount == totalTaskCount && maxEndDate != null) {
            actualEndDate = maxEndDate;
            log.info("季度计划 {} 下所有批次的所有任务都已完成（{}/{}），更新结束日期: {}", 
                    planId, finishedTaskCount, totalTaskCount, actualEndDate);
        } else {
            log.info("季度计划 {} 任务完成情况: {}/{}，暂不更新结束日期。maxEndDate: {}", 
                    planId, finishedTaskCount, totalTaskCount, maxEndDate);
        }
        
        // 只有当开始日期或结束日期不为null时才更新（结束日期只有在所有任务完成时才不为null）
        if (minStartDate != null || actualEndDate != null) {
            updatePlanActualDates(planId, minStartDate, actualEndDate);
        } else {
            log.info("季度计划 {} 没有可更新的日期信息", planId);
        }

        // 如果季度计划有父年度计划，更新年度计划的实际日期
        if (plan.getParentPlanId() != null) {
            log.info("季度计划 {} 有父年度计划 {}，更新年度计划实际日期", planId, plan.getParentPlanId());
            updateAnnualPlanActualDates(plan.getParentPlanId());
        }

        log.info("完成季度计划 {} 的实际日期更新: {} - {}", planId, minStartDate, maxEndDate);
    }

    /**
     * 更新年度计划的实际日期
     * 根据其下所有季度计划的实际日期计算
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAnnualPlanActualDates(Long planId) {
        if (planId == null) {
            return;
        }

        log.info("开始更新年度计划 {} 的实际日期", planId);

        // 查询并验证年度计划
        AgriculturePlantingPlan plan = getAndValidatePlan(planId, "annual", "年度计划");
        if (plan == null) {
            return;
        }

        // 查询该年度计划下的所有季度计划（未删除的）
        LambdaQueryWrapper<AgriculturePlantingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgriculturePlantingPlan::getParentPlanId, planId)
                .eq(AgriculturePlantingPlan::getPlanType, "seasonal")
                .and(wrapper -> wrapper.eq(AgriculturePlantingPlan::getDelFlag, "0")
                        .or()
                        .isNull(AgriculturePlantingPlan::getDelFlag));
        List<AgriculturePlantingPlan> seasonalPlans = plantingPlanService.list(queryWrapper);

        if (seasonalPlans == null || seasonalPlans.isEmpty()) {
            log.info("年度计划 {} 没有季度计划，清空实际日期", planId);
            clearPlanActualDates(planId);
            return;
        }

        // 收集所有季度计划的实际日期
        LocalDate minStartDate = null;
        LocalDate maxEndDate = null;
        int totalSeasonalPlanCount = seasonalPlans.size();
        int finishedSeasonalPlanCount = 0;

        log.info("年度计划 {} 开始检查季度计划完成情况，共有 {} 个季度计划", planId, totalSeasonalPlanCount);
        
        for (AgriculturePlantingPlan seasonalPlan : seasonalPlans) {
            minStartDate = updateMinLocalDate(seasonalPlan.getActualStartDate(), minStartDate);
            LocalDate planEndDate = seasonalPlan.getActualEndDate();
            log.info("季度计划 {} (planId: {}) - 实际开始日期: {}, 实际结束日期: {}", 
                    seasonalPlan.getPlanName(), seasonalPlan.getPlanId(), 
                    seasonalPlan.getActualStartDate(), planEndDate);
            
            if (planEndDate != null) {
                finishedSeasonalPlanCount++;
                maxEndDate = updateMaxLocalDate(planEndDate, maxEndDate);
                log.info("季度计划 {} 已完成，当前最晚结束日期: {}", seasonalPlan.getPlanId(), maxEndDate);
            } else {
                log.info("季度计划 {} 未完成（actualEndDate 为 null）", seasonalPlan.getPlanId());
            }
        }

        log.info("年度计划 {} 季度计划完成统计: {}/{} 已完成，最晚结束日期: {}", 
                planId, finishedSeasonalPlanCount, totalSeasonalPlanCount, maxEndDate);

        // 更新年度计划的实际开始日期（只要有季度计划开始就更新）
        // 更新年度计划的实际结束日期（只有当所有季度计划都结束后才更新，并选择最晚的结束日期）
        LocalDate actualEndDate = null;
        if (totalSeasonalPlanCount > 0 && finishedSeasonalPlanCount == totalSeasonalPlanCount && maxEndDate != null) {
            actualEndDate = maxEndDate;
            log.info("✓ 年度计划 {} 下所有季度计划都已完成（{}/{}），更新结束日期为最晚日期: {}", 
                    planId, finishedSeasonalPlanCount, totalSeasonalPlanCount, actualEndDate);
        } else {
            log.info("✗ 年度计划 {} 季度计划未全部完成（{}/{}），暂不更新结束日期。最晚结束日期: {}", 
                    planId, finishedSeasonalPlanCount, totalSeasonalPlanCount, maxEndDate);
        }
        
        // 只有当开始日期或结束日期不为null时才更新（结束日期只有在所有季度计划完成时才不为null）
        if (minStartDate != null || actualEndDate != null) {
            updatePlanActualDates(planId, minStartDate, actualEndDate);
        } else {
            log.info("年度计划 {} 没有可更新的日期信息", planId);
        }

        log.info("完成年度计划 {} 的实际日期更新: {} - {}", planId, minStartDate, maxEndDate);
    }

    /**
     * 更新轮作计划明细的实际日期
     * 根据关联批次的所有批次任务计算
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlanDetailActualDates(Long detailId) {
        if (detailId == null) {
            return;
        }

        log.info("开始更新轮作计划明细 {} 的实际日期", detailId);

        // 查询轮作计划明细
        AgriculturePlanDetail detail = planDetailService.getById(detailId);
        if (detail == null) {
            log.warn("轮作计划明细 {} 不存在", detailId);
            return;
        }

        // 查询关联的批次（通过 detail_id 直接关联）
        LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
        batchQuery.eq(AgricultureCropBatch::getDetailId, detailId)
                .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                        .or()
                        .isNull(AgricultureCropBatch::getDelFlag));
        List<AgricultureCropBatch> batches = cropBatchService.list(batchQuery);

        if (batches == null || batches.isEmpty()) {
            log.info("轮作计划明细 {} 没有关联批次，清空实际日期", detailId);
            clearPlanDetailActualDates(detailId);
            return;
        }

        // 收集所有批次的任务实际日期
        LocalDate minStartDate = null;
        LocalDate maxEndDate = null;
        int totalTaskCount = 0;
        int finishedTaskCount = 0;

        for (AgricultureCropBatch batch : batches) {
            // 查询该批次的所有任务
            List<AgricultureBatchTask> tasks = batchTaskService.selectBatchTaskListByBatchId(batch.getBatchId());
            
            for (AgricultureBatchTask task : tasks) {
                totalTaskCount++;
                minStartDate = updateMinDateFromDate(task.getActualStart(), minStartDate);
                // 判断任务是否完成：任务状态为"3"（已完成）且有实际结束日期
                if ("3".equals(task.getStatus()) && task.getActualFinish() != null) {
                    finishedTaskCount++;
                    maxEndDate = updateMaxDateFromDate(task.getActualFinish(), maxEndDate);
                    log.debug("轮作计划明细 {} 的批次 {} 的任务 {} 已完成，status: {}, actual_finish: {}", 
                            detailId, batch.getBatchId(), task.getTaskId(), task.getStatus(), task.getActualFinish());
                } else {
                    log.debug("轮作计划明细 {} 的批次 {} 的任务 {} 未完成，status: {}, actual_finish: {}", 
                            detailId, batch.getBatchId(), task.getTaskId(), task.getStatus(), task.getActualFinish());
                }
            }
        }
        
        log.info("轮作计划明细 {} 任务统计: 总数={}, 已完成={}, 最早开始日期={}, 最晚结束日期={}", 
                detailId, totalTaskCount, finishedTaskCount, minStartDate, maxEndDate);

        // 1. 同步实际开始日期：如果该日期早于轮作计划明细的 actual_start_date（或明细的 actual_start_date 为 null），更新明细的 actual_start_date
        updateDetailStartDateIfNeeded(detail, minStartDate, detailId);
        
        // 2. 同步实际结束日期：如果所有任务都已完成，更新明细的 actual_end_date
        updateDetailEndDateIfAllTasksFinished(detail, totalTaskCount, finishedTaskCount, maxEndDate, detailId);

        // 更新轮作计划明细的实际日期
        planDetailService.updateById(detail);

        // 3. 检查轮作计划是否应该结束：如果所有明细都已完成，更新轮作计划的实际结束日期和状态
        if (detail.getPlanId() != null) {
            checkAndUpdateRotationPlanEndDate(detail.getPlanId());
            // 更新轮作计划状态
            updateRotationPlanStatusByDetails(detail.getPlanId());
        }

        log.info("完成轮作计划明细 {} 的实际日期更新: {} - {}", detailId, detail.getActualStartDate(), detail.getActualEndDate());
    }

    /**
     * 检查并更新轮作计划的实际结束日期
     * 如果该轮作计划下的所有明细都已完成，更新轮作计划的实际结束日期
     *
     * @param planId 轮作计划ID
     */
    private void checkAndUpdateRotationPlanEndDate(Long planId) {
        if (planId == null) {
            return;
        }

        log.info("检查轮作计划 {} 是否应该结束", planId);

        // 查询轮作计划
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !"rotation".equals(plan.getPlanType())) {
            log.warn("计划 {} 不存在或不是轮作计划", planId);
            return;
        }

        // 查询该轮作计划下的所有明细
        LambdaQueryWrapper<AgriculturePlanDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(AgriculturePlanDetail::getPlanId, planId);
        List<AgriculturePlanDetail> details = planDetailService.list(detailQuery);

        if (details == null || details.isEmpty()) {
            log.info("轮作计划 {} 没有明细", planId);
            return;
        }

        // 检查是否所有明细都已完成，并获取最晚的结束日期
        LocalDate maxEndDate = calculateMaxEndDateIfAllFinished(details);
        
        // 如果所有明细都已完成，更新轮作计划的实际结束日期
        if (maxEndDate != null) {
            log.info("轮作计划 {} 下的所有明细都已完成，更新轮作计划的实际结束日期: {} -> {}", 
                    planId, plan.getActualEndDate(), maxEndDate);
            plan.setActualEndDate(maxEndDate);
            plantingPlanService.updateById(plan);
        }
    }

    /**
     * 更新轮作计划的实际日期
     * 根据其下所有轮作计划明细的实际日期计算
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRotationPlanActualDates(Long planId) {
        if (planId == null) {
            return;
        }

        log.info("开始更新轮作计划 {} 的实际日期", planId);

        // 查询并验证轮作计划
        AgriculturePlantingPlan plan = getAndValidatePlan(planId, "rotation", "轮作计划");
        if (plan == null) {
            return;
        }

        // 查询该轮作计划下的所有明细
        LambdaQueryWrapper<AgriculturePlanDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(AgriculturePlanDetail::getPlanId, planId);
        List<AgriculturePlanDetail> details = planDetailService.list(detailQuery);

        if (details == null || details.isEmpty()) {
            log.info("轮作计划 {} 没有明细，清空实际日期", planId);
            clearPlanActualDates(planId);
            return;
        }

        // 收集所有明细的实际日期
        LocalDate minStartDate = null;
        LocalDate maxEndDate = null;
        boolean allDetailsFinished = true;

        for (AgriculturePlanDetail detail : details) {
            minStartDate = updateMinLocalDate(detail.getActualStartDate(), minStartDate);
            
            if (detail.getActualEndDate() != null) {
                maxEndDate = updateMaxLocalDate(detail.getActualEndDate(), maxEndDate);
            } else {
                // 如果某个明细没有实际结束日期，说明轮作计划还未完成
                allDetailsFinished = false;
            }
        }

        // 更新轮作计划的实际开始日期（取最早）
        if (minStartDate != null) {
            plan.setActualStartDate(minStartDate);
        }

        // 更新轮作计划的实际结束日期（只有当所有明细都完成时才更新）
        if (allDetailsFinished && maxEndDate != null) {
            plan.setActualEndDate(maxEndDate);
        }

        // 更新轮作计划的实际日期
        plantingPlanService.updateById(plan);

        log.info("完成轮作计划 {} 的实际日期更新: {} - {}", planId, minStartDate, maxEndDate);
    }

    /**
     * 更新计划的实际日期
     * @param planId 计划ID
     * @param actualStartDate 实际开始日期（如果为null则不更新）
     * @param actualEndDate 实际结束日期（如果为null则不更新，只有当所有子项都完成时才传入非null值）
     */
    private void updatePlanActualDates(Long planId, LocalDate actualStartDate, LocalDate actualEndDate) {
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null) {
            return;
        }
        
        boolean needUpdate = false;
        
        // 只有当实际开始日期不为null时才更新，并且如果已有值，取更早的日期
        if (actualStartDate != null) {
            if (plan.getActualStartDate() == null || actualStartDate.isBefore(plan.getActualStartDate())) {
                plan.setActualStartDate(actualStartDate);
                needUpdate = true;
            }
        }
        
        // 只有当实际结束日期不为null时才更新（传入非null值表示所有子项都已完成）
        // 如果已有结束日期，只有当新的结束日期更晚时才更新（理论上不应该发生，但为了安全）
        if (actualEndDate != null) {
            LocalDate currentEndDate = plan.getActualEndDate();
            if (currentEndDate == null || actualEndDate.isAfter(currentEndDate)) {
                log.info("更新计划 {} 的实际结束日期: {} -> {}", planId, currentEndDate, actualEndDate);
                plan.setActualEndDate(actualEndDate);
                needUpdate = true;
            } else {
                log.info("计划 {} 已有结束日期 {}，新日期 {} 不更晚，不更新", planId, currentEndDate, actualEndDate);
            }
        } else {
            // 注意：如果 actualEndDate 为 null，不更新结束日期，保持原有值
            log.debug("计划 {} 的 actualEndDate 为 null，不更新结束日期，保持原有值: {}", planId, plan.getActualEndDate());
        }
        
        if (needUpdate) {
            plantingPlanService.updateById(plan);
        }
    }

    /**
     * 清空计划的实际日期
     */
    private void clearPlanActualDates(Long planId) {
        updatePlanActualDates(planId, null, null);
    }

    /**
     * 更新轮作计划明细的实际日期
     */
    private void updatePlanDetailActualDates(Long detailId, LocalDate actualStartDate, LocalDate actualEndDate) {
        LambdaUpdateWrapper<AgriculturePlanDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgriculturePlanDetail::getDetailId, detailId)
                .set(AgriculturePlanDetail::getActualStartDate, actualStartDate)
                .set(AgriculturePlanDetail::getActualEndDate, actualEndDate);
        planDetailService.update(updateWrapper);
    }

    /**
     * 清空轮作计划明细的实际日期
     */
    private void clearPlanDetailActualDates(Long detailId) {
        updatePlanDetailActualDates(detailId, null, null);
    }

    /**
     * 更新季度计划的实际日期（如果需要）
     */
    private void updateSeasonalPlanIfNeeded(AgricultureCropBatch batch) {
        if (batch.getPlanId() == null) {
            return;
        }
        
        AgriculturePlantingPlan plan = plantingPlanService.getById(batch.getPlanId());
        if (plan != null && "seasonal".equals(plan.getPlanType())) {
            log.info("批次 {} 关联季度计划 {}，更新季度计划实际日期", batch.getBatchId(), batch.getPlanId());
            updateSeasonalPlanActualDates(batch.getPlanId());
        }
    }

    /**
     * 更新轮作计划明细的实际日期（如果需要）
     */
    private void updateRotationPlanDetailIfNeeded(AgricultureCropBatch batch) {
        if (batch.getDetailId() == null) {
            return;
        }
        
        log.info("批次 {} 关联轮作计划明细 {}，更新明细实际日期", batch.getBatchId(), batch.getDetailId());
        updatePlanDetailActualDates(batch.getDetailId());
    }

    /**
     * 获取并验证计划
     */
    private AgriculturePlantingPlan getAndValidatePlan(Long planId, String expectedType, String planTypeName) {
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !expectedType.equals(plan.getPlanType())) {
            log.warn("计划 {} 不存在或不是{}", planId, planTypeName);
            return null;
        }
        return plan;
    }

    /**
     * 更新最小日期（从 Date 转换）
     * @return 更新后的最小日期
     */
    private LocalDate updateMinDateFromDate(java.util.Date date, LocalDate currentMin) {
        if (date == null) {
            return currentMin;
        }
        
        LocalDate taskStartDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (currentMin == null || taskStartDate.isBefore(currentMin)) {
            return taskStartDate;
        }
        return currentMin;
    }

    /**
     * 更新最大日期（从 Date 转换）
     * @return 更新后的最大日期
     */
    private LocalDate updateMaxDateFromDate(java.util.Date date, LocalDate currentMax) {
        if (date == null) {
            return currentMax;
        }
        
        LocalDate taskEndDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (currentMax == null || taskEndDate.isAfter(currentMax)) {
            return taskEndDate;
        }
        return currentMax;
    }

    /**
     * 更新最小日期（LocalDate）
     * @return 更新后的最小日期
     */
    private LocalDate updateMinLocalDate(LocalDate date, LocalDate currentMin) {
        if (date == null) {
            return currentMin;
        }
        
        if (currentMin == null || date.isBefore(currentMin)) {
            return date;
        }
        return currentMin;
    }

    /**
     * 更新最大日期（LocalDate）
     * @return 更新后的最大日期
     */
    private LocalDate updateMaxLocalDate(LocalDate date, LocalDate currentMax) {
        if (date == null) {
            return currentMax;
        }
        
        if (currentMax == null || date.isAfter(currentMax)) {
            return date;
        }
        return currentMax;
    }

    /**
     * 更新明细的开始日期（如果需要）
     */
    private void updateDetailStartDateIfNeeded(AgriculturePlanDetail detail, LocalDate minStartDate, Long detailId) {
        if (minStartDate == null) {
            return;
        }
        
        // 如果明细的实际开始日期为null，或者新的开始日期更早，则更新
        if (detail.getActualStartDate() == null || minStartDate.isBefore(detail.getActualStartDate())) {
            log.info("更新轮作计划明细 {} 的实际开始日期: {} -> {}", detailId, detail.getActualStartDate(), minStartDate);
            detail.setActualStartDate(minStartDate);
        }
    }

    /**
     * 更新明细的结束日期（如果所有任务都已完成）
     */
    private void updateDetailEndDateIfAllTasksFinished(AgriculturePlanDetail detail, int totalTaskCount, 
                                                       int finishedTaskCount, LocalDate maxEndDate, Long detailId) {
        if (totalTaskCount > 0 && finishedTaskCount == totalTaskCount && maxEndDate != null) {
            log.info("所有任务已完成，更新轮作计划明细 {} 的实际结束日期: {} -> {}", detailId, detail.getActualEndDate(), maxEndDate);
            detail.setActualEndDate(maxEndDate);
        }
    }

    /**
     * 计算所有明细都完成时的最晚结束日期
     */
    private LocalDate calculateMaxEndDateIfAllFinished(List<AgriculturePlanDetail> details) {
        LocalDate maxEndDate = null;
        
        for (AgriculturePlanDetail detail : details) {
            if (detail.getActualEndDate() == null) {
                // 如果某个明细没有实际结束日期，说明未全部完成
                return null;
            }
            
            if (maxEndDate == null || detail.getActualEndDate().isAfter(maxEndDate)) {
                maxEndDate = detail.getActualEndDate();
            }
        }
        
        return maxEndDate;
    }
}

