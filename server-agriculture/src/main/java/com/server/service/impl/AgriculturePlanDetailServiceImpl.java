package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgriculturePlanDetail;
import com.server.mapper.AgriculturePlanDetailMapper;
import com.server.service.AgriculturePlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 种植计划明细Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgriculturePlanDetailServiceImpl extends ServiceImpl<AgriculturePlanDetailMapper, AgriculturePlanDetail> implements AgriculturePlanDetailService
{
    @Autowired
    private AgriculturePlanDetailMapper agriculturePlanDetailMapper;

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
    public int updateAgriculturePlanDetail(AgriculturePlanDetail agriculturePlanDetail)
    {
        return updateById(agriculturePlanDetail) ? 1 : 0;
    }

    /**
     * 批量删除种植计划明细
     *
     * @param detailIds 需要删除的种植计划明细主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePlanDetailByDetailIds(Long[] detailIds)
    {
        return removeByIds(Arrays.asList(detailIds)) ? detailIds.length : 0;
    }

    /**
     * 删除种植计划明细信息
     *
     * @param detailId 种植计划明细主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePlanDetailByDetailId(Long detailId)
    {
        return removeById(detailId) ? 1 : 0;
    }
}

