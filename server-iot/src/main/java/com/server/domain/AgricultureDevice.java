package com.server.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备信息对象 agriculture_device
 *
 * @author bxwy
 * @date 2025-05-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_device")
@ApiModel(value = "AgricultureDevice" , description="设备信息表")
public class AgricultureDevice extends BaseEntityPlus implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="设备ID")
    private Long id;

    /** 设备名称 */
    @TableField(value="device_name")
    @ApiModelProperty(value="设备名称")
    private String deviceName;
    
    /** 设备类型（传感器、控制器、网关等） */
    @TableField(value="device_image")
    @ApiModelProperty(value="设备图片URL")
    private String deviceImage;

    /** 设备类型（传感器、控制器、网关等） */
    @TableField(value="device_type_id")
    @ApiModelProperty(value="设备类型（传感器、控制器、网关等）")
    private String deviceTypeId;

    /** 告警状态（0-正常，1-告警） */
    @TableField(value="alarm_status")
    @ApiModelProperty(value="告警状态（0-正常，1-告警）")
    private String alarmStatus;

    /** 最后在线时间 */
    @TableField(value="last_online_time")
    @ApiModelProperty(value="最后在线时间")
    private Date lastOnlineTime;

    /** 大棚id，空为没有绑定大棚 */
    @TableField(value="pasture_id")
    @ApiModelProperty(value="温室id")
    private String pastureId;

    /** 传感器采集指令 */
    @TableField(value="sensor_command")
    @ApiModelProperty(value="传感器采集指令")
    private String sensorCommand;
}

