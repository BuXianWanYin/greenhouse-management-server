package com.server.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.domain.dto.AgricultureCropBatchDTO;
import com.server.service.AgricultureBatchTaskService;
import com.server.service.AgricultureJobService;
import com.server.service.AgricultureTaskLogService;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.service.AgricultureCropBatchService;

/**
 * 种植批次Service业务层处理
 *
 * @author bxwy
 * @date 2025-09-28
 */
@Service
public class AgricultureCropBatchServiceImpl extends ServiceImpl<AgricultureCropBatchMapper, AgricultureCropBatch> implements AgricultureCropBatchService
{
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;
    @Autowired
    private AgricultureJobService agricultureJobService;
    @Autowired
    private AgricultureBatchTaskService agricultureBatchTaskService;
    @Autowired
    private AgricultureTaskLogService agricultureTaskLogService;

    /**
     * 查询种植批次
     *
     * @param batchId 批次主键
     * @return 种植批次
     */
    @Override
    public AgricultureCropBatch selectAgricultureCropBatchByBatchId(Long batchId)
    {
        return getById(batchId);
    }

    /**
     * 根据温室ID查询批次列表
     *
     * @param pastureId 温室ID
     * @return 批次列表
     */
    @Override
    public List<AgricultureCropBatchDTO> selectBatchByPastureId(Long pastureId) {
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgricultureCropBatch::getPastureId, pastureId);
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }

    /**
     * 导出批次列表
     *
     * @param agricultureCropBatch 种植批次
     * @return 批次列表
     */
    @Override
    public List<AgricultureCropBatch> selectAgricultureCropBatchList(AgricultureCropBatch agricultureCropBatch)
    {
        return list();
    }

    /**
     * 新增种植批次
     *
     * @param agricultureCropBatch 种植批次
     * @return 结果
     */
    @Override
    public int insertAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {

        // 先插入批次，以获取自增主键
        int result = agricultureCropBatchMapper.insert(agricultureCropBatch);
        // 成功插入后，agricultureCropBatch.getBatchId() 将会持有数据库生成的ID
        if (result > 0) {
            // 查询种质的标准作业
            List<AgricultureJob> jobList = null;
            if (agricultureCropBatch.getClassId() != null) {
                AgricultureJob job = new AgricultureJob();
                job.setClassId(agricultureCropBatch.getClassId());
                jobList = agricultureJobService.selectAgricultureJobList(job);
            }

            // 去重（如果有必要，可以根据jobId去重）
            if (jobList != null) {
                jobList = jobList.stream().distinct().collect(Collectors.toList());
            }

            // 遍历插入任务
            if (jobList != null) {
                for (AgricultureJob sj : jobList) {
                    AgricultureBatchTask agricultureBatchTask = new AgricultureBatchTask();
                    //设置批次任务的相关信息
                    agricultureBatchTask.setBatchId(agricultureCropBatch.getBatchId());
                    agricultureBatchTask.setResponsiblePersonId(agricultureCropBatch.getResponsiblePersonId());
                    agricultureBatchTask.setTaskName(sj.getJobName());
                    // 根据周期单位设置乘数（0:天, 1:周）
                    int mult = sj.getCycleUnit().equals("0")? 1 : 7;
                    LocalDateTime startTime = agricultureCropBatch.getStartTime();
                    // 如果批次开始时间为空，则默认为当前时间
                    if (startTime == null) {
                        startTime = LocalDateTime.now();
                    }
                    // 计算计划开始时间
                    LocalDateTime planStartDateTime = startTime.plusDays(sj.getJobStart() * mult);
                    agricultureBatchTask.setPlanStart(Date.from(planStartDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    // 计算计划完成时间
                    LocalDateTime planFinishDateTime = startTime.plusDays(sj.getJobFinish() * mult);
                    agricultureBatchTask.setPlanFinish(Date.from(planFinishDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    // 插入批次任务到数据库
                    agricultureBatchTaskService.insertBatchTask(agricultureBatchTask);
                    //创建任务日志
                    AgricultureTaskLog agricultureTaskLog = new AgricultureTaskLog();
                    agricultureTaskLog.setLogId(agricultureBatchTask.getTaskId());
                    agricultureTaskLog.setOperDes("创建任务");  // 操作描述
                    // 插入任务日志到数据库
                    agricultureTaskLogService.insertAgricultureTaskLog(agricultureTaskLog);
                }
            }
        }
        // 返回插入种植批次的结果
        return result;
    }

    /**
     * 修改种植批次
     *
     * @param agricultureCropBatch 种植批次
     * @return 结果
     */
    @Override
    public int updateAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        return updateById(agricultureCropBatch) ? 1 : 0;
    }

    /**
     * 批量删除种植批次
     * 删除的同时将批次下的任务和任务日志一起删掉，避免数据库过多不用的数据
     * @param batchId 需要删除的批次主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgricultureCropBatchByBatchIds(Long[] batchId)
    {
        // 1. 查询批次下的所有任务
        LambdaQueryWrapper<AgricultureBatchTask> taskQueryWrapper = new LambdaQueryWrapper<>();
        taskQueryWrapper.in(AgricultureBatchTask::getBatchId, Arrays.asList(batchId));
        List<AgricultureBatchTask> batchTaskList = agricultureBatchTaskService.list(taskQueryWrapper);
        
        // 2. 提取任务ID列表
        List<Long> taskIds = batchTaskList.stream()
                .map(AgricultureBatchTask::getTaskId)
                .collect(Collectors.toList());
        
        // 3. 删除任务日志（通过taskId）
        if (!taskIds.isEmpty()) {
            LambdaQueryWrapper<AgricultureTaskLog> logQueryWrapper = new LambdaQueryWrapper<>();
            logQueryWrapper.in(AgricultureTaskLog::getTaskId, taskIds);
            agricultureTaskLogService.remove(logQueryWrapper);
        }
        
        // 4. 删除批次任务
        if (!batchTaskList.isEmpty()) {
            agricultureBatchTaskService.remove(taskQueryWrapper);
        }
        
        // 5. 删除批次
        return removeByIds(Arrays.asList(batchId)) ? batchId.length : 0;
    }

    /**
     * 删除种植批次信息
     * 删除的同时将批次下的任务和任务日志一起删掉，避免数据库过多不用的数据
     *
     * @param batchId 批次主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgricultureCropBatchByBatchId(Long batchId)
    {
        // 1. 查询批次下的所有任务
        LambdaQueryWrapper<AgricultureBatchTask> taskQueryWrapper = new LambdaQueryWrapper<>();
        taskQueryWrapper.eq(AgricultureBatchTask::getBatchId, batchId);
        List<AgricultureBatchTask> batchTaskList = agricultureBatchTaskService.list(taskQueryWrapper);
        
        // 2. 提取任务ID列表
        List<Long> taskIds = batchTaskList.stream()
                .map(AgricultureBatchTask::getTaskId)
                .collect(Collectors.toList());
        
        // 3. 删除任务日志（通过taskId）
        if (!taskIds.isEmpty()) {
            LambdaQueryWrapper<AgricultureTaskLog> logQueryWrapper = new LambdaQueryWrapper<>();
            logQueryWrapper.in(AgricultureTaskLog::getTaskId, taskIds);
            agricultureTaskLogService.remove(logQueryWrapper);
        }
        
        // 4. 删除批次任务
        if (!batchTaskList.isEmpty()) {
            agricultureBatchTaskService.remove(taskQueryWrapper);
        }
        
        // 5. 删除批次
        return removeById(batchId) ? 1 : 0;
    }

    /**
     * 根据条件查询作物批次信息，并包含相关的分类图片
     *
     * @param agricultureCropBatchDTO 包含查询条件的DTO，如批次名称batchName
     * @return 符合条件的作物批次DTO列表
     */
    @Override
    public List<AgricultureCropBatchDTO> getCropBatchWithClassImages(AgricultureCropBatchDTO agricultureCropBatchDTO) {
        // 创建LambdaQueryWrapper，用于构建查询条件，基于数据库实体类AgricultureCropBatch
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        // 如果DTO中的批次名称不为空，则添加批次名称的模糊查询条件
        if (StringUtils.isNotEmpty(agricultureCropBatchDTO.getBatchName())) {
            queryWrapper.like(AgricultureCropBatch::getBatchName, agricultureCropBatchDTO.getBatchName());
        }
        if (agricultureCropBatchDTO.getClassId() != null) {
            queryWrapper.eq(AgricultureCropBatch::getClassId, agricultureCropBatchDTO.getClassId());
        }
        // 调用Mapper方法执行查询，并将Wrapper作为参数传递，让Mapper根据条件拼接SQL
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }
}
