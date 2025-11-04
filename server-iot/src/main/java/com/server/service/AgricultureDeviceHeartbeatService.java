package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDeviceHeartbeat;

/**
 * 设备心跳状态 Service接口
 * 
 * @author server
 * @date 2025-11-03
 */
public interface AgricultureDeviceHeartbeatService extends IService<AgricultureDeviceHeartbeat>
{
    /**
     * 查询设备心跳状态 
     * 
     * @param id 设备心跳状态 主键
     * @return 设备心跳状态 
     */
    public AgricultureDeviceHeartbeat selectAgricultureDeviceHeartbeatById(Long id);

    /**
     * 查询设备心跳状态 列表
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态 
     * @return 设备心跳状态 集合
     */
    public List<AgricultureDeviceHeartbeat> selectAgricultureDeviceHeartbeatList(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat);

    /**
     * 新增设备心跳状态 
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态 
     * @return 结果
     */
    public int insertAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat);

    /**
     * 修改设备心跳状态 
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态 
     * @return 结果
     */
    public int updateAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat);

    /**
     * 批量删除设备心跳状态 
     * 
     * @param ids 需要删除的设备心跳状态 主键集合
     * @return 结果
     */
    public int deleteAgricultureDeviceHeartbeatByIds(Long[] ids);

    /**
     * 删除设备心跳状态 信息
     * 
     * @param id 设备心跳状态 主键
     * @return 结果
     */
    public int deleteAgricultureDeviceHeartbeatById(Long id);
    
    /**
     * 更新设备在线状态（根据设备ID）
     * 
     * @param deviceId 设备ID
     * @param onlineStatus 在线状态（1=在线，0=离线）
     * @return 结果
     */
    public int updateOnlineStatusByDeviceId(Long deviceId, Long onlineStatus);
    
    /**
     * 更新设备为在线状态（根据设备ID）
     * 
     * @param deviceId 设备ID
     * @return 结果
     */
    public int setDeviceOnline(Long deviceId);
    
    /**
     * 更新设备为离线状态（根据设备ID）
     * 
     * @param deviceId 设备ID
     * @return 结果
     */
    public int setDeviceOffline(Long deviceId);
}

