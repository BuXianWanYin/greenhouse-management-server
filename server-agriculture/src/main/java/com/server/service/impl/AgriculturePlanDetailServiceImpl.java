package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgriculturePlanDetail;
import com.server.mapper.AgriculturePlanDetailMapper;
import com.server.exception.ServiceException;
import com.server.service.AgricultureCropBatchService;
import com.server.service.AgriculturePlanDetailService;
import com.server.service.PlanDateUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 种植计划明细Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Slf4j
@Service
public class AgriculturePlanDetailServiceImpl extends ServiceImpl<AgriculturePlanDetailMapper, AgriculturePlanDetail> implements AgriculturePlanDetailService
{
    @Autowired
    private AgriculturePlanDetailMapper agriculturePlanDetailMapper;
    
    @Autowired
    private PlanDateUpdateService planDateUpdateService;
    
    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

    /**
     * 查询种植计划明细
     *
     * @param detailId 种植计划明细主键
     * @return 种植计划明细
     */
    @Override
    public AgriculturePlanDetail selectAgriculturePlanDetailByDetailId(Long detailId)
    {
        return getById(detailId);
    }

    /**
     * 查询种植计划明细列表
     *
     * @param agriculturePlanDetail 种植计划明细
     * @return 种植计划明细
     */
    @Override
    public List<AgriculturePlanDetail> selectAgriculturePlanDetailList(AgriculturePlanDetail agriculturePlanDetail)
    {
        LambdaQueryWrapper<AgriculturePlanDetail> queryWrapper = new LambdaQueryWrapper<>();
        if (agriculturePlanDetail.getPlanId() != null) {
            queryWrapper.eq(AgriculturePlanDetail::getPlanId, agriculturePlanDetail.getPlanId());
        }
        if (agriculturePlanDetail.getClassId() != null) {
            queryWrapper.eq(AgriculturePlanDetail::getClassId, agriculturePlanDetail.getClassId());
        }
        if (agriculturePlanDetail.getSeasonType() != null) {
            queryWrapper.eq(AgriculturePlanDetail::getSeasonType, agriculturePlanDetail.getSeasonType());
        }
        queryWrapper.orderByAsc(AgriculturePlanDetail::getRotationOrder);
        queryWrapper.orderByDesc(AgriculturePlanDetail::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增种植计划明细
     *
     * @param agriculturePlanDetail 种植计划明细
     * @return 结果
     */
    @Override
    public int insertAgriculturePlanDetail(AgriculturePlanDetail agriculturePlanDetail)
    {
        return save(agriculturePlanDetail) ? 1 : 0;
    }

    /**
     * 修改种植计划明细
     *
     * @param agriculturePlanDetail 种植计划明细
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAgriculturePlanDetail(AgriculturePlanDetail agriculturePlanDetail)
    {
        // 获取修改前的明细信息，用于判断是否需要更新父计划日期
        AgriculturePlanDetail oldDetail = null;
        if (agriculturePlanDetail.getDetailId() != null) {
            oldDetail = getById(agriculturePlanDetail.getDetailId());
        }
        
        int result = updateById(agriculturePlanDetail) ? 1 : 0;
        
        // 如果实际日期发生变化，更新相关计划的实际日期
        if (result > 0 && agriculturePlanDetail.getDetailId() != null) {
            boolean needUpdate = false;
            if (oldDetail != null) {
                // 检查实际日期是否发生变化
                if ((oldDetail.getActualStartDate() == null && agriculturePlanDetail.getActualStartDate() != null) ||
                    (oldDetail.getActualStartDate() != null && agriculturePlanDetail.getActualStartDate() == null) ||
                    (oldDetail.getActualStartDate() != null && agriculturePlanDetail.getActualStartDate() != null 
                        && !oldDetail.getActualStartDate().equals(agriculturePlanDetail.getActualStartDate())) ||
                    (oldDetail.getActualEndDate() == null && agriculturePlanDetail.getActualEndDate() != null) ||
                    (oldDetail.getActualEndDate() != null && agriculturePlanDetail.getActualEndDate() == null) ||
                    (oldDetail.getActualEndDate() != null && agriculturePlanDetail.getActualEndDate() != null 
                        && !oldDetail.getActualEndDate().equals(agriculturePlanDetail.getActualEndDate()))) {
                    needUpdate = true;
                }
            } else {
                // 新增明细，如果有实际日期，需要更新
                if (agriculturePlanDetail.getActualStartDate() != null || agriculturePlanDetail.getActualEndDate() != null) {
                    needUpdate = true;
                }
            }
            
            if (needUpdate) {
                try {
                    // 更新明细的实际日期（基于关联批次的任务）
                    planDateUpdateService.updatePlanDetailActualDates(agriculturePlanDetail.getDetailId());
                } catch (Exception e) {
                    log.error("更新轮作计划明细实际日期失败，明细ID: {}", agriculturePlanDetail.getDetailId(), e);
                }
            }
        }
        
        return result;
    }

    /**
     * 批量删除种植计划明细
     *
     * @param detailIds 需要删除的种植计划明细主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgriculturePlanDetailByDetailIds(Long[] detailIds)
    {
        // 删除前，先获取所有要删除的明细信息，用于校验和更新父计划日期
        List<AgriculturePlanDetail> detailsToDelete = listByIds(Arrays.asList(detailIds));
        
        // 删除前校验：检查是否有关联批次
        if (detailsToDelete != null) {
            for (AgriculturePlanDetail detail : detailsToDelete) {
                checkPlanDetailHasBatches(detail);
            }
        }
        
        int result = removeByIds(Arrays.asList(detailIds)) ? detailIds.length : 0;
        
        // 删除后，更新相关轮作计划日期
        if (result > 0 && detailsToDelete != null) {
            // 收集所有需要更新的轮作计划ID（去重）
            detailsToDelete.stream()
                    .filter(detail -> detail.getPlanId() != null)
                    .map(AgriculturePlanDetail::getPlanId)
                    .distinct()
                    .forEach(planId -> {
                        try {
                            planDateUpdateService.updateRotationPlanActualDates(planId);
                        } catch (Exception e) {
                            log.error("更新轮作计划实际日期失败，计划ID: {}", planId, e);
                        }
                    });
        }
        
        return result;
    }

    /**
     * 删除种植计划明细信息
     *
     * @param detailId 种植计划明细主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgriculturePlanDetailByDetailId(Long detailId)
    {
        // 删除前，先获取明细信息，用于校验和更新父计划日期
        AgriculturePlanDetail detail = getById(detailId);
        if (detail == null) {
            throw new RuntimeException("轮作计划明细不存在，ID: " + detailId);
        }
        
        // 删除前校验：检查是否有关联批次
        checkPlanDetailHasBatches(detail);
        
        Long planId = detail.getPlanId();
        int result = removeById(detailId) ? 1 : 0;
        
        // 删除后，更新相关轮作计划日期
        if (result > 0 && planId != null) {
            try {
                planDateUpdateService.updateRotationPlanActualDates(planId);
            } catch (Exception e) {
                log.error("更新轮作计划实际日期失败，计划ID: {}", planId, e);
            }
        }
        
        return result;
    }

    /**
     * 检查轮作计划明细是否有关联批次
     * 如果有关联批次，抛出异常，不允许删除
     *
     * @param detail 轮作计划明细
     * @throws ServiceException 如果明细有关联批次
     */
    private void checkPlanDetailHasBatches(AgriculturePlanDetail detail) {
        if (detail == null) {
            return;
        }

        // 轮作计划明细：通过 detail_id 直接关联批次
        LambdaQueryWrapper<AgricultureCropBatch> batchQuery = new LambdaQueryWrapper<>();
        batchQuery.eq(AgricultureCropBatch::getDetailId, detail.getDetailId())
                .and(wrapper -> wrapper.eq(AgricultureCropBatch::getDelFlag, "0")
                        .or()
                        .isNull(AgricultureCropBatch::getDelFlag));
        long batchCount = agricultureCropBatchService.count(batchQuery);
        if (batchCount > 0) {
            throw new ServiceException(
                    String.format("轮作计划明细【明细ID:%d】有关联批次，无法删除", detail.getDetailId()));
        }
    }
}

