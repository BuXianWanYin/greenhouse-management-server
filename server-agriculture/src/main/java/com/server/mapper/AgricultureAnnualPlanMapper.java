package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureAnnualPlan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 年度种植规划Mapper接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Repository
@Mapper
public interface AgricultureAnnualPlanMapper extends BaseMapper<AgricultureAnnualPlan>
{

}

