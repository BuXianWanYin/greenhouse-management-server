package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureGrowthStage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 生长阶段Mapper接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Repository
@Mapper
public interface AgricultureGrowthStageMapper extends BaseMapper<AgricultureGrowthStage>
{

}

