package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDeviceHeartbeat;

/**
 * 设备心跳状态（关联设备，设备删除时心跳记录自动删除）Service接口
 * 
 * @author server
 * @date 2025-11-03
 */
public interface AgricultureDeviceHeartbeatService extends IService<AgricultureDeviceHeartbeat>
{
    /**
     * 查询设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param id 设备心跳状态（关联设备，设备删除时心跳记录自动删除）主键
     * @return 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     */
    public AgricultureDeviceHeartbeat selectAgricultureDeviceHeartbeatById(Long id);

    /**
     * 查询设备心跳状态（关联设备，设备删除时心跳记录自动删除）列表
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * @return 设备心跳状态（关联设备，设备删除时心跳记录自动删除）集合
     */
    public List<AgricultureDeviceHeartbeat> selectAgricultureDeviceHeartbeatList(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat);

    /**
     * 新增设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * @return 结果
     */
    public int insertAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat);

    /**
     * 修改设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * @return 结果
     */
    public int updateAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat);

    /**
     * 批量删除设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param ids 需要删除的设备心跳状态（关联设备，设备删除时心跳记录自动删除）主键集合
     * @return 结果
     */
    public int deleteAgricultureDeviceHeartbeatByIds(Long[] ids);

    /**
     * 删除设备心跳状态（关联设备，设备删除时心跳记录自动删除）信息
     * 
     * @param id 设备心跳状态（关联设备，设备删除时心跳记录自动删除）主键
     * @return 结果
     */
    public int deleteAgricultureDeviceHeartbeatById(Long id);
}

