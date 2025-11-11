package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureResourceUsage;
import com.server.mapper.AgricultureResourceUsageMapper;
import com.server.service.AgricultureResourceInventoryService;
import com.server.service.AgricultureResourceUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 农资使用记录Service业务层处理
 * 
 * @author server
 * @date 2025-01-XX
 */
@Service
public class AgricultureResourceUsageServiceImpl extends ServiceImpl<AgricultureResourceUsageMapper, AgricultureResourceUsage> implements AgricultureResourceUsageService {

    @Autowired
    private AgricultureResourceUsageMapper agricultureResourceUsageMapper;

    @Autowired
    private AgricultureResourceInventoryService agricultureResourceInventoryService;

    /**
     * 查询农资使用记录列表
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 农资使用记录
     */
    @Override
    public List<AgricultureResourceUsage> selectAgricultureResourceUsageList(AgricultureResourceUsage agricultureResourceUsage) {
        LambdaQueryWrapper<AgricultureResourceUsage> lambdaQueryWrapper = new QueryWrapper<AgricultureResourceUsage>().lambda();
        lambdaQueryWrapper.eq(agricultureResourceUsage.getResourceId() != null, 
                AgricultureResourceUsage::getResourceId, agricultureResourceUsage.getResourceId());
        lambdaQueryWrapper.eq(agricultureResourceUsage.getBatchId() != null, 
                AgricultureResourceUsage::getBatchId, agricultureResourceUsage.getBatchId());
        lambdaQueryWrapper.eq(agricultureResourceUsage.getTaskId() != null, 
                AgricultureResourceUsage::getTaskId, agricultureResourceUsage.getTaskId());
        return agricultureResourceUsageMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增农资使用记录（自动扣减库存）
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addAgricultureResourceUsage(AgricultureResourceUsage agricultureResourceUsage) {
        // 设置使用日期，如果为空则使用当前时间
        if (agricultureResourceUsage.getUsageDate() == null) {
            agricultureResourceUsage.setUsageDate(LocalDateTime.now());
        }
        // 如果是消耗类型，自动扣减库存
        if ("1".equals(agricultureResourceUsage.getUsageType())) {
            agricultureResourceInventoryService.deductInventory(
                    agricultureResourceUsage.getResourceId(), 
                    agricultureResourceUsage.getUsageQuantity()
            );
        }
        // 如果是入库类型(2)，自动增加库存
        if ("2".equals(agricultureResourceUsage.getUsageType())) {
            agricultureResourceInventoryService.addInventory(
                    agricultureResourceUsage.getResourceId(), 
                    agricultureResourceUsage.getUsageQuantity()
            );
        }
        return agricultureResourceUsageMapper.insert(agricultureResourceUsage);
    }

    /**
     * 修改农资使用记录
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 结果
     */
    @Override
    public int updateAgricultureResourceUsage(AgricultureResourceUsage agricultureResourceUsage) {
        return agricultureResourceUsageMapper.updateById(agricultureResourceUsage);
    }

    /**
     * 删除农资使用记录信息
     * 
     * @param usageId 农资使用记录主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureResourceUsageById(Long usageId) {
        return agricultureResourceUsageMapper.deleteById(usageId);
    }

    /**
     * 根据批次ID查询使用记录
     * 
     * @param batchId 批次ID
     * @return 使用记录列表
     */
    @Override
    public List<AgricultureResourceUsage> selectByBatchId(Long batchId) {
        LambdaQueryWrapper<AgricultureResourceUsage> lambdaQueryWrapper = new QueryWrapper<AgricultureResourceUsage>().lambda();
        lambdaQueryWrapper.eq(AgricultureResourceUsage::getBatchId, batchId);
        return agricultureResourceUsageMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 根据任务ID查询使用记录
     * 
     * @param taskId 任务ID
     * @return 使用记录列表
     */
    @Override
    public List<AgricultureResourceUsage> selectByTaskId(Long taskId) {
        LambdaQueryWrapper<AgricultureResourceUsage> lambdaQueryWrapper = new QueryWrapper<AgricultureResourceUsage>().lambda();
        lambdaQueryWrapper.eq(AgricultureResourceUsage::getTaskId, taskId);
        return agricultureResourceUsageMapper.selectList(lambdaQueryWrapper);
    }
}

