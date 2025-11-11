package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureTaskLogMapper;
import com.server.domain.AgricultureTaskLog;
import com.server.service.AgricultureTaskLogService;

/**
 * 批次任务日志Service业务层处理
 * 
 * @author server
 * @date 2025-06-11
 */
@Service
public class AgricultureTaskLogServiceImpl extends ServiceImpl<AgricultureTaskLogMapper,AgricultureTaskLog> implements AgricultureTaskLogService
{
    @Autowired
    private AgricultureTaskLogMapper agricultureTaskLogMapper;

    /**
     * 查询批次任务日志
     * 
     * @param logId 批次任务日志主键
     * @return 批次任务日志
     */
    @Override
    public AgricultureTaskLog selectAgricultureTaskLogByLogId(String logId)
    {
        return agricultureTaskLogMapper.selectById(logId);
    }

    /**
     * 查询批次任务日志列表
     * 
     * @param agricultureTaskLog 批次任务日志
     * @return 批次任务日志
     */
    @Override
    public List<AgricultureTaskLog> selectAgricultureTaskLogList(AgricultureTaskLog agricultureTaskLog)
    {
        LambdaQueryWrapper<AgricultureTaskLog> lambdaQueryWrapper = new QueryWrapper<AgricultureTaskLog>().lambda();
        // 如果传入了taskId，则按taskId查询
        if (agricultureTaskLog != null && agricultureTaskLog.getTaskId() != null) {
            lambdaQueryWrapper.eq(AgricultureTaskLog::getTaskId, agricultureTaskLog.getTaskId());
        }
        // 过滤已删除的记录
        lambdaQueryWrapper.eq(AgricultureTaskLog::getDelFlag, "0");
        // 按创建时间倒序排列，最新的日志在前面
        lambdaQueryWrapper.orderByDesc(AgricultureTaskLog::getCreateTime);
        return agricultureTaskLogMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增批次任务日志
     * 
     * @param agricultureTaskLog 批次任务日志
     * @return 结果
     */
    @Override
    public int insertAgricultureTaskLog(AgricultureTaskLog agricultureTaskLog)
    {
        return agricultureTaskLogMapper.insert(agricultureTaskLog);
    }

    /**
     *
     * 修改批次任务日志
     * 
     * @param agricultureTaskLog 批次任务日志
     * @return 结果
     */
    @Override
    public int updateAgricultureTaskLog(AgricultureTaskLog agricultureTaskLog)
    {
        return agricultureTaskLogMapper.updateById(agricultureTaskLog);
    }

    /**
     * 批量删除批次任务日志
     * 
     * @param logIds 需要删除的批次任务日志主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureTaskLogByLogIds(String[] logIds)
    {
        return agricultureTaskLogMapper.deleteById(logIds);
    }

    /**
     * 删除批次任务日志信息
     * 
     * @param logId 批次任务日志主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureTaskLogByLogId(String logId)
    {
        return agricultureTaskLogMapper.deleteById(logId);
    }
}
