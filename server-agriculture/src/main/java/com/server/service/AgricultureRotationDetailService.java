package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureRotationDetail;

/**
 * 轮作计划明细Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgricultureRotationDetailService extends IService<AgricultureRotationDetail>
{
    /**
     * 查询轮作计划明细
     *
     * @param detailId 轮作计划明细主键
     * @return 轮作计划明细
     */
    public AgricultureRotationDetail selectAgricultureRotationDetailByDetailId(Long detailId);

    /**
     * 查询轮作计划明细列表
     *
     * @param agricultureRotationDetail 轮作计划明细
     * @return 轮作计划明细集合
     */
    public List<AgricultureRotationDetail> selectAgricultureRotationDetailList(AgricultureRotationDetail agricultureRotationDetail);

    /**
     * 新增轮作计划明细
     *
     * @param agricultureRotationDetail 轮作计划明细
     * @return 结果
     */
    public int insertAgricultureRotationDetail(AgricultureRotationDetail agricultureRotationDetail);

    /**
     * 修改轮作计划明细
     *
     * @param agricultureRotationDetail 轮作计划明细
     * @return 结果
     */
    public int updateAgricultureRotationDetail(AgricultureRotationDetail agricultureRotationDetail);

    /**
     * 批量删除轮作计划明细
     *
     * @param detailIds 需要删除的轮作计划明细主键集合
     * @return 结果
     */
    public int deleteAgricultureRotationDetailByDetailIds(Long[] detailIds);

    /**
     * 删除轮作计划明细信息
     *
     * @param detailId 轮作计划明细主键
     * @return 结果
     */
    public int deleteAgricultureRotationDetailByDetailId(Long detailId);
}

