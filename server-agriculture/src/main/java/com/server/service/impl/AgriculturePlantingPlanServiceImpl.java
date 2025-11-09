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
        return save(agriculturePlantingPlan) ? 1 : 0;
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
        return updateById(agriculturePlantingPlan) ? 1 : 0;
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
        return removeByIds(Arrays.asList(planIds)) ? planIds.length : 0;
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
        return removeById(planId) ? 1 : 0;
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
}

