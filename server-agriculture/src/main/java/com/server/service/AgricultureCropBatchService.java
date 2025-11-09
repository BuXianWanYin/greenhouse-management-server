package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCropBatch;

/**
 * 种植批次Service接口
 *
 * @author bxwy
 * @date 2025-05-28
 */
public interface AgricultureCropBatchService extends IService<AgricultureCropBatch>
{
    /**
     * 查询种植批次
     *
     * @param batchId 批次主键
     * @return 种植批次
     */
    public AgricultureCropBatch selectAgricultureCropBatchByBatchId(Long batchId);

    /**
     * 根据温室ID查询批次列表
     *
     * @param pastureId 温室ID
     * @return 批次列表
     */
    List<AgricultureCropBatch> selectBatchByPastureId(Long pastureId);

    /**
     * 导出批次列表
     *
     * @param agricultureCropBatch 种植批次
     * @return 批次集合
     */
    public List<AgricultureCropBatch> selectAgricultureCropBatchList(AgricultureCropBatch agricultureCropBatch);

    /**
     * 新增种植批次
     *
     * @param agricultureCropBatch 种植批次
     * @return 结果
     */
    public int insertAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch);

    /**
     * 修改种植批次
     *
     * @param agricultureCropBatch 种植批次
     * @return 结果
     */
    public int updateAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch);

    /**
     * 批量删除种植批次
     *
     * @param batchId 需要删除的批次主键集合
     * @return 结果
     */
    public int deleteAgricultureCropBatchByBatchIds(Long[] batchId);

    /**
     * 删除种植批次信息
     *
     * @param batchId 批次主键
     * @return 结果
     */
    public int deleteAgricultureCropBatchByBatchId(Long batchId);

    /**
     * 根据条件查询作物批次信息，并包含相关的分类图片
     * @param agricultureCropBatch 查询条件
     * @return 符合条件的作物批次列表
     */
    List<AgricultureCropBatch> getCropBatchWithClassImages(AgricultureCropBatch agricultureCropBatch);
}
