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
@TableName(value ="agriculture_soil_data")
@ApiModel(value = "AgricultureSoilData" , description="土壤8参数传感器数据表")
public class AgricultureSoilData implements Serializable  {

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

    @TableField(value="soil_temperature")
    @ApiModelProperty(value="土壤温度(℃)")
    private Double soilTemperature;

    @TableField(value="soil_humidity")
    @ApiModelProperty(value="土壤湿度(m³/m³)")
    private Double soilHumidity;

    @TableField(value="conductivity")
    @ApiModelProperty(value="电导率(μS/cm)")
    private Double conductivity;

    @TableField(value="salinity")
    @ApiModelProperty(value="盐分(mg/L)")
    private Double salinity;

    @TableField(value="nitrogen")
    @ApiModelProperty(value="氮含量(mg/kg)")
    private Double nitrogen;

    @TableField(value="phosphorus")
    @ApiModelProperty(value="磷含量(mg/kg)")
    private Double phosphorus;

    @TableField(value="potassium")
    @ApiModelProperty(value="钾含量(mg/kg)")
    private Double potassium;

    @TableField(value="ph_value")
    @ApiModelProperty(value="pH值")
    private Double phValue;

    @TableField(value="collect_time")
    @ApiModelProperty(value="采集时间")
    private LocalDateTime collectTime;

}

