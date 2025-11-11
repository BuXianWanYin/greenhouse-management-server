package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureBatchTask;
import com.server.mapper.AgricultureBatchTaskMapper;
import com.server.service.AgricultureBatchTaskService;
import com.server.service.PlanDateUpdateService;
import com.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AgricultureBatchTaskServiceImpl extends ServiceImpl<AgricultureBatchTaskMapper, AgricultureBatchTask> implements AgricultureBatchTaskService {

    @Autowired
    private AgricultureBatchTaskMapper batchTaskServiceMapper;
    
    @Autowired
    private PlanDateUpdateService planDateUpdateService;

    /**
     * 查询批次任务列表
     *
     * @param agricultureBatchTask 查询条件
     * @return 批次任务列表
     */
    @Override
    public List<AgricultureBatchTask> selectBatchTaskList(AgricultureBatchTask agricultureBatchTask) {
        // 创建 LambdaQueryWrapper 用于构建动态查询条件
        LambdaQueryWrapper<AgricultureBatchTask> queryWrapper = new LambdaQueryWrapper<>();
        // 获取 planFinish 字段的值（结束日期）
        Date planFinishDate = agricultureBatchTask.getPlanFinish();
        if (planFinishDate != null) {
            // 将 Date 类型转换为 LocalDateTime，便于处理时间部分
            LocalDateTime finishDateTime = planFinishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            // 如果时间为 00:00:00，说明前端只传了日期，需要补全为 23:59:59.999，确保当天的数据能被查出
            if (finishDateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
                finishDateTime = finishDateTime.with(LocalTime.MAX);
                // 将补全后的 LocalDateTime 转回 Date 类型
                planFinishDate = Date.from(finishDateTime.atZone(ZoneId.systemDefault()).toInstant());
            }
        }
        // 如果 batchId 不为空，添加等值查询条件
        queryWrapper.eq(agricultureBatchTask.getBatchId() != null,
                        AgricultureBatchTask::getBatchId,
                        agricultureBatchTask.getBatchId())
                // 如果 taskName 不为空，添加模糊查询条件
                .like(StringUtils.isNotBlank(agricultureBatchTask.getTaskName()),
                        AgricultureBatchTask::getTaskName,
                        agricultureBatchTask.getTaskName())
                // 如果 status 不为空，添加等值查询条件
                .eq(StringUtils.isNotBlank(agricultureBatchTask.getStatus()),
                        AgricultureBatchTask::getStatus,
                        agricultureBatchTask.getStatus())
                // 如果 planStart 不为空，添加大于等于的范围查询条件
                .ge(agricultureBatchTask.getPlanStart() != null, AgricultureBatchTask::getPlanStart, agricultureBatchTask.getPlanStart())
                // 如果 planFinish 不为空，添加小于等于的范围查询条件（已补全为 23:59:59）
                .le(planFinishDate != null, AgricultureBatchTask::getPlanFinish, planFinishDate)
                // 设置结果集按照 planStart 字段升序排序，然后按 taskId 排序
                .orderByAsc(AgricultureBatchTask::getPlanStart)
                .orderByAsc(AgricultureBatchTask::getTaskId);
        // 日志输出查询参数，便于调试
        log.info("Query parameters: {}", agricultureBatchTask);
        // 执行查询，获取结果列表
        List<AgricultureBatchTask> result = baseMapper.selectList(queryWrapper);
        // 日志输出结果数量，便于调试
        log.info("Query result size: {}", result.size());
        // 返回查询结果
        return result;
    }

    /**
     * 查询批次任务
     *
     * @param taskId 批次任务主键
     * @return 批次任务
     */
    @Override
    public AgricultureBatchTask selectBatchTaskByTaskId(Long taskId) {
        return getById(taskId);
    }

    /**
     * 删除批次任务
     *
     * @param taskId 批次任务主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgricultureCropBatchByBatchIds(Long taskId) {
        // 删除前，先获取任务信息，用于更新计划日期
        AgricultureBatchTask task = getById(taskId);
        Long batchId = task != null ? task.getBatchId() : null;
        
        int result = removeById(taskId) ? 1 : 0;
        
        // 删除后，更新相关计划的实际日期
        if (result > 0 && batchId != null) {
            try {
                planDateUpdateService.updatePlanDatesByBatchTask(batchId);
            } catch (Exception e) {
                log.error("更新计划实际日期失败，批次ID: {}", batchId, e);
                // 不抛出异常，避免影响主流程
            }
        }
        
        return result;
    }

    /**
     * 修改批次任务
     *
     * @param agricultureBatchTask 批次任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatchTask(AgricultureBatchTask agricultureBatchTask) {
        log.info("========== 开始更新批次任务 ==========");
        log.info("批次任务ID: {}, 批次ID: {}", agricultureBatchTask.getTaskId(), agricultureBatchTask.getBatchId());
        log.info("实际开始日期: {}, 实际结束日期: {}", agricultureBatchTask.getActualStart(), agricultureBatchTask.getActualFinish());
        
        // 获取修改前的任务信息，用于判断是否需要更新计划日期
        AgricultureBatchTask oldTask = null;
        if (agricultureBatchTask.getTaskId() != null) {
            oldTask = getById(agricultureBatchTask.getTaskId());
            if (oldTask != null) {
                log.info("原任务信息 - 实际开始日期: {}, 实际结束日期: {}, 批次ID: {}", 
                        oldTask.getActualStart(), oldTask.getActualFinish(), oldTask.getBatchId());
                // 如果前端没有传递batchId，从数据库查询结果中获取
                if (agricultureBatchTask.getBatchId() == null && oldTask.getBatchId() != null) {
                    log.info("前端未传递批次ID，从数据库查询结果中获取: {}", oldTask.getBatchId());
                    agricultureBatchTask.setBatchId(oldTask.getBatchId());
                }
            } else {
                log.warn("未找到原任务信息，任务ID: {}", agricultureBatchTask.getTaskId());
            }
        }
        
        agricultureBatchTask.setUpdateTime(new Date());
        int result = updateById(agricultureBatchTask) ? 1 : 0;
        log.info("批次任务更新结果: {}", result);
        
        // 获取批次ID（优先使用前端传递的，如果没有则使用数据库查询的）
        Long batchId = agricultureBatchTask.getBatchId();
        if (batchId == null && oldTask != null) {
            batchId = oldTask.getBatchId();
            log.info("从原任务信息中获取批次ID: {}", batchId);
        }
        
        // 如果实际开始或结束日期发生变化，更新相关计划的实际日期和状态
        if (result > 0 && batchId != null) {
            log.info("开始检查是否需要更新计划日期和状态，批次ID: {}", batchId);
            boolean needUpdate = false;
            boolean actualStartChanged = false;
            boolean statusChanged = false;
            
            if (oldTask != null) {
                // 检查实际日期是否发生变化
                Date oldActualStart = oldTask.getActualStart();
                Date newActualStart = agricultureBatchTask.getActualStart();
                Date oldActualFinish = oldTask.getActualFinish();
                Date newActualFinish = agricultureBatchTask.getActualFinish();
                String oldStatus = oldTask.getStatus();
                String newStatus = agricultureBatchTask.getStatus();
                
                log.info("比较日期变化 - 旧开始日期: {}, 新开始日期: {}", oldActualStart, newActualStart);
                log.info("比较日期变化 - 旧结束日期: {}, 新结束日期: {}", oldActualFinish, newActualFinish);
                log.info("比较状态变化 - 旧状态: {}, 新状态: {}", oldStatus, newStatus);
                
                // 检查实际开始日期是否从null变为有值（任务开始）
                if (oldActualStart == null && newActualStart != null) {
                    actualStartChanged = true;
                    needUpdate = true;
                    log.info("✓ 实际开始日期从null变为有值，标记为需要更新状态");
                }
                
                // 检查任务状态是否变为已完成（3）
                if (!"3".equals(oldStatus) && "3".equals(newStatus)) {
                    statusChanged = true;
                    needUpdate = true;
                    log.info("✓ 任务状态变为已完成（3），标记为需要更新状态");
                }
                
                // 检查实际结束日期是否从null变为有值（任务完成）
                if (oldActualFinish == null && newActualFinish != null) {
                    statusChanged = true;
                    needUpdate = true;
                    log.info("✓ 实际结束日期从null变为有值，标记为需要更新状态");
                }
                
                if ((oldActualStart != null && newActualStart == null) ||
                    (oldActualStart != null && newActualStart != null && !oldActualStart.equals(newActualStart)) ||
                    (oldActualFinish != null && newActualFinish == null) ||
                    (oldActualFinish != null && newActualFinish != null && !oldActualFinish.equals(newActualFinish))) {
                    needUpdate = true;
                    log.info("✓ 实际日期发生变化，标记为需要更新");
                }
            } else {
                // 新增任务，如果有实际日期，需要更新
                if (agricultureBatchTask.getActualStart() != null || agricultureBatchTask.getActualFinish() != null) {
                    if (agricultureBatchTask.getActualStart() != null) {
                        actualStartChanged = true;
                        log.info("✓ 新增任务有实际开始日期，标记为需要更新状态");
                    }
                    if (agricultureBatchTask.getActualFinish() != null || "3".equals(agricultureBatchTask.getStatus())) {
                        statusChanged = true;
                        log.info("✓ 新增任务有实际结束日期或已完成状态，标记为需要更新状态");
                    }
                    needUpdate = true;
                    log.info("✓ 新增任务有实际日期，标记为需要更新");
                }
            }
            
            log.info("是否需要更新: {}, 实际开始日期是否变化: {}, 状态是否变化: {}", 
                    needUpdate, actualStartChanged, statusChanged);
            
            if (needUpdate) {
                try {
                    // 先更新计划实际日期（这会触发数据库查询，确保数据已提交）
                    log.info("========== 更新批次 {} 的计划实际日期 ==========", batchId);
                    planDateUpdateService.updatePlanDatesByBatchTask(batchId);
                    
                    // 如果实际开始日期从null变为有值，或者任务状态/结束日期发生变化，更新批次状态和关联计划状态
                    if (actualStartChanged || statusChanged) {
                        log.info("========== 批次任务 {} 状态变化（开始或完成），更新批次 {} 状态和关联计划状态 ==========", 
                                agricultureBatchTask.getTaskId(), batchId);
                        // 重新查询任务，确保获取到最新的数据
                        AgricultureBatchTask updatedTask = getById(agricultureBatchTask.getTaskId());
                        if (updatedTask != null) {
                            log.info("确认任务 {} 已更新，status: {}, actual_start: {}, actual_finish: {}", 
                                    updatedTask.getTaskId(), updatedTask.getStatus(), 
                                    updatedTask.getActualStart(), updatedTask.getActualFinish());
                            planDateUpdateService.updateBatchAndPlanStatus(batchId);
                        } else {
                            log.warn("任务 {} 更新后查询不到", agricultureBatchTask.getTaskId());
                        }
                    }
                } catch (Exception e) {
                    log.error("更新计划实际日期和状态失败，批次ID: {}", batchId, e);
                    e.printStackTrace();
                    // 不抛出异常，避免影响主流程
                }
            } else {
                log.info("不需要更新计划日期和状态");
            }
        } else {
            log.warn("更新失败或批次ID为空，result: {}, batchId: {}", result, batchId);
        }
        
        log.info("========== 批次任务更新完成 ==========");
        return result;
    }

    /**
     * 新增批次任务
     *
     * @param agricultureBatchTask 批次任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchTask(AgricultureBatchTask agricultureBatchTask) {
        agricultureBatchTask.setCreateTime(new Date());
        agricultureBatchTask.setStatus("0"); // 默认未分配状态
        int result = save(agricultureBatchTask) ? 1 : 0;
        
        // 如果新增的任务有实际日期，更新相关计划的实际日期和状态
        if (result > 0 && agricultureBatchTask.getBatchId() != null &&
            (agricultureBatchTask.getActualStart() != null || agricultureBatchTask.getActualFinish() != null)) {
            try {
                // 更新批次状态和关联计划状态
                planDateUpdateService.updateBatchAndPlanStatus(agricultureBatchTask.getBatchId());
                // 更新计划实际日期
                planDateUpdateService.updatePlanDatesByBatchTask(agricultureBatchTask.getBatchId());
            } catch (Exception e) {
                log.error("更新计划实际日期和状态失败，批次ID: {}", agricultureBatchTask.getBatchId(), e);
                // 不抛出异常，避免影响主流程
            }
        }
        
        return result;
    }

    /**
     * 根据批次ID查询批次任务列表
     *
     * @param batchId 批次ID
     * @return 批次任务集合
     */
    @Override
    public List<AgricultureBatchTask> selectBatchTaskListByBatchId(Long batchId) {
        LambdaQueryWrapper<AgricultureBatchTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgricultureBatchTask::getBatchId, batchId)
                .orderByAsc(AgricultureBatchTask::getPlanStart)
                .orderByAsc(AgricultureBatchTask::getTaskId);
        return list(queryWrapper);
    }
}