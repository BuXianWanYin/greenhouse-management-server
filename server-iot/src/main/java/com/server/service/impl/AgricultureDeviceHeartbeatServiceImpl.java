package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureDeviceHeartbeatMapper;
import com.server.domain.AgricultureDeviceHeartbeat;
import com.server.service.AgricultureDeviceHeartbeatService;

/**
 * 设备心跳状态（关联设备，设备删除时心跳记录自动删除）Service业务层处理
 * 
 * @author server
 * @date 2025-11-03
 */
@Service
public class AgricultureDeviceHeartbeatServiceImpl extends ServiceImpl<AgricultureDeviceHeartbeatMapper, AgricultureDeviceHeartbeat> implements AgricultureDeviceHeartbeatService
{
    @Autowired
    private AgricultureDeviceHeartbeatMapper agricultureDeviceHeartbeatMapper;

    /**
     * 查询设备心跳状态
     * 
     * @param id 设备id
     * @return 设备心跳状态
     */
    @Override
    public AgricultureDeviceHeartbeat selectAgricultureDeviceHeartbeatById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询设备心跳状态（关联设备，设备删除时心跳记录自动删除）列表
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * @return 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     */
    @Override
    public List<AgricultureDeviceHeartbeat> selectAgricultureDeviceHeartbeatList(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        return list();
    }

    /**
     * 新增设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * @return 结果
     */
    @Override
    public int insertAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        agricultureDeviceHeartbeat.setCreateTime(LocalDateTime.now());
        return agricultureDeviceHeartbeatMapper.insert(agricultureDeviceHeartbeat);
    }

    /**
     * 修改设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * @return 结果
     */
    @Override
    public int updateAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        agricultureDeviceHeartbeat.setUpdateTime(LocalDateTime.now());
        return agricultureDeviceHeartbeatMapper.updateById(agricultureDeviceHeartbeat);
    }

    /**
     * 批量删除设备心跳状态（关联设备，设备删除时心跳记录自动删除）
     * 
     * @param ids 需要删除的设备id
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceHeartbeatByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除设备心跳状态（关联设备，设备删除时心跳记录自动删除）信息
     * 
     * @param id 设备id
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceHeartbeatById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}

