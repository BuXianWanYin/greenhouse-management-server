package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/** 
 * @author bxwy
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_device_heartbeat")
@ApiModel(value = "AgricultureDeviceHeartbeat" , description="设备心跳状态表")
public class AgricultureDeviceHeartbeat extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    private Long id;

    @TableField(value="device_id")
    @ApiModelProperty(value="关联设备表的主键id（外键）")
    private Long deviceId;

    @TableField(value="heartbeat_cmd_hex")
    @ApiModelProperty(value="心跳查询指令（十六进制，空格分隔）")
    private String heartbeatCmdHex;

    @TableField(value="cmd_function_code")
    @ApiModelProperty(value="Modbus功能码")
    private Long cmdFunctionCode;

    @TableField(value="cmd_reg_start")
    @ApiModelProperty(value="起始寄存器地址（十进制）")
    private Long cmdRegStart;

    @TableField(value="cmd_reg_length")
    @ApiModelProperty(value="读取寄存器长度（十进制）")
    private Long cmdRegLength;

    @TableField(value="crc16_low")
    @ApiModelProperty(value="CRC16校验低位")
    private Long crc16Low;

    @TableField(value="crc16_high")
    @ApiModelProperty(value="CRC16校验高位")
    private Long crc16High;

    @TableField(value="last_send_time")
    @ApiModelProperty(value="最近发送时间")
    private LocalDateTime lastSendTime;

    @TableField(value="last_recv_time")
    @ApiModelProperty(value="最近接收时间")
    private LocalDateTime lastRecvTime;

    @TableField(value="online_status")
    @ApiModelProperty(value="在线状态（1=在线，0=离线）")
    private Long onlineStatus;

    @TableField(value="offline_count")
    @ApiModelProperty(value="连续离线次数")
    private Long offlineCount;

    @TableField(value="send_interval")
    @ApiModelProperty(value="指令发送间隔（秒）")
    private Long sendInterval;

    @TableField(value="last_online_time")
    @ApiModelProperty(value="最后在线时间")
    private LocalDateTime lastOnlineTime;

    // 覆盖 BaseEntityPlus 中的字段，标记为不存在
    @TableField(value = "create_by", exist = false)
    private String createBy;
    
    @TableField(value = "create_time", exist = false)
    private LocalDateTime createTime;
    
    @TableField(value = "update_by", exist = false)
    private String updateBy;
    
    @TableField(value = "update_time", exist = false)
    private LocalDateTime updateTime;
    
    @TableField(value = "remark", exist = false)
    private String remark;

}
