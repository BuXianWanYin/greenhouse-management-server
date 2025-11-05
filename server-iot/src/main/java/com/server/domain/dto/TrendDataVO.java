package com.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 趋势数据响应对象
 * 用于返回24小时、7天、30天的聚合数据
 * 
 * @author system
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendDataVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间轴数据（格式：24小时为 "HH:00"，7天/30天为 "MM/dd"）
     */
    private List<String> xAxis;

    /**
     * 气象数据 - 温度
     */
    private List<Double> temperature;

    /**
     * 气象数据 - 湿度
     */
    private List<Double> humidity;

    /**
     * 气象数据 - 光照强度
     */
    private List<Double> lightIntensity;

    /**
     * 土壤数据 - 土壤温度
     */
    private List<Double> soilTemperature;

    /**
     * 土壤数据 - 土壤湿度
     */
    private List<Double> soilHumidity;

    /**
     * 土壤数据 - 电导率
     */
    private List<Double> conductivity;

    /**
     * 土壤数据 - pH值
     */
    private List<Double> phValue;

    /**
     * 土壤数据 - 盐分
     */
    private List<Double> salinity;

    /**
     * 土壤数据 - 氮含量
     */
    private List<Double> nitrogen;

    /**
     * 土壤数据 - 磷含量
     */
    private List<Double> phosphorus;

    /**
     * 土壤数据 - 钾含量
     */
    private List<Double> potassium;
}

