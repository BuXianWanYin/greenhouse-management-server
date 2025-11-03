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
    private Long id;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID")
    private Long deviceId;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="温室ID")
    private Long pastureId;

    @TableField(value="temperature")
    @ApiModelProperty(value="空气温度(℃)")
    private Double temperature;

    @TableField(value="humidity")
    @ApiModelProperty(value="空气湿度(%)")
    private Double humidity;

    @TableField(value="illuminance")
    @ApiModelProperty(value="光照度(Lux)")
    private Double illuminance;

    @TableField(value="collect_time")
    @ApiModelProperty(value="采集时间")
    private LocalDateTime collectTime;

}

