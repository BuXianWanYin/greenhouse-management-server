package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureAnnualPlan;
import com.server.mapper.AgricultureAnnualPlanMapper;
import com.server.service.AgricultureAnnualPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 年度种植规划Service业务层处理
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Service
public class AgricultureAnnualPlanServiceImpl extends ServiceImpl<AgricultureAnnualPlanMapper, AgricultureAnnualPlan> implements AgricultureAnnualPlanService
{
    @Autowired
    private AgricultureAnnualPlanMapper agricultureAnnualPlanMapper;

    /**
     * 查询年度种植规划
     *
     * @param planId 年度种植规划主键
     * @return 年度种植规划
     */
    @Override
    public AgricultureAnnualPlan selectAgricultureAnnualPlanByPlanId(Long planId)
    {
        return getById(planId);
    }

    /**
     * 查询年度种植规划列表
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 年度种植规划
     */
    @Override
    public List<AgricultureAnnualPlan> selectAgricultureAnnualPlanList(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        LambdaQueryWrapper<AgricultureAnnualPlan> queryWrapper = new LambdaQueryWrapper<>();
        if (agricultureAnnualPlan.getPlanYear() != null) {
            queryWrapper.eq(AgricultureAnnualPlan::getPlanYear, agricultureAnnualPlan.getPlanYear());
        }
        if (agricultureAnnualPlan.getPastureId() != null) {
            queryWrapper.eq(AgricultureAnnualPlan::getPastureId, agricultureAnnualPlan.getPastureId());
        }
        if (agricultureAnnualPlan.getPlanStatus() != null) {
            queryWrapper.eq(AgricultureAnnualPlan::getPlanStatus, agricultureAnnualPlan.getPlanStatus());
        }
        if (agricultureAnnualPlan.getPlanName() != null) {
            queryWrapper.like(AgricultureAnnualPlan::getPlanName, agricultureAnnualPlan.getPlanName());
        }
        queryWrapper.eq(AgricultureAnnualPlan::getDelFlag, "0");
        queryWrapper.orderByDesc(AgricultureAnnualPlan::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增年度种植规划
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 结果
     */
    @Override
    public int insertAgricultureAnnualPlan(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        return save(agricultureAnnualPlan) ? 1 : 0;
    }

    /**
     * 修改年度种植规划
     *
     * @param agricultureAnnualPlan 年度种植规划
     * @return 结果
     */
    @Override
    public int updateAgricultureAnnualPlan(AgricultureAnnualPlan agricultureAnnualPlan)
    {
        return updateById(agricultureAnnualPlan) ? 1 : 0;
    }

    /**
     * 批量删除年度种植规划
     *
     * @param planIds 需要删除的年度种植规划主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAnnualPlanByPlanIds(Long[] planIds)
    {
        return removeByIds(Arrays.asList(planIds)) ? planIds.length : 0;
    }

    /**
     * 删除年度种植规划信息
     *
     * @param planId 年度种植规划主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAnnualPlanByPlanId(Long planId)
    {
        return removeById(planId) ? 1 : 0;
    }
}

