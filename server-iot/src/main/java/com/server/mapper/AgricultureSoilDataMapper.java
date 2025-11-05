package com.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureSoilData;
import com.server.domain.dto.TrendDataItem;

/**
 * 土壤8参数传感器数据Mapper接口
 * 
 * @author bxwy
 * @date 2025-11-03
 */
public interface AgricultureSoilDataMapper extends BaseMapper<AgricultureSoilData>
{
    /**
     * 查询土壤温度趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(soil_temperature), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectSoilTemperatureTrendByHour(@Param("pastureId") Long pastureId, 
                                                            @Param("startTime") String startTime, 
                                                            @Param("endTime") String endTime);

    /**
     * 查询土壤温度趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(soil_temperature), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectSoilTemperatureTrendByDay(@Param("pastureId") Long pastureId, 
                                                         @Param("startTime") String startTime, 
                                                         @Param("endTime") String endTime);

    /**
     * 查询土壤湿度趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(soil_humidity), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectSoilHumidityTrendByHour(@Param("pastureId") Long pastureId, 
                                                       @Param("startTime") String startTime, 
                                                       @Param("endTime") String endTime);

    /**
     * 查询土壤湿度趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(soil_humidity), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectSoilHumidityTrendByDay(@Param("pastureId") Long pastureId, 
                                                      @Param("startTime") String startTime, 
                                                      @Param("endTime") String endTime);

    /**
     * 查询电导率趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(conductivity), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectConductivityTrendByHour(@Param("pastureId") Long pastureId, 
                                                       @Param("startTime") String startTime, 
                                                       @Param("endTime") String endTime);

    /**
     * 查询电导率趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(conductivity), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectConductivityTrendByDay(@Param("pastureId") Long pastureId, 
                                                      @Param("startTime") String startTime, 
                                                      @Param("endTime") String endTime);

    /**
     * 查询pH值趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(ph_value), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectPhValueTrendByHour(@Param("pastureId") Long pastureId, 
                                                   @Param("startTime") String startTime, 
                                                   @Param("endTime") String endTime);

    /**
     * 查询pH值趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(ph_value), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectPhValueTrendByDay(@Param("pastureId") Long pastureId, 
                                                 @Param("startTime") String startTime, 
                                                 @Param("endTime") String endTime);

    /**
     * 查询盐分趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(salinity), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectSalinityTrendByHour(@Param("pastureId") Long pastureId, 
                                                   @Param("startTime") String startTime, 
                                                   @Param("endTime") String endTime);

    /**
     * 查询盐分趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(salinity), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectSalinityTrendByDay(@Param("pastureId") Long pastureId, 
                                                  @Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime);

    /**
     * 查询氮含量趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(nitrogen), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectNitrogenTrendByHour(@Param("pastureId") Long pastureId, 
                                                   @Param("startTime") String startTime, 
                                                   @Param("endTime") String endTime);

    /**
     * 查询氮含量趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(nitrogen), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectNitrogenTrendByDay(@Param("pastureId") Long pastureId, 
                                                  @Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime);

    /**
     * 查询磷含量趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(phosphorus), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectPhosphorusTrendByHour(@Param("pastureId") Long pastureId, 
                                                      @Param("startTime") String startTime, 
                                                      @Param("endTime") String endTime);

    /**
     * 查询磷含量趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(phosphorus), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectPhosphorusTrendByDay(@Param("pastureId") Long pastureId, 
                                                     @Param("startTime") String startTime, 
                                                     @Param("endTime") String endTime);

    /**
     * 查询钾含量趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(potassium), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectPotassiumTrendByHour(@Param("pastureId") Long pastureId, 
                                                      @Param("startTime") String startTime, 
                                                      @Param("endTime") String endTime);

    /**
     * 查询钾含量趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(potassium), 2) AS avgValue " +
            "  FROM agriculture_soil_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectPotassiumTrendByDay(@Param("pastureId") Long pastureId, 
                                                    @Param("startTime") String startTime, 
                                                    @Param("endTime") String endTime);
}

