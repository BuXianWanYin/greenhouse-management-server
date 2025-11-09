package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgriculturePlanDetail;

/**
 * 种植计划明细Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgriculturePlanDetailService extends IService<AgriculturePlanDetail>
{
    /**
     * 查询种植计划明细
     *
     * @param detailId 种植计划明细主键
     * @return 种植计划明细
     */
    public AgriculturePlanDetail selectAgriculturePlanDetailByDetailId(Long detailId);

    /**
     * 查询种植计划明细列表
     *
     * @param agriculturePlanDetail 种植计划明细
     * @return 种植计划明细集合
     */
    public List<AgriculturePlanDetail> selectAgriculturePlanDetailList(AgriculturePlanDetail agriculturePlanDetail);

    /**
     * 新增种植计划明细
     *
     * @param agriculturePlanDetail 种植计划明细
     * @return 结果
     */
    public int insertAgriculturePlanDetail(AgriculturePlanDetail agriculturePlanDetail);

    /**
     * 修改种植计划明细
     *
     * @param agriculturePlanDetail 种植计划明细
     * @return 结果
     */
    public int updateAgriculturePlanDetail(AgriculturePlanDetail agriculturePlanDetail);

    /**
     * 批量删除种植计划明细
     *
     * @param detailIds 需要删除的种植计划明细主键集合
     * @return 结果
     */
    public int deleteAgriculturePlanDetailByDetailIds(Long[] detailIds);

    /**
     * 删除种植计划明细信息
     *
     * @param detailId 种植计划明细主键
     * @return 结果
     */
    public int deleteAgriculturePlanDetailByDetailId(Long detailId);
}

