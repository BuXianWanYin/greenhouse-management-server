package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgriculturePlanDetail;
import com.server.domain.AgriculturePlantingPlan;
import com.server.mapper.AgriculturePlantingPlanMapper;
import com.server.exception.ServiceException;
import com.server.service.AgricultureCropBatchService;
import com.server.service.AgriculturePlanDetailService;
import com.server.service.AgriculturePlantingPlanService;
import com.server.service.PlanDateUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 种植计划Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Slf4j
@Service
public class AgriculturePlantingPlanServiceImpl extends ServiceImpl<AgriculturePlantingPlanMapper, AgriculturePlantingPlan> implements AgriculturePlantingPlanService
{
    @Autowired
    private AgriculturePlantingPlanMapper agriculturePlantingPlanMapper;
    
    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;
    
    @Autowired
    private PlanDateUpdateService planDateUpdateService;
    
    @Autowired
    private AgriculturePlanDetailService agriculturePlanDetailService;

    /**
     * 查询种植计划
     *
     * @param planId 种植计划主键
     * @return 种植计划
     */
    @Override
    public AgriculturePlantingPlan selectAgriculturePlantingPlanByPlanId(Long planId)
    {
        return getById(planId);
    }

    /**
     * 查询种植计划列表
     *
     * @param agriculturePlantingPlan 种植计划
     * @return 种植计划
     */
    @Override
    public List<AgriculturePlantingPlan> selectAgriculturePlantingPlanList(AgriculturePlantingPlan agriculturePlantingPlan)
    {
        LambdaQueryWrapper<AgriculturePlantingPlan> queryWrapper = new LambdaQueryWrapper<>();
        if (agriculturePlantingPlan.getPlanYear() != null) {
            queryWrapper.eq(AgriculturePlantingPlan::getPlanYear, agriculturePlantingPlan.getPlanYear());
        }
        if (agriculturePlantingPlan.getPastureId() != null) {
            queryWrapper.eq(AgriculturePlantingPlan::getPastureId, agriculturePlantingPlan.getPastureId());
        }
        if (agriculturePlantingPlan.getPlanStatus() != null) {
            queryWrapper.eq(AgriculturePlantingPlan::getPlanStatus, agriculturePlantingPlan.getPlanStatus());
        }
        if (agriculturePlantingPlan.getPlanName() != null) {
            queryWrapper.like(AgriculturePlantingPlan::getPlanName, agriculturePlantingPlan.getPlanName());
        }
        if (agriculturePlantingPlan.getPlanType() != null) {
            queryWrapper.eq(AgriculturePlantingPlan::getPlanType, agriculturePlantingPlan.getPlanType());
        }
        // 只查询未删除的记录（del_flag = "0" 或 del_flag 为 NULL）
        queryWrapper.and(wrapper -> wrapper.eq(AgriculturePlantingPlan::getDelFlag, "0")
                .or()
                .isNull(AgriculturePlantingPlan::getDelFlag));
        queryWrapper.orderByDesc(AgriculturePlantingPlan::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增种植计划
     *
     * @param agriculturePlantingPlan 种植计划
     * @return 结果
     */
    @Override
    public int insertAgriculturePlantingPlan(AgriculturePlantingPlan agriculturePlantingPlan)
    {
        int result = save(agriculturePlantingPlan) ? 1 : 0;
        
        // 如果是季度计划，更新父年度计划状态
        if (result > 0 && agriculturePlantingPlan.getParentPlanId() != null) {
            updateAnnualPlanStatusBySeasonalPlans(agriculturePlantingPlan.getParentPlanId());
        }
        
        return result;
    }

    /**
     * 修改种植计划
     *
     * @param agriculturePlantingPlan 种植计划
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAgriculturePlantingPlan(AgriculturePlantingPlan agriculturePlantingPlan)
    {
        // 获取修改前的计划信息，用于判断是否需要更新父计划状态和日期
        AgriculturePlantingPlan oldPlan = null;
        if (agriculturePlantingPlan.getPlanId() != null) {
            oldPlan = getById(agriculturePlantingPlan.getPlanId());
        }
        
        int result = updateById(agriculturePlantingPlan) ? 1 : 0;
        
        // 如果是季度计划，更新父年度计划状态和日期
        if (result > 0) {
            Long parentPlanId = agriculturePlantingPlan.getParentPlanId();
            // 如果父计划ID发生变化，需要更新新旧两个父计划的状态和日期
            if (oldPlan != null && oldPlan.getParentPlanId() != null 
                    && !oldPlan.getParentPlanId().equals(parentPlanId)) {
                // 更新旧父计划状态
                updateAnnualPlanStatusBySeasonalPlans(oldPlan.getParentPlanId());
                // 更新旧父计划日期
                try {
                    planDateUpdateService.updateAnnualPlanActualDates(oldPlan.getParentPlanId());
                } catch (Exception e) {
                    log.error("更新年度计划实际日期失败，计划ID: {}", oldPlan.getParentPlanId(), e);
                }
            }
            if (parentPlanId != null) {
                // 更新新父计划状态
                updateAnnualPlanStatusBySeasonalPlans(parentPlanId);
                // 更新新父计划日期
                try {
                    planDateUpdateService.updateAnnualPlanActualDates(parentPlanId);
                } catch (Exception e) {
                    log.error("更新年度计划实际日期失败，计划ID: {}", parentPlanId, e);
                }
            }
            
            // 如果是季度计划，更新其实际日期（基于关联批次的任务）
            if ("seasonal".equals(agriculturePlantingPlan.getPlanType())) {
                try {
                    planDateUpdateService.updateSeasonalPlanActualDates(agriculturePlantingPlan.getPlanId());
                } catch (Exception e) {
                    log.error("更新季度计划实际日期失败，计划ID: {}", agriculturePlantingPlan.getPlanId(), e);
                }
            }
            
            // 如果是轮作计划，更新其实际日期（基于关联明细）
            if ("rotation".equals(agriculturePlantingPlan.getPlanType())) {
                try {
                    planDateUpdateService.updateRotationPlanActualDates(agriculturePlantingPlan.getPlanId());
                } catch (Exception e) {
                    log.error("更新轮作计划实际日期失败，计划ID: {}", agriculturePlantingPlan.getPlanId(), e);
                }
            }
        }
        
        return result;
    }

    /**
     * 批量删除种植计划
     *
     * @param planIds 需要删除的种植计划主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgriculturePlantingPlanByPlanIds(Long[] planIds)
    {
        // 删除前，先获取所有要删除的计划信息，用于校验和更新父计划状态和日期
        List<AgriculturePlantingPlan> plansToDelete = listByIds(Arrays.asList(planIds));
        
        // 删除前校验：检查是否有关联批次
        if (plansToDelete != null) {
            for (AgriculturePlantingPlan plan : plansToDelete) {
                checkPlanHasBatches(plan);
            }
        }
        
        int result = removeByIds(Arrays.asList(planIds)) ? planIds.length : 0;
        
        // 删除后，更新相关年度计划状态和日期
        if (result > 0 && plansToDelete != null) {
            // 收集所有需要更新的父计划ID（去重）
            plansToDelete.stream()
                    .filter(plan -> plan.getParentPlanId() != null)
                    .map(AgriculturePlantingPlan::getParentPlanId)
                    .distinct()
                    .forEach(parentPlanId -> {
                        // 更新父计划状态
                        updateAnnualPlanStatusBySeasonalPlans(parentPlanId);
                        // 更新父计划日期
                        try {
                            planDateUpdateService.updateAnnualPlanActualDates(parentPlanId);
                        } catch (Exception e) {
                            log.error("更新年度计划实际日期失败，计划ID: {}", parentPlanId, e);
                        }
                    });
        }
        
        return result;
    }

    /**
     * 删除种植计划信息
     *
     * @param planId 种植计划主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgriculturePlantingPlanByPlanId(Long planId)
    {
        // 删除前，先获取计划信息，用于校验和更新父计划状态和日期
        AgriculturePlantingPlan plan = getById(planId);
        if (plan == null) {
            throw new RuntimeException("计划不存在，ID: " + planId);
        }
        
        // 删除前校验：检查是否有关联批次
        checkPlanHasBatches(plan);
        
        Long parentPlanId = plan.getParentPlanId();
        int result = removeById(planId) ? 1 : 0;
        
        // 删除后，如果是季度计划，更新父年度计划状态和日期
        if (result > 0 && parentPlanId != null) {
            // 更新父计划状态
            updateAnnualPlanStatusBySeasonalPlans(parentPlanId);
            // 更新父计划日期
            try {
                planDateUpdateService.updateAnnualPlanActualDates(parentPlanId);
            } catch (Exception e) {
                log.error("更新年度计划实际日期失败，计划ID: {}", parentPlanId, e);
            }
        }
        
        return result;
    }

    /**
     * 获取种植计划关联的批次列表
     *
     * @param planId 种植计划ID
     * @return 批次列表
     */
    @Override
    public List<AgricultureCropBatch> getPlantingPlanBatches(Long planId)
    {
        // 通过plan_id查询批次
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgricultureCropBatch::getPlanId, planId);
        return agricultureCropBatchService.list(queryWrapper);
    }

    /**
     * 将批次添加到计划
     *
     * @param planId 计划ID
     * @param batchIds 批次ID数组
     * @return 结果
     */
    @Override
    public int addBatchToPlan(Long planId, Long[] batchIds)
    {
        int count = 0;
        for (Long batchId : batchIds) {
            // 查询批次信息
            AgricultureCropBatch batch = agricultureCropBatchService.getById(batchId);
            if (batch != null && (batch.getPlanId() == null || !batch.getPlanId().equals(planId))) {
                // 更新批次的计划ID
                batch.setPlanId(planId);
                if (agricultureCropBatchService.updateById(batch)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 从计划中移除批次
     *
     * @param planId 计划ID
     * @param batchId 批次ID
     * @return 结果
     */
    @Override
    public int removeBatchFromPlan(Long planId, Long batchId)
    {
        // 查询批次信息
        AgricultureCropBatch batch = agricultureCropBatchService.getById(batchId);
        if (batch != null && batch.getPlanId() != null && batch.getPlanId().equals(planId)) {
            // 清空批次的计划ID
            batch.setPlanId(null);
            return agricultureCropBatchService.updateById(batch) ? 1 : 0;
        }
        return 0;
    }

    /**
     * 根据季度计划状态更新年度计划状态
     * 
     * 状态更新规则：
     * 1. 任一季度计划为"执行中"(1) → 年度计划设为"执行中"(1)
     * 2. 所有季度计划为"已完成"(2) → 年度计划设为"已完成"(2)
     * 3. 所有季度计划为"已取消"(3) → 年度计划设为"已取消"(3)
     * 4. 部分已完成或部分执行中 → 年度计划设为"执行中"(1)
     * 5. 所有季度计划为"未开始"(0) → 年度计划设为"未开始"(0)
     * 
     * @param parentPlanId 年度计划ID
     */
    @Override
    public void updateAnnualPlanStatusBySeasonalPlans(Long parentPlanId)
    {
        if (parentPlanId == null) {
            return;
        }

        // 查询年度计划
        AgriculturePlantingPlan annualPlan = getById(parentPlanId);
        if (annualPlan == null || !"annual".equals(annualPlan.getPlanType())) {
            // 不是年度计划，不需要更新
            return;
        }

        // 查询该年度计划下的所有季度计划（未删除的）
        LambdaQueryWrapper<AgriculturePlantingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgriculturePlantingPlan::getParentPlanId, parentPlanId)
                .eq(AgriculturePlantingPlan::getPlanType, "seasonal")
                .and(wrapper -> wrapper.eq(AgriculturePlantingPlan::getDelFlag, "0")
                        .or()
                        .isNull(AgriculturePlantingPlan::getDelFlag));

        List<AgriculturePlantingPlan> seasonalPlans = list(queryWrapper);

        // 如果没有季度计划，保持年度计划原状态
        if (seasonalPlans == null || seasonalPlans.isEmpty()) {
            return;
        }

        // 统计各状态的季度计划数量
        long executingCount = 0;  // 执行中(1)
        long completedCount = 0;  // 已完成(2)
        long cancelledCount = 0;  // 已取消(3)
        long notStartedCount = 0; // 未开始(0)

        for (AgriculturePlantingPlan plan : seasonalPlans) {
            String status = plan.getPlanStatus();
            if ("1".equals(status)) {
                executingCount++;
            } else if ("2".equals(status)) {
                completedCount++;
            } else if ("3".equals(status)) {
                cancelledCount++;
            } else {
                notStartedCount++; // 默认为未开始(0)
            }
        }

        int totalCount = seasonalPlans.size();
        String newStatus = annualPlan.getPlanStatus(); // 默认保持原状态

        // 状态更新逻辑
        if (executingCount > 0) {
            // 任一季度计划为"执行中" → 年度计划设为"执行中"
            newStatus = "1";
        } else if (completedCount == totalCount) {
            // 所有季度计划为"已完成" → 年度计划设为"已完成"
            newStatus = "2";
        } else if (cancelledCount == totalCount) {
            // 所有季度计划为"已取消" → 年度计划设为"已取消"
            newStatus = "3";
        } else if (completedCount > 0 || executingCount > 0) {
            // 部分已完成或部分执行中 → 年度计划设为"执行中"
            newStatus = "1";
        } else if (notStartedCount == totalCount) {
            // 所有季度计划为"未开始" → 年度计划设为"未开始"
            newStatus = "0";
        }

        // 如果状态发生变化，更新年度计划
        if (!newStatus.equals(annualPlan.getPlanStatus())) {
            annualPlan.setPlanStatus(newStatus);
            updateById(annualPlan);
        }
    }

    /**
     * 检查计划是否有关联批次
     * 如果有关联批次，抛出异常，不允许删除
     *
     * @param plan 种植计划
     * @throws ServiceException 如果计划有关联批次
     */
    private void checkPlanHasBatches(AgriculturePlantingPlan plan) {
        if (plan == null) {
            return;
        }

        String planType = plan.getPlanType();
        Long planId = plan.getPlanId();

        if ("annual".equals(planType)) {
            // 年度计划：检查其下是否有季度计划，如果有，不允许删除
            LambdaQueryWrapper<AgriculturePlantingPlan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AgriculturePlantingPlan::getParentPlanId, planId)
                    .eq(AgriculturePlantingPlan::getPlanType, "seasonal")
                    .and(wrapper -> wrapper.eq(AgriculturePlantingPlan::getDelFlag, "0")
                            .or()
                            .isNull(AgriculturePlantingPlan::getDelFlag));
            List<AgriculturePlantingPlan> seasonalPlans = list(queryWrapper);

            if (seasonalPlans != null && !seasonalPlans.isEmpty()) {
                // 如果有季度计划，不允许删除年度计划
                String seasonalPlanNames = seasonalPlans.stream()
                        .map(AgriculturePlantingPlan::getPlanName)
                        .collect(Collectors.joining("、"));
                throw new ServiceException(
                        String.format("年度计划【%s】下存在季度计划【%s】，无法删除。请先删除所有季度计划", 
                                plan.getPlanName(), seasonalPlanNames));
            }
        } else if ("seasonal".equals(planType)) {
            // 季度计划：检查是否有关联批次
            LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
            batchQuery.eq(AgricultureCropBatch::getPlanId, planId)
                    .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                            .or()
                            .isNull(AgricultureCropBatch::getDelFlag));
            long batchCount = agricultureCropBatchService.count(batchQuery);
            if (batchCount > 0) {
                throw new ServiceException(
                        String.format("季度计划【%s】有关联批次，无法删除", plan.getPlanName()));
            }
        } else if ("rotation".equals(planType)) {
            // 轮作计划：检查其下是否有明细，如果有，检查这些明细是否有关联批次
            LambdaQueryWrapper<AgriculturePlanDetail> detailQuery = new LambdaQueryWrapper<>();
            detailQuery.eq(AgriculturePlanDetail::getPlanId, planId);
            List<AgriculturePlanDetail> details = agriculturePlanDetailService.list(detailQuery);

            if (details != null && !details.isEmpty()) {
                // 检查每个明细是否有关联批次
                for (AgriculturePlanDetail detail : details) {
                    if (detail.getClassId() != null && detail.getSeasonType() != null) {
                        LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
                        batchQuery.eq(AgricultureCropBatch::getClassId, detail.getClassId())
                                .eq(AgricultureCropBatch::getSeasonType, detail.getSeasonType())
                                .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                                        .or()
                                        .isNull(AgricultureCropBatch::getDelFlag));
                        long batchCount = agricultureCropBatchService.count(batchQuery);
                        if (batchCount > 0) {
                            throw new ServiceException(
                                    String.format("轮作计划【%s】下存在明细【种质ID:%d, 季节:%s】有关联批次，无法删除", 
                                            plan.getPlanName(), detail.getClassId(), detail.getSeasonType()));
                        }
                    }
                }
            }
        }
    }
}

