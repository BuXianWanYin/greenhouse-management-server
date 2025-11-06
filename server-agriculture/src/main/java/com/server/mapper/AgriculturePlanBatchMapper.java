package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgriculturePlanBatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 年度计划批次关联Mapper接口
 *
 * @author bxwy
 * @date 2025-11-07
 */
@Repository
@Mapper
public interface AgriculturePlanBatchMapper extends BaseMapper<AgriculturePlanBatch>
{

}

