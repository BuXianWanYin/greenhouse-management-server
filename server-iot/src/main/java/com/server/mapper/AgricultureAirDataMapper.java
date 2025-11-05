package com.server.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureAirData;
import com.server.domain.dto.TrendDataItem;

/**
 * 温度湿度光照传感器数据Mapper接口
 * 
 * @author server
 * @date 2025-11-03
 */
public interface AgricultureAirDataMapper extends BaseMapper<AgricultureAirData>
{
    /**
     * 查询温度趋势数据（按小时聚合）
     * @param pastureId 温室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 趋势数据列表
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(temperature), 2) AS avgValue " +
            "  FROM agriculture_air_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectTemperatureTrendByHour(@Param("pastureId") Long pastureId, 
                                                       @Param("startTime") String startTime, 
                                                       @Param("endTime") String endTime);

    /**
     * 查询温度趋势数据（按天聚合）
     * @param pastureId 温室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 趋势数据列表
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(temperature), 2) AS avgValue " +
            "  FROM agriculture_air_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectTemperatureTrendByDay(@Param("pastureId") Long pastureId, 
                                                     @Param("startTime") String startTime, 
                                                     @Param("endTime") String endTime);

    /**
     * 查询湿度趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(humidity), 2) AS avgValue " +
            "  FROM agriculture_air_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectHumidityTrendByHour(@Param("pastureId") Long pastureId, 
                                                  @Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime);

    /**
     * 查询湿度趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(humidity), 2) AS avgValue " +
            "  FROM agriculture_air_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectHumidityTrendByDay(@Param("pastureId") Long pastureId, 
                                                  @Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime);

    /**
     * 查询光照强度趋势数据（按小时聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%H:00') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') AS time_grouped, " +
            "         ROUND(AVG(illuminance), 2) AS avgValue " +
            "  FROM agriculture_air_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE_FORMAT(collect_time, '%Y-%m-%d %H:00:00') " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectLightIntensityTrendByHour(@Param("pastureId") Long pastureId, 
                                                        @Param("startTime") String startTime, 
                                                        @Param("endTime") String endTime);

    /**
     * 查询光照强度趋势数据（按天聚合）
     */
    @Select("SELECT DATE_FORMAT(time_grouped, '%m/%d') AS timePoint, " +
            "time_grouped AS time, " +
            "avgValue " +
            "FROM ( " +
            "  SELECT DATE(collect_time) AS time_grouped, " +
            "         ROUND(AVG(illuminance), 2) AS avgValue " +
            "  FROM agriculture_air_data " +
            "  WHERE pasture_id = #{pastureId} " +
            "  AND collect_time >= #{startTime} AND collect_time <= #{endTime} " +
            "  GROUP BY DATE(collect_time) " +
            ") AS t " +
            "ORDER BY time_grouped ASC")
    List<TrendDataItem> selectLightIntensityTrendByDay(@Param("pastureId") Long pastureId, 
                                                        @Param("startTime") String startTime, 
                                                        @Param("endTime") String endTime);
}

