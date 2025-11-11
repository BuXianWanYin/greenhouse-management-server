package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureResourceInventory;

import java.util.List;

/**
 * 农资库存Service接口
 * 
 * @author server
 * @date 2025-01-XX
 */
public interface AgricultureResourceInventoryService extends IService<AgricultureResourceInventory> {

    /**
     * 查询农资库存列表
     * 
     * @param agricultureResourceInventory 农资库存
     * @return 农资库存集合
     */
    List<AgricultureResourceInventory> selectAgricultureResourceInventoryList(AgricultureResourceInventory agricultureResourceInventory);

    /**
     * 新增农资库存
     * 
     * @param agricultureResourceInventory 农资库存
     * @return 结果
     */
    int addAgricultureResourceInventory(AgricultureResourceInventory agricultureResourceInventory);

    /**
     * 修改农资库存
     * 
     * @param agricultureResourceInventory 农资库存
     * @return 结果
     */
    int updateAgricultureResourceInventory(AgricultureResourceInventory agricultureResourceInventory);

    /**
     * 删除农资库存信息
     * 
     * @param inventoryId 农资库存主键
     * @return 结果
     */
    int deleteAgricultureResourceInventoryById(Long inventoryId);

    /**
     * 根据农资ID查询库存
     * 
     * @param resourceId 农资ID
     * @return 库存信息
     */
    AgricultureResourceInventory selectByResourceId(Long resourceId);

    /**
     * 扣减库存
     * 
     * @param resourceId 农资ID
     * @param quantity 扣减数量
     * @return 结果
     */
    int deductInventory(Long resourceId, java.math.BigDecimal quantity);

    /**
     * 增加库存
     * 
     * @param resourceId 农资ID
     * @param quantity 增加数量
     * @return 结果
     */
    int addInventory(Long resourceId, java.math.BigDecimal quantity);
}

