package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureRotationDetail;
import com.server.mapper.AgricultureRotationDetailMapper;
import com.server.service.AgricultureRotationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 轮作计划明细Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgricultureRotationDetailServiceImpl extends ServiceImpl<AgricultureRotationDetailMapper, AgricultureRotationDetail> implements AgricultureRotationDetailService
{
    @Autowired
    private AgricultureRotationDetailMapper agricultureRotationDetailMapper;

    /**
     * 查询轮作计划明细
     *
     * @param detailId 轮作计划明细主键
     * @return 轮作计划明细
     */
    @Override
    public AgricultureRotationDetail selectAgricultureRotationDetailByDetailId(Long detailId)
    {
        return getById(detailId);
    }

    /**
     * 查询轮作计划明细列表
     *
     * @param agricultureRotationDetail 轮作计划明细
     * @return 轮作计划明细
     */
    @Override
    public List<AgricultureRotationDetail> selectAgricultureRotationDetailList(AgricultureRotationDetail agricultureRotationDetail)
    {
        LambdaQueryWrapper<AgricultureRotationDetail> queryWrapper = new LambdaQueryWrapper<>();
        if (agricultureRotationDetail.getRotationId() != null) {
            queryWrapper.eq(AgricultureRotationDetail::getRotationId, agricultureRotationDetail.getRotationId());
        }
        if (agricultureRotationDetail.getClassId() != null) {
            queryWrapper.eq(AgricultureRotationDetail::getClassId, agricultureRotationDetail.getClassId());
        }
        if (agricultureRotationDetail.getSeasonType() != null) {
            queryWrapper.eq(AgricultureRotationDetail::getSeasonType, agricultureRotationDetail.getSeasonType());
        }
        queryWrapper.orderByAsc(AgricultureRotationDetail::getRotationOrder);
        queryWrapper.orderByDesc(AgricultureRotationDetail::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增轮作计划明细
     *
     * @param agricultureRotationDetail 轮作计划明细
     * @return 结果
     */
    @Override
    public int insertAgricultureRotationDetail(AgricultureRotationDetail agricultureRotationDetail)
    {
        return save(agricultureRotationDetail) ? 1 : 0;
    }

    /**
     * 修改轮作计划明细
     *
     * @param agricultureRotationDetail 轮作计划明细
     * @return 结果
     */
    @Override
    public int updateAgricultureRotationDetail(AgricultureRotationDetail agricultureRotationDetail)
    {
        return updateById(agricultureRotationDetail) ? 1 : 0;
    }

    /**
     * 批量删除轮作计划明细
     *
     * @param detailIds 需要删除的轮作计划明细主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureRotationDetailByDetailIds(Long[] detailIds)
    {
        return removeByIds(Arrays.asList(detailIds)) ? detailIds.length : 0;
    }

    /**
     * 删除轮作计划明细信息
     *
     * @param detailId 轮作计划明细主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureRotationDetailByDetailId(Long detailId)
    {
        return removeById(detailId) ? 1 : 0;
    }
}

