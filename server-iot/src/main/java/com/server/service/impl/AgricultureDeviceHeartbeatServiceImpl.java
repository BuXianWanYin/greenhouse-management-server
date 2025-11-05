package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceHeartbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureDeviceHeartbeatMapper;
import com.server.service.AgricultureDeviceHeartbeatService;
import com.server.service.AgricultureDeviceService;
import com.server.util.ModbusCommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设备心跳状态 Service业务层处理
 * 负责心跳数据的 CRUD 操作和在线状态管理
 * 不依赖 iot.enabled 配置，始终可用
 * 
 * @author server
 * @date 2025-11-03
 */
@Service
public class AgricultureDeviceHeartbeatServiceImpl extends ServiceImpl<AgricultureDeviceHeartbeatMapper, AgricultureDeviceHeartbeat> implements AgricultureDeviceHeartbeatService
{
    private static final Logger log = LoggerFactory.getLogger(AgricultureDeviceHeartbeatServiceImpl.class);
    
    @Autowired
    private AgricultureDeviceHeartbeatMapper agricultureDeviceHeartbeatMapper;
    
    @Autowired
    private AgricultureDeviceService agricultureDeviceService;

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
     * 查询设备心跳状态 列表
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态 
     * @return 设备心跳状态 
     */
    @Override
    public List<AgricultureDeviceHeartbeat> selectAgricultureDeviceHeartbeatList(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        // 构建查询条件
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AgricultureDeviceHeartbeat> queryWrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        // 如果传入了deviceId，则根据deviceId查询
        if (agricultureDeviceHeartbeat != null && agricultureDeviceHeartbeat.getDeviceId() != null) {
            queryWrapper.eq("device_id", agricultureDeviceHeartbeat.getDeviceId());
        }
        
        // 如果传入了id，则根据id查询
        if (agricultureDeviceHeartbeat != null && agricultureDeviceHeartbeat.getId() != null) {
            queryWrapper.eq("id", agricultureDeviceHeartbeat.getId());
        }
        
        // 如果传入了在线状态，则根据在线状态查询
        if (agricultureDeviceHeartbeat != null && agricultureDeviceHeartbeat.getOnlineStatus() != null) {
            queryWrapper.eq("online_status", agricultureDeviceHeartbeat.getOnlineStatus());
        }
        
        return list(queryWrapper);
    }

    /**
     * 新增设备心跳状态 
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态 
     * @return 结果
     */
    @Override
    public int insertAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        // 自动解析心跳指令
        parseHeartbeatCommand(agricultureDeviceHeartbeat);
        
        // 如果发送间隔未设置，设置默认值5秒
        if (agricultureDeviceHeartbeat.getSendInterval() == null || agricultureDeviceHeartbeat.getSendInterval() <= 0) {
            agricultureDeviceHeartbeat.setSendInterval(5L);
        }

        agricultureDeviceHeartbeat.setCreateTime(LocalDateTime.now());
        return agricultureDeviceHeartbeatMapper.insert(agricultureDeviceHeartbeat);
    }

    /**
     * 修改设备心跳状态 
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态 
     * @return 结果
     */
    @Override
    public int updateAgricultureDeviceHeartbeat(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat)
    {
        // 如果心跳指令有变化，重新解析
        if (agricultureDeviceHeartbeat.getHeartbeatCmdHex() != null) {
            parseHeartbeatCommand(agricultureDeviceHeartbeat);
        }
        
        return agricultureDeviceHeartbeatMapper.updateById(agricultureDeviceHeartbeat);
    }
    
    /**
     * 解析心跳指令并填充相关字段
     * 
     * @param agricultureDeviceHeartbeat 设备心跳状态对象
     */
    private void parseHeartbeatCommand(AgricultureDeviceHeartbeat agricultureDeviceHeartbeat) {
        String heartbeatCmdHex = agricultureDeviceHeartbeat.getHeartbeatCmdHex();
        
        if (heartbeatCmdHex == null || heartbeatCmdHex.trim().isEmpty()) {
            log.warn("心跳指令为空，跳过解析");
            return;
        }
        
        // 解析Modbus指令
        Map<String, Object> parsed = ModbusCommandParser.parseModbusCommand(heartbeatCmdHex);
        
        if (parsed == null) {
            log.error("心跳指令解析失败: {}", heartbeatCmdHex);
            return;
        }
        
        // 填充解析后的字段
        agricultureDeviceHeartbeat.setCmdFunctionCode((Long) parsed.get("functionCode"));
        agricultureDeviceHeartbeat.setCmdRegStart((Long) parsed.get("regStart"));
        agricultureDeviceHeartbeat.setCmdRegLength((Long) parsed.get("regLength"));
        agricultureDeviceHeartbeat.setCrc16Low((Long) parsed.get("crcLow"));
        agricultureDeviceHeartbeat.setCrc16High((Long) parsed.get("crcHigh"));
    }

    /**
     * 批量删除设备心跳状态 
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
     * 删除设备心跳状态 信息
     * 
     * @param id 设备id
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceHeartbeatById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
    
    /**
     * 更新设备在线状态（根据设备ID）
     * 
     * @param deviceId 设备ID
     * @param onlineStatus 在线状态（1=在线，0=离线）
     * @return 结果
     */
    @Override
    public int updateOnlineStatusByDeviceId(Long deviceId, Long onlineStatus) {
        if (deviceId == null) {
            log.warn("设备ID为空，跳过更新在线状态");
            return 0;
        }
        
        if (onlineStatus == null || (onlineStatus != 0L && onlineStatus != 1L)) {
            log.warn("在线状态值无效: {}, 必须是0或1", onlineStatus);
            return 0;
        }
        
        try {
            // 根据设备ID查询心跳记录
            AgricultureDeviceHeartbeat heartbeat = lambdaQuery()
                    .eq(AgricultureDeviceHeartbeat::getDeviceId, deviceId)
                    .one();
            
            if (heartbeat == null) {
                log.warn("未找到设备的心跳记录。设备ID: {}", deviceId);
                return 0;
            }
            
            // 更新在线状态
            heartbeat.setOnlineStatus(onlineStatus);
            heartbeat.setLastRecvTime(LocalDateTime.now());
            
            // 更新离线次数和最后在线时间
            if (onlineStatus == 0L) {
                // 离线时，增加离线次数，更新最后在线时间（记录最后在线时间）
                Long offlineCount = heartbeat.getOfflineCount();
                if (offlineCount == null) {
                    offlineCount = 0L;
                }
                heartbeat.setOfflineCount(offlineCount + 1);
                // 如果当前lastOnlineTime为null，则设置为当前时间（表示设备刚离线）
                if (heartbeat.getLastOnlineTime() == null) {
                    heartbeat.setLastOnlineTime(LocalDateTime.now());
                }
                
                // 同步更新设备的用户控制开关为关闭状态
                try {
                    AgricultureDevice device = agricultureDeviceService.getById(deviceId);
                    if (device != null) {
                        device.setUserControlSwitch("0");
                        agricultureDeviceService.updateById(device);
                    }
                } catch (Exception e) {
                    log.warn("更新设备用户控制开关失败。设备ID: {}", deviceId, e);
                    // 不影响心跳状态的更新，只记录警告日志
                }
            } else {
                // 在线时，重置离线次数，将最后在线时间设置为null（表示设备当前在线）
                heartbeat.setOfflineCount(0L);
                heartbeat.setLastOnlineTime(null);
            }
            
            boolean result = updateById(heartbeat);
            if (result) {
                return 1;
            } else {
                log.error("更新设备在线状态失败。设备ID: {}", deviceId);
                return 0;
            }
            
        } catch (Exception e) {
            log.error("更新设备在线状态异常。设备ID: {}", deviceId, e);
            return 0;
        }
    }
    
    /**
     * 更新设备为在线状态（根据设备ID）
     * 
     * @param deviceId 设备ID
     * @return 结果
     */
    @Override
    public int setDeviceOnline(Long deviceId) {
        return updateOnlineStatusByDeviceId(deviceId, 1L);
    }
    
    /**
     * 更新设备为离线状态（根据设备ID）
     * 
     * @param deviceId 设备ID
     * @return 结果
     */
    @Override
    public int setDeviceOffline(Long deviceId) {
        return updateOnlineStatusByDeviceId(deviceId, 0L);
    }
}

