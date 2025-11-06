package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureGrowthStage;
import com.server.mapper.AgricultureGrowthStageMapper;
import com.server.service.AgricultureGrowthStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生长阶段Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgricultureGrowthStageServiceImpl extends ServiceImpl<AgricultureGrowthStageMapper, AgricultureGrowthStage> implements AgricultureGrowthStageService
{
    @Autowired
    private AgricultureGrowthStageMapper agricultureGrowthStageMapper;

    /**
     * 查询生长阶段
     *
     * @param stageId 生长阶段主键
     * @return 生长阶段
     */
    @Override
    public AgricultureGrowthStage selectAgricultureGrowthStageByStageId(Long stageId)
    {
        return getById(stageId);
    }

    /**
     * 查询生长阶段列表
     *
     * @param agricultureGrowthStage 生长阶段
     * @return 生长阶段
     */
    @Override
    public List<AgricultureGrowthStage> selectAgricultureGrowthStageList(AgricultureGrowthStage agricultureGrowthStage)
    {
        LambdaQueryWrapper<AgricultureGrowthStage> queryWrapper = new LambdaQueryWrapper<>();
        if (agricultureGrowthStage.getBatchId() != null) {
            queryWrapper.eq(AgricultureGrowthStage::getBatchId, agricultureGrowthStage.getBatchId());
        }
        if (agricultureGrowthStage.getStageType() != null) {
            queryWrapper.eq(AgricultureGrowthStage::getStageType, agricultureGrowthStage.getStageType());
        }
        if (agricultureGrowthStage.getStageStatus() != null) {
            queryWrapper.eq(AgricultureGrowthStage::getStageStatus, agricultureGrowthStage.getStageStatus());
        }
        queryWrapper.orderByAsc(AgricultureGrowthStage::getStageOrder);
        queryWrapper.orderByDesc(AgricultureGrowthStage::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增生长阶段
     *
     * @param agricultureGrowthStage 生长阶段
     * @return 结果
     */
    @Override
    public int insertAgricultureGrowthStage(AgricultureGrowthStage agricultureGrowthStage)
    {
        return save(agricultureGrowthStage) ? 1 : 0;
    }

    /**
     * 修改生长阶段
     *
     * @param agricultureGrowthStage 生长阶段
     * @return 结果
     */
    @Override
    public int updateAgricultureGrowthStage(AgricultureGrowthStage agricultureGrowthStage)
    {
        return updateById(agricultureGrowthStage) ? 1 : 0;
    }

    /**
     * 批量删除生长阶段
     *
     * @param stageIds 需要删除的生长阶段主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureGrowthStageByStageIds(Long[] stageIds)
    {
        return removeByIds(Arrays.asList(stageIds)) ? stageIds.length : 0;
    }

    /**
     * 删除生长阶段信息
     *
     * @param stageId 生长阶段主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureGrowthStageByStageId(Long stageId)
    {
        return removeById(stageId) ? 1 : 0;
    }
}

