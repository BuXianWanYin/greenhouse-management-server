package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureAnnualPlan;

/**
 * 年度种植规划Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgricultureAnnualPlanService extends IService<AgricultureAnnualPlan>
{
    /**
     * 查询年度种植规划
     *
     * @param planId 年度种植规划主键
     * @return 年度种植规划
     */
    public AgricultureAnnualPlan selectAgricultureAnnualPlanByPlanId(Long planId);

    /**
     * 查询年度种植规划列表
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 年度种植规划集合
     */
    public List<AgricultureAnnualPlan> selectAgricultureAnnualPlanList(AgricultureAnnualPlan agricultureAnnualPlan);

    /**
     * 新增年度种植规划
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 结果
     */
    public int insertAgricultureAnnualPlan(AgricultureAnnualPlan agricultureAnnualPlan);

    /**
     * 修改年度种植规划
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 结果
     */
    public int updateAgricultureAnnualPlan(AgricultureAnnualPlan agricultureAnnualPlan);

    /**
     * 批量删除年度种植规划
     *
     * @param planIds 需要删除的年度种植规划主键集合
     * @return 结果
     */
    public int deleteAgricultureAnnualPlanByPlanIds(Long[] planIds);

    /**
     * 删除年度种植规划信息
     *
     * @param planId 年度种植规划主键
     * @return 结果
     */
    public int deleteAgricultureAnnualPlanByPlanId(Long planId);

    /**
     * 获取计划关联的批次列表
     *
     * @param planId 年度计划ID
     * @return 批次列表
     */
    public List<com.server.domain.dto.AgricultureCropBatchDTO> getPlanBatches(Long planId);

    /**
     * 将批次添加到计划
     *
     * @param planId 年度计划ID
     * @param batchIds 批次ID数组
     * @return 结果
     */
    public int addBatchToPlan(Long planId, Long[] batchIds);

    /**
     * 从计划中移除批次
     *
     * @param planId 年度计划ID
     * @param batchId 批次ID
     * @return 结果
     */
    public int removeBatchFromPlan(Long planId, Long batchId);
}

