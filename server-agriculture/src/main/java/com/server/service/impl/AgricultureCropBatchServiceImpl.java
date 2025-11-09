package com.server.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.service.AgricultureBatchTaskService;
import com.server.service.AgricultureJobService;
import com.server.service.AgricultureTaskLogService;
import com.server.service.AgriculturePlantingPlanService;
import com.server.service.AgriculturePlanDetailService;
import com.server.exception.ServiceException;
import com.server.core.domain.entity.SysUser;
import com.server.mapper.SysUserMapper;
import com.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private AgriculturePlantingPlanService agriculturePlantingPlanService;
    @Autowired
    private AgriculturePlanDetailService agriculturePlanDetailService;

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
    public List<AgricultureCropBatch> selectBatchByPastureId(Long pastureId) {
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
    @Transactional(rollbackFor = Exception.class)
    public int insertAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        // 验证批次时间范围
        validateBatchTimeRange(agricultureCropBatch);
        
        // 清空前端可能传入的创建时间和更新时间，由 MyMetaObjectHandler 自动填充
        agricultureCropBatch.setCreateTime(null);
        agricultureCropBatch.setUpdateTime(null);
        // 先插入批次，以获取自增主键
        int result = agricultureCropBatchMapper.insert(agricultureCropBatch);
        // 成功插入后，agricultureCropBatch.getBatchId() 将会持有数据库生成的ID
        if (result > 0 && agricultureCropBatch.getClassId() != null) {
            // 根据批次所属的种质ID，查询该种质对应的标准作业流程
            AgricultureJob job = new AgricultureJob();
            job.setClassId(agricultureCropBatch.getClassId());
            List<AgricultureJob> jobList = agricultureJobService.selectAgricultureJobList(job);

            // 过滤掉停用的作业（status = "1"），只保留正常状态的作业（status = "0"）
            if (jobList != null && !jobList.isEmpty()) {
                jobList = jobList.stream()
                        .filter(j -> "0".equals(j.getStatus())) // 只保留正常状态的作业
                        .distinct() // 去重
                        .collect(Collectors.toList());

                // 遍历作业流程，为每个作业创建批次任务
                for (AgricultureJob sj : jobList) {
                    AgricultureBatchTask agricultureBatchTask = new AgricultureBatchTask();
                    //设置批次任务的相关信息
                    agricultureBatchTask.setBatchId(agricultureCropBatch.getBatchId());
                    agricultureBatchTask.setResponsiblePersonId(agricultureCropBatch.getResponsiblePersonId());
                    agricultureBatchTask.setTaskName(sj.getJobName());
                    
                    // 根据负责人ID查询负责人姓名（从系统用户表获取nickName，直接使用Mapper避免分页限制）
                    if (agricultureCropBatch.getResponsiblePersonId() != null) {
                        // 直接使用Mapper查询，避免Service层的分页限制
                        SysUser user = sysUserMapper.selectUserById(agricultureCropBatch.getResponsiblePersonId());
                        if (user != null && user.getNickName() != null && !user.getNickName().trim().isEmpty()) {
                            // 检查用户是否被删除（del_flag = "2"）
                            if (user.getDelFlag() == null || !"2".equals(user.getDelFlag())) {
                                agricultureBatchTask.setResponsiblePersonName(user.getNickName());
                            }
                        }
                    }
                    
                    // 计算计划开始和结束日期：
                    // 1. 根据作业的周期单位（cycleUnit）设置乘数：0=周（乘数7），1=天（乘数1）
                    // 2. 计划开始时间 = 批次开始时间 + 作业起始时间（job_start * 乘数）
                    // 3. 计划完成时间 = 批次开始时间 + 作业结束时间（job_finish * 乘数）
                    int mult = sj.getCycleUnit().equals("0") ? 7 : 1; // 0=周，1=天
                    LocalDate startDate = agricultureCropBatch.getStartTime();
                    // 如果批次开始时间为空，则默认为当前日期
                    if (startDate == null) {
                        startDate = LocalDate.now();
                    }
                    // 将 LocalDate 转换为 LocalDateTime（当天的 00:00:00）用于计算
                    LocalDateTime startTime = startDate.atStartOfDay();
                    // 计算计划开始时间：批次开始时间 + 作业起始时间（转换为天数）
                    LocalDateTime planStartDateTime = startTime.plusDays(sj.getJobStart() * mult);
                    agricultureBatchTask.setPlanStart(Date.from(planStartDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    // 计算计划完成时间：批次开始时间 + 作业结束时间（转换为天数）
                    LocalDateTime planFinishDateTime = startTime.plusDays(sj.getJobFinish() * mult);
                    agricultureBatchTask.setPlanFinish(Date.from(planFinishDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                    // 设置任务状态为未分配（0）
                    agricultureBatchTask.setStatus("0");
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
    @Transactional(rollbackFor = Exception.class)
    public int updateAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        // 获取修改前的批次信息，用于判断是否需要重新验证
        AgricultureCropBatch oldBatch = null;
        if (agricultureCropBatch.getBatchId() != null) {
            oldBatch = getById(agricultureCropBatch.getBatchId());
        }
        
        // 如果时间相关字段或计划关联发生变化，需要重新验证
        boolean needValidate = false;
        if (oldBatch != null) {
            // 检查时间字段是否变化
            if (!java.util.Objects.equals(agricultureCropBatch.getStartTime(), oldBatch.getStartTime()) ||
                !java.util.Objects.equals(agricultureCropBatch.getExpectedHarvestTime(), oldBatch.getExpectedHarvestTime()) ||
                !java.util.Objects.equals(agricultureCropBatch.getPlanId(), oldBatch.getPlanId()) ||
                !java.util.Objects.equals(agricultureCropBatch.getDetailId(), oldBatch.getDetailId())) {
                needValidate = true;
            }
        } else {
            // 新增或无法获取旧数据，需要验证
            needValidate = true;
        }
        
        if (needValidate) {
            // 验证批次时间范围
            validateBatchTimeRange(agricultureCropBatch);
        }
        
        // 先使用 updateById 更新其他字段（会忽略 null 值）
        int result = updateById(agricultureCropBatch) ? 1 : 0;
        
        // 如果 season_type 为 null，需要单独更新（因为 updateById 会忽略 null 值）
        if (agricultureCropBatch.getSeasonType() == null) {
            LambdaUpdateWrapper<AgricultureCropBatch> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(AgricultureCropBatch::getBatchId, agricultureCropBatch.getBatchId())
                    .set(AgricultureCropBatch::getSeasonType, null);
            update(updateWrapper);
        }
        
        return result;
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
     * @param agricultureCropBatch 包含查询条件的实体，如批次名称batchName
     * @return 符合条件的作物批次列表
     */
    @Override
    public List<AgricultureCropBatch> getCropBatchWithClassImages(AgricultureCropBatch agricultureCropBatch) {
        // 创建LambdaQueryWrapper，用于构建查询条件，基于数据库实体类AgricultureCropBatch
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        // 如果批次名称不为空，则添加批次名称的模糊查询条件
        if (StringUtils.isNotEmpty(agricultureCropBatch.getBatchName())) {
            queryWrapper.like(AgricultureCropBatch::getBatchName, agricultureCropBatch.getBatchName());
        }
        if (agricultureCropBatch.getClassId() != null) {
            queryWrapper.eq(AgricultureCropBatch::getClassId, agricultureCropBatch.getClassId());
        }
        // 调用Mapper方法执行查询，并将Wrapper作为参数传递，让Mapper根据条件拼接SQL
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }

    /**
     * 验证批次时间范围
     * 验证批次的时间是否在关联计划的允许范围内
     *
     * @param batch 种植批次
     * @throws ServiceException 如果验证失败
     */
    private void validateBatchTimeRange(AgricultureCropBatch batch) {
        if (batch == null) {
            return;
        }

        LocalDate batchStartTime = batch.getStartTime();
        LocalDateTime batchExpectedHarvestTime = batch.getExpectedHarvestTime();

        // 1. 基础验证：预计收获时间不能早于开始时间
        if (batchStartTime != null && batchExpectedHarvestTime != null) {
            LocalDate expectedHarvestDate = batchExpectedHarvestTime.toLocalDate();
            if (expectedHarvestDate.isBefore(batchStartTime)) {
                throw new ServiceException(
                        String.format("批次预计收获时间（%s）不能早于开始时间（%s）", 
                                expectedHarvestDate, batchStartTime));
            }
        }

        // 2. 如果批次关联了种植计划（planId 不为空），验证时间范围
        if (batch.getPlanId() != null) {
            validateBatchTimeWithPlantingPlan(batch);
        }
        // 3. 如果批次关联了轮作计划明细（detailId 不为空），验证时间范围
        else if (batch.getDetailId() != null) {
            validateBatchTimeWithRotationPlan(batch);
        }
    }

    /**
     * 验证批次时间与种植计划的时间范围
     *
     * @param batch 种植批次
     * @throws ServiceException 如果验证失败
     */
    private void validateBatchTimeWithPlantingPlan(AgricultureCropBatch batch) {
        // 查询种植计划信息
        AgriculturePlantingPlan plan = agriculturePlantingPlanService.getById(batch.getPlanId());
        if (plan == null) {
            throw new ServiceException("关联的种植计划不存在，计划ID: " + batch.getPlanId());
        }

        LocalDate batchStartTime = batch.getStartTime();
        LocalDateTime batchExpectedHarvestTime = batch.getExpectedHarvestTime();
        LocalDate planStartDate = plan.getStartDate();
        LocalDate planEndDate = plan.getEndDate();

        // 验证批次的开始时间不能早于计划的开始日期
        if (batchStartTime != null && planStartDate != null) {
            if (batchStartTime.isBefore(planStartDate)) {
                throw new ServiceException(
                        String.format("批次开始时间（%s）不能早于计划【%s】的开始时间（%s）", 
                                batchStartTime, plan.getPlanName(), planStartDate));
            }
        }

        // 验证批次的预计收获时间不能晚于计划的结束日期
        if (batchExpectedHarvestTime != null && planEndDate != null) {
            LocalDate expectedHarvestDate = batchExpectedHarvestTime.toLocalDate();
            if (expectedHarvestDate.isAfter(planEndDate)) {
                throw new ServiceException(
                        String.format("批次预计收获时间（%s）不能晚于计划【%s】的结束时间（%s）", 
                                expectedHarvestDate, plan.getPlanName(), planEndDate));
            }
        }

        // 如果计划类型是季度计划，额外验证批次的季节类型是否与计划的季节类型匹配
        if ("seasonal".equals(plan.getPlanType())) {
            String planSeasonType = plan.getSeasonType();
            String batchSeasonType = batch.getSeasonType();
            if (planSeasonType != null && batchSeasonType != null && !planSeasonType.equals(batchSeasonType)) {
                throw new ServiceException(
                        String.format("批次季节类型（%s）与季度计划【%s】的季节类型（%s）不匹配", 
                                batchSeasonType, plan.getPlanName(), planSeasonType));
            }
        }
    }

    /**
     * 验证批次时间与轮作计划的时间范围
     *
     * @param batch 种植批次
     * @throws ServiceException 如果验证失败
     */
    private void validateBatchTimeWithRotationPlan(AgricultureCropBatch batch) {
        // 直接通过 detail_id 查询轮作计划明细
        if (batch.getDetailId() == null) {
            throw new ServiceException("批次关联轮作计划时，轮作计划明细ID不能为空");
        }

        AgriculturePlanDetail detail = agriculturePlanDetailService.getById(batch.getDetailId());

        if (detail == null) {
            throw new ServiceException("关联的轮作计划明细不存在，明细ID: " + batch.getDetailId());
        }

        LocalDate batchStartTime = batch.getStartTime();
        LocalDateTime batchExpectedHarvestTime = batch.getExpectedHarvestTime();
        LocalDate detailExpectedStartDate = detail.getExpectedStartDate();
        LocalDate detailExpectedEndDate = detail.getExpectedEndDate();
        
        // 验证批次的开始时间不能早于轮作计划明细的预期开始日期
        if (batchStartTime != null && detailExpectedStartDate != null) {
            if (batchStartTime.isBefore(detailExpectedStartDate)) {
                throw new ServiceException(
                        String.format("批次开始时间（%s）不能早于轮作计划明细的预期开始时间（%s）", 
                                batchStartTime, detailExpectedStartDate));
            }
        }

        // 验证批次的预计收获时间不能晚于轮作计划明细的预期结束日期
        if (batchExpectedHarvestTime != null && detailExpectedEndDate != null) {
            LocalDate expectedHarvestDate = batchExpectedHarvestTime.toLocalDate();
            if (expectedHarvestDate.isAfter(detailExpectedEndDate)) {
                throw new ServiceException(
                        String.format("批次预计收获时间（%s）不能晚于轮作计划明细的预期结束时间（%s）", 
                                expectedHarvestDate, detailExpectedEndDate));
            }
        }
    }
}
