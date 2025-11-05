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
@TableName(value ="agriculture_soil_data")
@ApiModel(value = "AgricultureSoilData" , description="土壤8参数传感器数据表")
public class AgricultureSoilData implements Serializable  {

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

    @TableField(value="soil_temperature")
    @ApiModelProperty(value="土壤温度(℃)")
    @Excel(name = "土壤温度", sort = 3, suffix = "℃")
    private Double soilTemperature;

    @TableField(value="soil_humidity")
    @ApiModelProperty(value="土壤湿度(m³/m³)")
    @Excel(name = "土壤湿度", sort = 4, suffix = "%")
    private Double soilHumidity;

    @TableField(value="conductivity")
    @ApiModelProperty(value="电导率(μS/cm)")
    @Excel(name = "电导率", sort = 5, suffix = "μS/cm")
    private Double conductivity;

    @TableField(value="salinity")
    @ApiModelProperty(value="盐分(mg/L)")
    @Excel(name = "盐分", sort = 6, suffix = "mg/L")
    private Double salinity;

    @TableField(value="nitrogen")
    @ApiModelProperty(value="氮含量(mg/kg)")
    @Excel(name = "氮含量", sort = 7, suffix = "mg/kg")
    private Double nitrogen;

    @TableField(value="phosphorus")
    @ApiModelProperty(value="磷含量(mg/kg)")
    @Excel(name = "磷含量", sort = 8, suffix = "mg/kg")
    private Double phosphorus;

    @TableField(value="potassium")
    @ApiModelProperty(value="钾含量(mg/kg)")
    @Excel(name = "钾含量", sort = 9, suffix = "mg/kg")
    private Double potassium;

    @TableField(value="ph_value")
    @ApiModelProperty(value="pH值")
    @Excel(name = "pH值", sort = 10)
    private Double phValue;

    @TableField(value="collect_time")
    @ApiModelProperty(value="采集时间")
    @Excel(name = "采集时间", sort = 11, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;

    @TableField(exist = false)
    @ApiModelProperty(value="查询开始时间")
    private String beginTime;

    @TableField(exist = false)
    @ApiModelProperty(value="查询结束时间")
    private String endTime;

}

