package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureResourceInventory;
import com.server.mapper.AgricultureResourceInventoryMapper;
import com.server.service.AgricultureResourceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 农资库存Service业务层处理
 * 
 * @author server
 * @date 2025-01-XX
 */
@Service
public class AgricultureResourceInventoryServiceImpl extends ServiceImpl<AgricultureResourceInventoryMapper, AgricultureResourceInventory> implements AgricultureResourceInventoryService {

    @Autowired
    private AgricultureResourceInventoryMapper agricultureResourceInventoryMapper;

    /**
     * 查询农资库存列表
     * 
     * @param agricultureResourceInventory 农资库存
     * @return 农资库存
     */
    @Override
    public List<AgricultureResourceInventory> selectAgricultureResourceInventoryList(AgricultureResourceInventory agricultureResourceInventory) {
        LambdaQueryWrapper<AgricultureResourceInventory> lambdaQueryWrapper = new QueryWrapper<AgricultureResourceInventory>().lambda();
        lambdaQueryWrapper.eq(agricultureResourceInventory.getResourceId() != null, 
                AgricultureResourceInventory::getResourceId, agricultureResourceInventory.getResourceId());
        return agricultureResourceInventoryMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增农资库存
     * 
     * @param agricultureResourceInventory 农资库存
     * @return 结果
     */
    @Override
    public int addAgricultureResourceInventory(AgricultureResourceInventory agricultureResourceInventory) {
        return agricultureResourceInventoryMapper.insert(agricultureResourceInventory);
    }

    /**
     * 修改农资库存
     * 
     * @param agricultureResourceInventory 农资库存
     * @return 结果
     */
    @Override
    public int updateAgricultureResourceInventory(AgricultureResourceInventory agricultureResourceInventory) {
        return agricultureResourceInventoryMapper.updateById(agricultureResourceInventory);
    }

    /**
     * 删除农资库存信息
     * 
     * @param inventoryId 农资库存主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureResourceInventoryById(Long inventoryId) {
        return agricultureResourceInventoryMapper.deleteById(inventoryId);
    }

    /**
     * 根据农资ID查询库存
     * 
     * @param resourceId 农资ID
     * @return 库存信息
     */
    @Override
    public AgricultureResourceInventory selectByResourceId(Long resourceId) {
        LambdaQueryWrapper<AgricultureResourceInventory> lambdaQueryWrapper = new QueryWrapper<AgricultureResourceInventory>().lambda();
        lambdaQueryWrapper.eq(AgricultureResourceInventory::getResourceId, resourceId);
        return agricultureResourceInventoryMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 扣减库存
     * 
     * @param resourceId 农资ID
     * @param quantity 扣减数量
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deductInventory(Long resourceId, BigDecimal quantity) {
        AgricultureResourceInventory inventory = selectByResourceId(resourceId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }
        BigDecimal newStock = inventory.getCurrentStock().subtract(quantity);
        if (newStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("库存不足");
        }
        LambdaUpdateWrapper<AgricultureResourceInventory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgricultureResourceInventory::getResourceId, resourceId)
                .set(AgricultureResourceInventory::getCurrentStock, newStock);
        return agricultureResourceInventoryMapper.update(null, updateWrapper);
    }

    /**
     * 增加库存
     * 
     * @param resourceId 农资ID
     * @param quantity 增加数量
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addInventory(Long resourceId, BigDecimal quantity) {
        AgricultureResourceInventory inventory = selectByResourceId(resourceId);
        if (inventory == null) {
            throw new RuntimeException("库存记录不存在");
        }
        BigDecimal newStock = inventory.getCurrentStock().add(quantity);
        LambdaUpdateWrapper<AgricultureResourceInventory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AgricultureResourceInventory::getResourceId, resourceId)
                .set(AgricultureResourceInventory::getCurrentStock, newStock);
        return agricultureResourceInventoryMapper.update(null, updateWrapper);
    }
}

