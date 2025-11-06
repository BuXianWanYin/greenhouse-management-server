package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureGrowthStage;

/**
 * 生长阶段Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgricultureGrowthStageService extends IService<AgricultureGrowthStage>
{
    /**
     * 查询生长阶段
     *
     * @param stageId 生长阶段主键
     * @return 生长阶段
     */
    public AgricultureGrowthStage selectAgricultureGrowthStageByStageId(Long stageId);

    /**
     * 查询生长阶段列表
     *
     * @param agricultureGrowthStage 生长阶段
     * @return 生长阶段集合
     */
    public List<AgricultureGrowthStage> selectAgricultureGrowthStageList(AgricultureGrowthStage agricultureGrowthStage);

    /**
     * 新增生长阶段
     *
     * @param agricultureGrowthStage 生长阶段
     * @return 结果
     */
    public int insertAgricultureGrowthStage(AgricultureGrowthStage agricultureGrowthStage);

    /**
     * 修改生长阶段
     *
     * @param agricultureGrowthStage 生长阶段
     * @return 结果
     */
    public int updateAgricultureGrowthStage(AgricultureGrowthStage agricultureGrowthStage);

    /**
     * 批量删除生长阶段
     *
     * @param stageIds 需要删除的生长阶段主键集合
     * @return 结果
     */
    public int deleteAgricultureGrowthStageByStageIds(Long[] stageIds);

    /**
     * 删除生长阶段信息
     *
     * @param stageId 生长阶段主键
     * @return 结果
     */
    public int deleteAgricultureGrowthStageByStageId(Long stageId);
}

