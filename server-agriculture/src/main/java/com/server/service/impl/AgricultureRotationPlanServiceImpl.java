package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgricultureRotationPlan;
import com.server.domain.dto.AgricultureCropBatchDTO;
import com.server.mapper.AgricultureRotationPlanMapper;
import com.server.service.AgricultureCropBatchService;
import com.server.service.AgricultureRotationPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 轮作计划Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgricultureRotationPlanServiceImpl extends ServiceImpl<AgricultureRotationPlanMapper, AgricultureRotationPlan> implements AgricultureRotationPlanService
{
    @Autowired
    private AgricultureRotationPlanMapper agricultureRotationPlanMapper;
    
    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

    /**
     * 查询轮作计划
     *
     * @param rotationId 轮作计划主键
     * @return 轮作计划
     */
    @Override
    public AgricultureRotationPlan selectAgricultureRotationPlanByRotationId(Long rotationId)
    {
        return getById(rotationId);
    }

    /**
     * 查询轮作计划列表
     *
     * @param agricultureRotationPlan 轮作计划
     * @return 轮作计划
     */
    @Override
    public List<AgricultureRotationPlan> selectAgricultureRotationPlanList(AgricultureRotationPlan agricultureRotationPlan)
    {
        LambdaQueryWrapper<AgricultureRotationPlan> queryWrapper = new LambdaQueryWrapper<>();
        if (agricultureRotationPlan.getPlanYear() != null) {
            queryWrapper.eq(AgricultureRotationPlan::getPlanYear, agricultureRotationPlan.getPlanYear());
        }
        if (agricultureRotationPlan.getPastureId() != null) {
            queryWrapper.eq(AgricultureRotationPlan::getPastureId, agricultureRotationPlan.getPastureId());
        }
        if (agricultureRotationPlan.getRotationStatus() != null) {
            queryWrapper.eq(AgricultureRotationPlan::getRotationStatus, agricultureRotationPlan.getRotationStatus());
        }
        if (agricultureRotationPlan.getRotationName() != null) {
            queryWrapper.like(AgricultureRotationPlan::getRotationName, agricultureRotationPlan.getRotationName());
        }
        queryWrapper.eq(AgricultureRotationPlan::getDelFlag, "0");
        queryWrapper.orderByDesc(AgricultureRotationPlan::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增轮作计划
     *
     * @param agricultureRotationPlan 轮作计划
     * @return 结果
     */
    @Override
    public int insertAgricultureRotationPlan(AgricultureRotationPlan agricultureRotationPlan)
    {
        return save(agricultureRotationPlan) ? 1 : 0;
    }

    /**
     * 修改轮作计划
     *
     * @param agricultureRotationPlan 轮作计划
     * @return 结果
     */
    @Override
    public int updateAgricultureRotationPlan(AgricultureRotationPlan agricultureRotationPlan)
    {
        return updateById(agricultureRotationPlan) ? 1 : 0;
    }

    /**
     * 批量删除轮作计划
     *
     * @param rotationIds 需要删除的轮作计划主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureRotationPlanByRotationIds(Long[] rotationIds)
    {
        return removeByIds(Arrays.asList(rotationIds)) ? rotationIds.length : 0;
    }

    /**
     * 删除轮作计划信息
     *
     * @param rotationId 轮作计划主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureRotationPlanByRotationId(Long rotationId)
    {
        return removeById(rotationId) ? 1 : 0;
    }

    /**
     * 获取轮作计划关联的批次列表
     *
     * @param rotationId 轮作计划ID
     * @return 批次列表
     */
    @Override
    public List<AgricultureCropBatchDTO> getRotationPlanBatches(Long rotationId)
    {
        // 通过rotation_plan_id查询批次
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgricultureCropBatch::getRotationPlanId, rotationId);
        List<AgricultureCropBatch> batchList = agricultureCropBatchService.list(queryWrapper);
        
        // 转换为DTO
        return batchList.stream().map(batch -> {
            AgricultureCropBatchDTO dto = new AgricultureCropBatchDTO();
            dto.setBatchId(String.valueOf(batch.getBatchId()));
            dto.setBatchName(batch.getBatchName());
            dto.setClassId(batch.getClassId());
            dto.setPastureId(batch.getPastureId());
            dto.setStartTime(batch.getStartTime());
            dto.setCropArea(batch.getCropArea() != null ? batch.getCropArea() : 0.0);
            // 将Date转换为LocalDate
            if (batch.getCreateTime() != null) {
                LocalDate localDate = batch.getCreateTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                dto.setCreateTime(localDate);
            }
            return dto;
        }).collect(Collectors.toList());
    }
}

