package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgriculturePlantingPlan;
import com.server.mapper.AgriculturePlantingPlanMapper;
import com.server.service.AgricultureCropBatchService;
import com.server.service.AgriculturePlantingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 种植计划Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgriculturePlantingPlanServiceImpl extends ServiceImpl<AgriculturePlantingPlanMapper, AgriculturePlantingPlan> implements AgriculturePlantingPlanService
{
    @Autowired
    private AgriculturePlantingPlanMapper agriculturePlantingPlanMapper;
    
    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

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
    public int updateAgriculturePlantingPlan(AgriculturePlantingPlan agriculturePlantingPlan)
    {
        // 获取修改前的计划信息，用于判断是否需要更新父计划状态
        AgriculturePlantingPlan oldPlan = null;
        if (agriculturePlantingPlan.getPlanId() != null) {
            oldPlan = getById(agriculturePlantingPlan.getPlanId());
        }
        
        int result = updateById(agriculturePlantingPlan) ? 1 : 0;
        
        // 如果是季度计划，更新父年度计划状态
        if (result > 0) {
            Long parentPlanId = agriculturePlantingPlan.getParentPlanId();
            // 如果父计划ID发生变化，需要更新新旧两个父计划的状态
            if (oldPlan != null && oldPlan.getParentPlanId() != null 
                    && !oldPlan.getParentPlanId().equals(parentPlanId)) {
                // 更新旧父计划状态
                updateAnnualPlanStatusBySeasonalPlans(oldPlan.getParentPlanId());
            }
            if (parentPlanId != null) {
                // 更新新父计划状态
                updateAnnualPlanStatusBySeasonalPlans(parentPlanId);
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
    public int deleteAgriculturePlantingPlanByPlanIds(Long[] planIds)
    {
        // 删除前，先获取所有要删除的计划信息，用于更新父计划状态
        List<AgriculturePlantingPlan> plansToDelete = listByIds(Arrays.asList(planIds));
        
        int result = removeByIds(Arrays.asList(planIds)) ? planIds.length : 0;
        
        // 删除后，更新相关年度计划状态
        if (result > 0 && plansToDelete != null) {
            // 收集所有需要更新的父计划ID（去重）
            plansToDelete.stream()
                    .filter(plan -> plan.getParentPlanId() != null)
                    .map(AgriculturePlantingPlan::getParentPlanId)
                    .distinct()
                    .forEach(this::updateAnnualPlanStatusBySeasonalPlans);
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
    public int deleteAgriculturePlantingPlanByPlanId(Long planId)
    {
        // 删除前，先获取计划信息，用于更新父计划状态
        AgriculturePlantingPlan plan = getById(planId);
        Long parentPlanId = plan != null ? plan.getParentPlanId() : null;
        
        int result = removeById(planId) ? 1 : 0;
        
        // 删除后，如果是季度计划，更新父年度计划状态
        if (result > 0 && parentPlanId != null) {
            updateAnnualPlanStatusBySeasonalPlans(parentPlanId);
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
}

