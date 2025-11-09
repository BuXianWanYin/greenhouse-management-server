package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureTaskEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 批次任务工人Mapper接口
 * 
 * @author server
 * @date 2025-06-10
 */
@Mapper
public interface AgricultureTaskEmployeeMapper extends BaseMapper<AgricultureTaskEmployee> {

    @Select("SELECT te.* , u.nick_name as user_name FROM agriculture_task_employee te LEFT JOIN sys_user u ON te.user_id = u.user_id WHERE te.task_id = #{taskId}")
    List<AgricultureTaskEmployee> selectGYID(@Param("taskId") Long taskId);
}
