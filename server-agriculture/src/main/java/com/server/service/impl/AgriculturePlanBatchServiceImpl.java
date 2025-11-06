package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgriculturePlanBatch;
import com.server.mapper.AgriculturePlanBatchMapper;
import com.server.service.AgriculturePlanBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 年度计划批次关联Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgriculturePlanBatchServiceImpl extends ServiceImpl<AgriculturePlanBatchMapper, AgriculturePlanBatch> implements AgriculturePlanBatchService
{
    @Autowired
    private AgriculturePlanBatchMapper agriculturePlanBatchMapper;

    /**
     * 查询年度计划批次关联
     *
     * @param id 年度计划批次关联主键
     * @return 年度计划批次关联
     */
    @Override
    public AgriculturePlanBatch selectAgriculturePlanBatchById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询年度计划批次关联列表
     *
     * @param agriculturePlanBatch 年度计划批次关联
     * @return 年度计划批次关联
     */
    @Override
    public List<AgriculturePlanBatch> selectAgriculturePlanBatchList(AgriculturePlanBatch agriculturePlanBatch)
    {
        LambdaQueryWrapper<AgriculturePlanBatch> queryWrapper = new LambdaQueryWrapper<>();
        if (agriculturePlanBatch.getPlanId() != null) {
            queryWrapper.eq(AgriculturePlanBatch::getPlanId, agriculturePlanBatch.getPlanId());
        }
        if (agriculturePlanBatch.getBatchId() != null) {
            queryWrapper.eq(AgriculturePlanBatch::getBatchId, agriculturePlanBatch.getBatchId());
        }
        queryWrapper.orderByDesc(AgriculturePlanBatch::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增年度计划批次关联
     *
     * @param agriculturePlanBatch 年度计划批次关联
     * @return 结果
     */
    @Override
    public int insertAgriculturePlanBatch(AgriculturePlanBatch agriculturePlanBatch)
    {
        return save(agriculturePlanBatch) ? 1 : 0;
    }

    /**
     * 修改年度计划批次关联
     *
     * @param agriculturePlanBatch 年度计划批次关联
     * @return 结果
     */
    @Override
    public int updateAgriculturePlanBatch(AgriculturePlanBatch agriculturePlanBatch)
    {
        return updateById(agriculturePlanBatch) ? 1 : 0;
    }

    /**
     * 批量删除年度计划批次关联
     *
     * @param ids 需要删除的年度计划批次关联主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePlanBatchByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除年度计划批次关联信息
     *
     * @param id 年度计划批次关联主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePlanBatchById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}

