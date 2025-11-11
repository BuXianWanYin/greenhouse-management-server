package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureResource;
import com.server.domain.AgricultureResourceUsage;
import com.server.mapper.AgricultureResourceUsageMapper;
import com.server.service.AgricultureResourceInventoryService;
import com.server.service.AgricultureResourceService;
import com.server.service.AgricultureResourceUsageService;
import com.server.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
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

    @Autowired
    private AgricultureResourceService agricultureResourceService;

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
     * 新增农资使用记录（自动扣减/增加库存）
     * 
     * @param agricultureResourceUsage 农资使用记录
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addAgricultureResourceUsage(AgricultureResourceUsage agricultureResourceUsage) {
        // 参数校验
        if (agricultureResourceUsage.getResourceId() == null) {
            throw new RuntimeException("农资ID不能为空");
        }
        if (agricultureResourceUsage.getUsageQuantity() == null || agricultureResourceUsage.getUsageQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("使用数量必须大于0");
        }
        if (agricultureResourceUsage.getUsageType() == null) {
            throw new RuntimeException("使用类型不能为空");
        }

        // 设置使用日期，如果为空则使用当前时间
        if (agricultureResourceUsage.getUsageDate() == null) {
            agricultureResourceUsage.setUsageDate(LocalDateTime.now());
        }

        // 查询农资类型
        AgricultureResource resource = agricultureResourceService.getById(agricultureResourceUsage.getResourceId());
        if (resource == null) {
            throw new RuntimeException("农资资源不存在");
        }
        String resourceType = resource.getResourceType();
        String usageType = agricultureResourceUsage.getUsageType();
        BigDecimal usageQuantity = agricultureResourceUsage.getUsageQuantity();

        // 根据使用类型处理库存和状态
        if ("2".equals(usageType)) {
            // 入库：库存增加，状态设为正常
            agricultureResourceInventoryService.addInventory(agricultureResourceUsage.getResourceId(), usageQuantity);
            agricultureResourceUsage.setStatus("0"); // 正常
        } else if ("0".equals(usageType)) {
            // 领用：库存减少
            agricultureResourceInventoryService.deductInventory(agricultureResourceUsage.getResourceId(), usageQuantity);
            if ("1".equals(resourceType)) {
                // 机械类型：状态设为使用中
                agricultureResourceUsage.setStatus("2");
            } else {
                // 物料类型：状态设为正常
                agricultureResourceUsage.setStatus("0");
            }
        } else if ("1".equals(usageType)) {
            // 消耗：库存减少，状态设为正常
            agricultureResourceInventoryService.deductInventory(agricultureResourceUsage.getResourceId(), usageQuantity);
            agricultureResourceUsage.setStatus("0"); // 正常
        } else {
            throw new RuntimeException("使用类型不正确");
        }

        // 如果状态为空，默认设置为正常
        if (agricultureResourceUsage.getStatus() == null) {
            agricultureResourceUsage.setStatus("0");
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
     * 删除农资使用记录信息（恢复库存）
     * 
     * @param usageId 农资使用记录主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgricultureResourceUsageById(Long usageId) {
        // 查询使用记录
        AgricultureResourceUsage usage = agricultureResourceUsageMapper.selectById(usageId);
        if (usage == null) {
            throw new RuntimeException("使用记录不存在");
        }

        // 恢复库存
        String usageType = usage.getUsageType();
        BigDecimal usageQuantity = usage.getUsageQuantity();
        Long resourceId = usage.getResourceId();

        if ("0".equals(usageType) || "1".equals(usageType)) {
            // 领用或消耗：恢复库存（增加）
            agricultureResourceInventoryService.addInventory(resourceId, usageQuantity);
        } else if ("2".equals(usageType)) {
            // 入库：减少库存（恢复）
            agricultureResourceInventoryService.deductInventory(resourceId, usageQuantity);
        }

        // 删除使用记录
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

    /**
     * 归还机械类型农资
     * 
     * @param usageId 使用记录ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int returnAgricultureResource(Long usageId) {
        // 查询使用记录
        AgricultureResourceUsage usage = agricultureResourceUsageMapper.selectById(usageId);
        if (usage == null) {
            throw new RuntimeException("使用记录不存在");
        }

        // 验证：使用类型必须是"领用"
        if (!"0".equals(usage.getUsageType())) {
            throw new RuntimeException("该记录不是领用类型，无法归还");
        }

        // 验证：状态必须是"使用中"
        if (!"2".equals(usage.getStatus())) {
            throw new RuntimeException("该农资已归还或已撤销");
        }

        // 查询农资类型
        AgricultureResource resource = agricultureResourceService.getById(usage.getResourceId());
        if (resource == null) {
            throw new RuntimeException("农资资源不存在");
        }

        // 验证：必须是机械类型
        if (!"1".equals(resource.getResourceType())) {
            throw new RuntimeException("只有机械类型农资可以归还");
        }

        // 更新库存：增加库存（归还）
        agricultureResourceInventoryService.addInventory(usage.getResourceId(), usage.getUsageQuantity());

        // 更新使用记录：状态从"使用中"改为"正常/已归还"
        LambdaUpdateWrapper<AgricultureResourceUsage> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgricultureResourceUsage::getUsageId, usageId)
                .set(AgricultureResourceUsage::getStatus, "0") // 正常/已归还
                .set(AgricultureResourceUsage::getUpdateTime, LocalDateTime.now())
                .set(AgricultureResourceUsage::getUpdateBy, String.valueOf(SecurityUtils.getUserId()));

        return agricultureResourceUsageMapper.update(null, updateWrapper);
    }

    /**
     * 批量删除农资使用记录信息（恢复库存）
     * 重写此方法以确保批量删除时也正确处理库存恢复
     * 
     * @param idList 使用记录ID集合
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (idList == null || idList.isEmpty()) {
            return false;
        }

        // 查询所有使用记录
        List<AgricultureResourceUsage> usageList = this.listByIds(idList);
        if (usageList == null || usageList.isEmpty()) {
            return false;
        }

        // 逐个恢复库存
        for (AgricultureResourceUsage usage : usageList) {
            String usageType = usage.getUsageType();
            BigDecimal usageQuantity = usage.getUsageQuantity();
            Long resourceId = usage.getResourceId();

            if (usageType != null && usageQuantity != null && resourceId != null) {
                if ("0".equals(usageType) || "1".equals(usageType)) {
                    // 领用或消耗：恢复库存（增加）
                    agricultureResourceInventoryService.addInventory(resourceId, usageQuantity);
                } else if ("2".equals(usageType)) {
                    // 入库：减少库存（恢复）
                    agricultureResourceInventoryService.deductInventory(resourceId, usageQuantity);
                }
            }
        }

        // 批量删除使用记录
        return super.removeByIds(idList);
    }
}

