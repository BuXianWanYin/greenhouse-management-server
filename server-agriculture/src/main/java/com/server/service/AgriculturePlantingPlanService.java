package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgriculturePlantingPlan;
import com.server.domain.AgricultureCropBatch;

/**
 * 种植计划Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgriculturePlantingPlanService extends IService<AgriculturePlantingPlan>
{
    /**
     * 查询种植计划
     *
     * @param planId 种植计划主键
     * @return 种植计划
     */
    public AgriculturePlantingPlan selectAgriculturePlantingPlanByPlanId(Long planId);

    /**
     * 查询种植计划列表
     *
     * @param agriculturePlantingPlan 种植计划
     * @return 种植计划集合
     */
    public List<AgriculturePlantingPlan> selectAgriculturePlantingPlanList(AgriculturePlantingPlan agriculturePlantingPlan);

    /**
     * 新增种植计划
     *
     * @param agriculturePlantingPlan 种植计划
     * @return 结果
     */
    public int insertAgriculturePlantingPlan(AgriculturePlantingPlan agriculturePlantingPlan);

    /**
     * 修改种植计划
     *
     * @param agriculturePlantingPlan 种植计划
     * @return 结果
     */
    public int updateAgriculturePlantingPlan(AgriculturePlantingPlan agriculturePlantingPlan);

    /**
     * 批量删除种植计划
     *
     * @param planIds 需要删除的种植计划主键集合
     * @return 结果
     */
    public int deleteAgriculturePlantingPlanByPlanIds(Long[] planIds);

    /**
     * 删除种植计划信息
     *
     * @param planId 种植计划主键
     * @return 结果
     */
    public int deleteAgriculturePlantingPlanByPlanId(Long planId);

    /**
     * 获取计划关联的批次列表
     *
     * @param planId 计划ID
     * @return 批次列表
     */
    public List<AgricultureCropBatch> getPlantingPlanBatches(Long planId);

    /**
     * 将批次添加到计划
     *
     * @param planId 计划ID
     * @param batchIds 批次ID数组
     * @return 结果
     */
    public int addBatchToPlan(Long planId, Long[] batchIds);

    /**
     * 从计划中移除批次
     *
     * @param planId 计划ID
     * @param batchId 批次ID
     * @return 结果
     */
    public int removeBatchFromPlan(Long planId, Long batchId);
}

