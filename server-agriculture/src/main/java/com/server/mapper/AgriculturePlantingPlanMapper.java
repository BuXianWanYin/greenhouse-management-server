package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgriculturePlantingPlan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 种植计划Mapper接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Repository
@Mapper
public interface AgriculturePlantingPlanMapper extends BaseMapper<AgriculturePlantingPlan>
{

}

