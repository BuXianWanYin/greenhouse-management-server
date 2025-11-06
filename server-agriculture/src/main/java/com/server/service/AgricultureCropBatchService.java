package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.dto.AgricultureCropBatchDTO;

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
    List<AgricultureCropBatchDTO> selectBatchByPastureId(Long pastureId);

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
     * 链表返回
     * @return
     */
    List<AgricultureCropBatchDTO> getCropBatchWithClassImages(AgricultureCropBatchDTO agricultureCropBatchDTO);
}
