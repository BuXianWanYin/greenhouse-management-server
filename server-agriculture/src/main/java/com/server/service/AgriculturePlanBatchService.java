package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgriculturePlanBatch;

/**
 * 年度计划批次关联Service接口
 *
 * @author bxwy
 * @date 2025-11-07
 */
public interface AgriculturePlanBatchService extends IService<AgriculturePlanBatch>
{
    /**
     * 查询年度计划批次关联
     *
     * @param id 年度计划批次关联主键
     * @return 年度计划批次关联
     */
    public AgriculturePlanBatch selectAgriculturePlanBatchById(Long id);

    /**
     * 查询年度计划批次关联列表
     *
     * @param agriculturePlanBatch 年度计划批次关联
     * @return 年度计划批次关联集合
     */
    public List<AgriculturePlanBatch> selectAgriculturePlanBatchList(AgriculturePlanBatch agriculturePlanBatch);

    /**
     * 新增年度计划批次关联
     *
     * @param agriculturePlanBatch 年度计划批次关联
     * @return 结果
     */
    public int insertAgriculturePlanBatch(AgriculturePlanBatch agriculturePlanBatch);

    /**
     * 修改年度计划批次关联
     *
     * @param agriculturePlanBatch 年度计划批次关联
     * @return 结果
     */
    public int updateAgriculturePlanBatch(AgriculturePlanBatch agriculturePlanBatch);

    /**
     * 批量删除年度计划批次关联
     *
     * @param ids 需要删除的年度计划批次关联主键集合
     * @return 结果
     */
    public int deleteAgriculturePlanBatchByIds(Long[] ids);

    /**
     * 删除年度计划批次关联信息
     *
     * @param id 年度计划批次关联主键
     * @return 结果
     */
    public int deleteAgriculturePlanBatchById(Long id);
}

