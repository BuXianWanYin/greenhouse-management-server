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

        // 如果批次关联了季度计划，更新季度计划的实际日期
        if (batch.getPlanId() != null) {
            AgriculturePlantingPlan plan = plantingPlanService.getById(batch.getPlanId());
            if (plan != null && "seasonal".equals(plan.getPlanType())) {
                log.info("批次 {} 关联季度计划 {}，更新季度计划实际日期", batchId, batch.getPlanId());
                updateSeasonalPlanActualDates(batch.getPlanId());
            }
        }

        // 如果批次关联了轮作计划明细（detail_id 不为空），更新明细的实际日期
        if (batch.getDetailId() != null) {
            log.info("批次 {} 关联轮作计划明细 {}，更新明细实际日期", batchId, batch.getDetailId());
            updatePlanDetailActualDates(batch.getDetailId());
        }

        log.info("完成批次 {} 相关的计划实际日期更新", batchId);
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

        // 查询季度计划
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !"seasonal".equals(plan.getPlanType())) {
            log.warn("计划 {} 不存在或不是季度计划", planId);
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

        for (AgricultureCropBatch batch : batches) {
            // 查询该批次的所有任务
            List<AgricultureBatchTask> tasks = batchTaskService.selectBatchTaskListByBatchId(batch.getBatchId());
            
            for (AgricultureBatchTask task : tasks) {
                if (task.getActualStart() != null) {
                    LocalDate taskStartDate = task.getActualStart().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    if (minStartDate == null || taskStartDate.isBefore(minStartDate)) {
                        minStartDate = taskStartDate;
                    }
                }
                
                if (task.getActualFinish() != null) {
                    LocalDate taskEndDate = task.getActualFinish().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    if (maxEndDate == null || taskEndDate.isAfter(maxEndDate)) {
                        maxEndDate = taskEndDate;
                    }
                }
            }
        }

        // 更新季度计划的实际日期
        updatePlanActualDates(planId, minStartDate, maxEndDate);

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

        // 查询年度计划
        AgriculturePlantingPlan plan = plantingPlanService.getById(planId);
        if (plan == null || !"annual".equals(plan.getPlanType())) {
            log.warn("计划 {} 不存在或不是年度计划", planId);
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

        for (AgriculturePlantingPlan seasonalPlan : seasonalPlans) {
            if (seasonalPlan.getActualStartDate() != null) {
                if (minStartDate == null || seasonalPlan.getActualStartDate().isBefore(minStartDate)) {
                    minStartDate = seasonalPlan.getActualStartDate();
                }
            }
            
            if (seasonalPlan.getActualEndDate() != null) {
                if (maxEndDate == null || seasonalPlan.getActualEndDate().isAfter(maxEndDate)) {
                    maxEndDate = seasonalPlan.getActualEndDate();
                }
            }
        }

        // 更新年度计划的实际日期
        updatePlanActualDates(planId, minStartDate, maxEndDate);

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

        for (AgricultureCropBatch batch : batches) {
            // 查询该批次的所有任务
            List<AgricultureBatchTask> tasks = batchTaskService.selectBatchTaskListByBatchId(batch.getBatchId());
            
            for (AgricultureBatchTask task : tasks) {
                if (task.getActualStart() != null) {
                    LocalDate taskStartDate = task.getActualStart().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    if (minStartDate == null || taskStartDate.isBefore(minStartDate)) {
                        minStartDate = taskStartDate;
                    }
                }
                
                if (task.getActualFinish() != null) {
                    LocalDate taskEndDate = task.getActualFinish().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    if (maxEndDate == null || taskEndDate.isAfter(maxEndDate)) {
                        maxEndDate = taskEndDate;
                    }
                }
            }
        }

        // 更新轮作计划明细的实际日期
        updatePlanDetailActualDates(detailId, minStartDate, maxEndDate);

        // 如果明细有父轮作计划，更新轮作计划的实际日期
        if (detail.getPlanId() != null) {
            log.info("轮作计划明细 {} 有父轮作计划 {}，更新轮作计划实际日期", detailId, detail.getPlanId());
            updateRotationPlanActualDates(detail.getPlanId());
        }

        log.info("完成轮作计划明细 {} 的实际日期更新: {} - {}", detailId, minStartDate, maxEndDate);
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
            log.info("轮作计划 {} 没有明细，清空实际日期", planId);
            clearPlanActualDates(planId);
            return;
        }

        // 收集所有明细的实际日期
        LocalDate minStartDate = null;
        LocalDate maxEndDate = null;

        for (AgriculturePlanDetail detail : details) {
            if (detail.getActualStartDate() != null) {
                if (minStartDate == null || detail.getActualStartDate().isBefore(minStartDate)) {
                    minStartDate = detail.getActualStartDate();
                }
            }
            
            if (detail.getActualEndDate() != null) {
                if (maxEndDate == null || detail.getActualEndDate().isAfter(maxEndDate)) {
                    maxEndDate = detail.getActualEndDate();
                }
            }
        }

        // 更新轮作计划的实际日期
        updatePlanActualDates(planId, minStartDate, maxEndDate);

        log.info("完成轮作计划 {} 的实际日期更新: {} - {}", planId, minStartDate, maxEndDate);
    }

    /**
     * 更新计划的实际日期
     */
    private void updatePlanActualDates(Long planId, LocalDate actualStartDate, LocalDate actualEndDate) {
        LambdaUpdateWrapper<AgriculturePlantingPlan> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgriculturePlantingPlan::getPlanId, planId)
                .set(AgriculturePlantingPlan::getActualStartDate, actualStartDate)
                .set(AgriculturePlantingPlan::getActualEndDate, actualEndDate);
        plantingPlanService.update(updateWrapper);
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
}

