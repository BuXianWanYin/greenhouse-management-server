package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureGrowthNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 生长关键节点Mapper接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
@Repository
@Mapper
public interface AgricultureGrowthNodeMapper extends BaseMapper<AgricultureGrowthNode>
{

}

