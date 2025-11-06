package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureRotationPlan;

/**
 * 轮作计划Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgricultureRotationPlanService extends IService<AgricultureRotationPlan>
{
    /**
     * 查询轮作计划
     *
     * @param rotationId 轮作计划主键
     * @return 轮作计划
     */
    public AgricultureRotationPlan selectAgricultureRotationPlanByRotationId(Long rotationId);

    /**
     * 查询轮作计划列表
     *
     * @param agricultureRotationPlan 轮作计划
     * @return 轮作计划集合
     */
    public List<AgricultureRotationPlan> selectAgricultureRotationPlanList(AgricultureRotationPlan agricultureRotationPlan);

    /**
     * 新增轮作计划
     *
     * @param agricultureRotationPlan 轮作计划
     * @return 结果
     */
    public int insertAgricultureRotationPlan(AgricultureRotationPlan agricultureRotationPlan);

    /**
     * 修改轮作计划
     *
     * @param agricultureRotationPlan 轮作计划
     * @return 结果
     */
    public int updateAgricultureRotationPlan(AgricultureRotationPlan agricultureRotationPlan);

    /**
     * 批量删除轮作计划
     *
     * @param rotationIds 需要删除的轮作计划主键集合
     * @return 结果
     */
    public int deleteAgricultureRotationPlanByRotationIds(Long[] rotationIds);

    /**
     * 删除轮作计划信息
     *
     * @param rotationId 轮作计划主键
     * @return 结果
     */
    public int deleteAgricultureRotationPlanByRotationId(Long rotationId);

    /**
     * 获取轮作计划关联的批次列表
     *
     * @param rotationId 轮作计划ID
     * @return 批次列表
     */
    public List<com.server.domain.dto.AgricultureCropBatchDTO> getRotationPlanBatches(Long rotationId);
}

