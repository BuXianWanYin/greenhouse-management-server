package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.ZoneId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureAnnualPlan;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgriculturePlanBatch;
import com.server.domain.dto.AgricultureCropBatchDTO;
import com.server.mapper.AgricultureAnnualPlanMapper;
import com.server.service.AgricultureAnnualPlanService;
import com.server.service.AgricultureCropBatchService;
import com.server.service.AgriculturePlanBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 年度种植规划Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgricultureAnnualPlanServiceImpl extends ServiceImpl<AgricultureAnnualPlanMapper, AgricultureAnnualPlan> implements AgricultureAnnualPlanService
{
    @Autowired
    private AgricultureAnnualPlanMapper agricultureAnnualPlanMapper;
    
    @Autowired
    private AgriculturePlanBatchService agriculturePlanBatchService;
    
    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

    /**
     * 查询年度种植规划
     *
     * @param planId 年度种植规划主键
     * @return 年度种植规划
     */
    @Override
    public AgricultureAnnualPlan selectAgricultureAnnualPlanByPlanId(Long planId)
    {
        return getById(planId);
    }

    /**
     * 查询年度种植规划列表
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 年度种植规划
     */
    @Override
    public List<AgricultureAnnualPlan> selectAgricultureAnnualPlanList(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        LambdaQueryWrapper<AgricultureAnnualPlan> queryWrapper = new LambdaQueryWrapper<>();
        if (agricultureAnnualPlan.getPlanYear() != null) {
            queryWrapper.eq(AgricultureAnnualPlan::getPlanYear, agricultureAnnualPlan.getPlanYear());
        }
        if (agricultureAnnualPlan.getPastureId() != null) {
            queryWrapper.eq(AgricultureAnnualPlan::getPastureId, agricultureAnnualPlan.getPastureId());
        }
        if (agricultureAnnualPlan.getPlanStatus() != null) {
            queryWrapper.eq(AgricultureAnnualPlan::getPlanStatus, agricultureAnnualPlan.getPlanStatus());
        }
        if (agricultureAnnualPlan.getPlanName() != null) {
            queryWrapper.like(AgricultureAnnualPlan::getPlanName, agricultureAnnualPlan.getPlanName());
        }
        queryWrapper.eq(AgricultureAnnualPlan::getDelFlag, "0");
        queryWrapper.orderByDesc(AgricultureAnnualPlan::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增年度种植规划
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 结果
     */
    @Override
    public int insertAgricultureAnnualPlan(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        return save(agricultureAnnualPlan) ? 1 : 0;
    }

    /**
     * 修改年度种植规划
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 结果
     */
    @Override
    public int updateAgricultureAnnualPlan(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        return updateById(agricultureAnnualPlan) ? 1 : 0;
    }

    /**
     * 批量删除年度种植规划
     *
     * @param planIds 需要删除的年度种植规划主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAnnualPlanByPlanIds(Long[] planIds)
    {
        return removeByIds(Arrays.asList(planIds)) ? planIds.length : 0;
    }

    /**
     * 删除年度种植规划信息
     *
     * @param planId 年度种植规划主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAnnualPlanByPlanId(Long planId)
    {
        return removeById(planId) ? 1 : 0;
    }

    /**
     * 获取计划关联的批次列表
     *
     * @param planId 年度计划ID
     * @return 批次列表
     */
    @Override
    public List<AgricultureCropBatchDTO> getPlanBatches(Long planId)
    {
        // 查询计划批次关联表
        AgriculturePlanBatch queryParam = new AgriculturePlanBatch();
        queryParam.setPlanId(planId);
        List<AgriculturePlanBatch> planBatchList = agriculturePlanBatchService.selectAgriculturePlanBatchList(queryParam);
        
        // 提取批次ID列表
        List<Long> batchIds = planBatchList.stream()
                .map(AgriculturePlanBatch::getBatchId)
                .collect(Collectors.toList());
        
        if (batchIds.isEmpty()) {
            return List.of();
        }
        
        // 查询批次信息
        List<AgricultureCropBatch> batchList = agricultureCropBatchService.listByIds(batchIds);
        
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

    /**
     * 将批次添加到计划
     *
     * @param planId 年度计划ID
     * @param batchIds 批次ID数组
     * @return 结果
     */
    @Override
    public int addBatchToPlan(Long planId, Long[] batchIds)
    {
        int count = 0;
        for (Long batchId : batchIds) {
            // 检查是否已存在关联
            AgriculturePlanBatch queryParam = new AgriculturePlanBatch();
            queryParam.setPlanId(planId);
            queryParam.setBatchId(batchId);
            List<AgriculturePlanBatch> existing = agriculturePlanBatchService.selectAgriculturePlanBatchList(queryParam);
            
            if (existing.isEmpty()) {
                // 创建新的关联
                AgriculturePlanBatch planBatch = new AgriculturePlanBatch();
                planBatch.setPlanId(planId);
                planBatch.setBatchId(batchId);
                if (agriculturePlanBatchService.insertAgriculturePlanBatch(planBatch) > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 从计划中移除批次
     *
     * @param planId 年度计划ID
     * @param batchId 批次ID
     * @return 结果
     */
    @Override
    public int removeBatchFromPlan(Long planId, Long batchId)
    {
        // 查询关联记录
        AgriculturePlanBatch queryParam = new AgriculturePlanBatch();
        queryParam.setPlanId(planId);
        queryParam.setBatchId(batchId);
        List<AgriculturePlanBatch> planBatchList = agriculturePlanBatchService.selectAgriculturePlanBatchList(queryParam);
        
        if (planBatchList.isEmpty()) {
            return 0;
        }
        
        // 删除关联记录
        Long[] ids = planBatchList.stream()
                .map(AgriculturePlanBatch::getId)
                .toArray(Long[]::new);
        return agriculturePlanBatchService.deleteAgriculturePlanBatchByIds(ids);
    }
}

