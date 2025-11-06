package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureRotationPlan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 轮作计划Mapper接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Repository
@Mapper
public interface AgricultureRotationPlanMapper extends BaseMapper<AgricultureRotationPlan>
{

}

