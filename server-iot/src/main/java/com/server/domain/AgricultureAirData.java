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

import com.server.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/** 
 * @author 851543
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_air_data")
@ApiModel(value = "AgricultureAirData" , description="温度湿度光照传感器数据表")
public class AgricultureAirData implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    @Excel(name = "主键ID", sort = 0)
    private Long id;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID")
    private Long deviceId;

    @TableField(exist = false)
    @ApiModelProperty(value="设备名称")
    @Excel(name = "设备名称", sort = 1)
    private String deviceName;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="温室ID")
    private Long pastureId;

    @TableField(exist = false)
    @ApiModelProperty(value="温室名称")
    @Excel(name = "温室名称", sort = 2)
    private String pastureName;

    @TableField(value="temperature")
    @ApiModelProperty(value="空气温度(℃)")
    @Excel(name = "温度", sort = 3, suffix = "℃")
    private Double temperature;

    @TableField(value="humidity")
    @ApiModelProperty(value="空气湿度(%)")
    @Excel(name = "湿度", sort = 4, suffix = "%")
    private Double humidity;

    @TableField(value="illuminance")
    @ApiModelProperty(value="光照度(Lux)")
    @Excel(name = "光照强度", sort = 5, suffix = "lux")
    private Double illuminance;

    @TableField(value="collect_time")
    @ApiModelProperty(value="采集时间")
    @Excel(name = "采集时间", sort = 6, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;

    @TableField(exist = false)
    @ApiModelProperty(value="查询开始时间")
    private String beginTime;

    @TableField(exist = false)
    @ApiModelProperty(value="查询结束时间")
    private String endTime;

}

