package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureRotationDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 轮作计划明细Mapper接口
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Repository
@Mapper
public interface AgricultureRotationDetailMapper extends BaseMapper<AgricultureRotationDetail>
{

}

