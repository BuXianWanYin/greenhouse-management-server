package com.server.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureSoilDataMapper;
import com.server.domain.AgricultureSoilData;
import com.server.domain.dto.TrendDataItem;
import com.server.domain.dto.TrendDataVO;
import com.server.service.AgricultureSoilDataService;

/**
 * 土壤8参数传感器数据Service业务层处理
 * 
 * @author server
 * @date 2025-11-03
 */
@Service
public class AgricultureSoilDataServiceImpl extends ServiceImpl<AgricultureSoilDataMapper, AgricultureSoilData> implements AgricultureSoilDataService
{
    @Autowired
    private AgricultureSoilDataMapper agricultureSoilDataMapper;

    /**
     * 查询土壤8参数传感器数据
     * 
     * @param id 土壤8参数传感器数据主键
     * @return 土壤8参数传感器数据
     */
    @Override
    public AgricultureSoilData selectAgricultureSoilDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询土壤8参数传感器数据列表
     * 
     * @param agricultureSoilData 土壤8参数传感器数据
     * @return 土壤8参数传感器数据
     */
    @Override
    public List<AgricultureSoilData> selectAgricultureSoilDataList(AgricultureSoilData agricultureSoilData)
    {
        // 创建 LambdaQueryWrapper 用于构建动态查询条件
        LambdaQueryWrapper<AgricultureSoilData> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据 pastureId 查询
        if (agricultureSoilData != null && agricultureSoilData.getPastureId() != null) {
            queryWrapper.eq(AgricultureSoilData::getPastureId, agricultureSoilData.getPastureId());
        }
        
        // 根据 deviceId 查询
        if (agricultureSoilData != null && agricultureSoilData.getDeviceId() != null) {
            queryWrapper.eq(AgricultureSoilData::getDeviceId, agricultureSoilData.getDeviceId());
        }
        
        // 根据时间范围查询
        if (agricultureSoilData != null && StringUtils.isNotEmpty(agricultureSoilData.getBeginTime())) {
            try {
                LocalDateTime beginTime = LocalDateTime.parse(agricultureSoilData.getBeginTime(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.ge(AgricultureSoilData::getCollectTime, beginTime);
            } catch (Exception e) {
                // 时间格式解析失败，忽略该条件
            }
        }
        
        if (agricultureSoilData != null && StringUtils.isNotEmpty(agricultureSoilData.getEndTime())) {
            try {
                LocalDateTime endTime = LocalDateTime.parse(agricultureSoilData.getEndTime(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                queryWrapper.le(AgricultureSoilData::getCollectTime, endTime);
            } catch (Exception e) {
                // 时间格式解析失败，忽略该条件
            }
        }
        
        // 按采集时间降序排序（最新的数据在前面）
        queryWrapper.orderByDesc(AgricultureSoilData::getCollectTime);
        
        // 执行查询
        return list(queryWrapper);
    }

    /**
     * 新增土壤8参数传感器数据
     * 
     * @param agricultureSoilData 土壤8参数传感器数据
     * @return 结果
     */
    @Override
    public int insertAgricultureSoilData(AgricultureSoilData agricultureSoilData)
    {
        return agricultureSoilDataMapper.insert(agricultureSoilData);
    }

    /**
     * 修改土壤8参数传感器数据
     * 
     * @param agricultureSoilData 土壤8参数传感器数据
     * @return 结果
     */
    @Override
    public int updateAgricultureSoilData(AgricultureSoilData agricultureSoilData)
    {
        return agricultureSoilDataMapper.updateById(agricultureSoilData);
    }

    /**
     * 批量删除土壤8参数传感器数据
     * 
     * @param ids 需要删除的土壤8参数传感器数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureSoilDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除土壤8参数传感器数据信息
     * 
     * @param id 土壤8参数传感器数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureSoilDataById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 查询土壤趋势数据
     * 
     * @param pastureId 温室ID
     * @param range 时间范围：'day'(24小时), 'week'(7天), 'month'(30天)
     * @return 趋势数据
     */
    @Override
    public TrendDataVO getTrendData(Long pastureId, String range) {
        if (pastureId == null) {
            return new TrendDataVO();
        }

        // 计算时间范围
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTimeForQuery = now; // 查询时使用当前时间，确保包含最新数据
        LocalDateTime startTime;
        LocalDateTime endTimeForAxis; // 时间轴显示的结束时间
        boolean isHourly = false;

        if ("day".equals(range)) {
            // 24小时数据，按小时聚合
            // 查询范围：从当前时间往前推24小时到当前时间
            startTime = now.minusHours(24);
            // 时间轴显示：从当前小时的整点往前推23小时，生成24个时间点（包括当前小时）
            endTimeForAxis = now.withMinute(0).withSecond(0).withNano(0);
            isHourly = true;
        } else if ("week".equals(range)) {
            // 7天数据，按天聚合
            // 查询范围：从今天往前推7天到当前时间
            startTime = now.minusDays(7).withHour(0).withMinute(0).withSecond(0).withNano(0);
            // 时间轴显示：从今天往前推6天，生成7个时间点（包括今天）
            endTimeForAxis = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else if ("month".equals(range)) {
            // 30天数据，按天聚合
            // 查询范围：从今天往前推30天到当前时间
            startTime = now.minusDays(30).withHour(0).withMinute(0).withSecond(0).withNano(0);
            // 时间轴显示：从今天往前推29天，生成30个时间点（包括今天）
            endTimeForAxis = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else {
            // 默认24小时
            startTime = now.minusHours(24);
            endTimeForAxis = now.withMinute(0).withSecond(0).withNano(0);
            isHourly = true;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = startTime.format(formatter);
        String endTimeStr = endTimeForQuery.format(formatter); // 查询时使用当前时间

        TrendDataVO result = new TrendDataVO();
        
        // 生成完整的时间轴（用于显示）
        List<String> xAxis = generateTimeAxis(isHourly, startTime, endTimeForAxis);

        // 查询各参数数据
        List<TrendDataItem> tempData = isHourly 
            ? agricultureSoilDataMapper.selectSoilTemperatureTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectSoilTemperatureTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> humidityData = isHourly
            ? agricultureSoilDataMapper.selectSoilHumidityTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectSoilHumidityTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> conductivityData = isHourly
            ? agricultureSoilDataMapper.selectConductivityTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectConductivityTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> phData = isHourly
            ? agricultureSoilDataMapper.selectPhValueTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectPhValueTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> salinityData = isHourly
            ? agricultureSoilDataMapper.selectSalinityTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectSalinityTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> nitrogenData = isHourly
            ? agricultureSoilDataMapper.selectNitrogenTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectNitrogenTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> phosphorusData = isHourly
            ? agricultureSoilDataMapper.selectPhosphorusTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectPhosphorusTrendByDay(pastureId, startTimeStr, endTimeStr);

        List<TrendDataItem> potassiumData = isHourly
            ? agricultureSoilDataMapper.selectPotassiumTrendByHour(pastureId, startTimeStr, endTimeStr)
            : agricultureSoilDataMapper.selectPotassiumTrendByDay(pastureId, startTimeStr, endTimeStr);

        // 将查询结果转换为Map，方便查找
        Map<String, Double> tempMap = new HashMap<>();
        for (TrendDataItem item : tempData) {
            tempMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> humidityMap = new HashMap<>();
        for (TrendDataItem item : humidityData) {
            humidityMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> conductivityMap = new HashMap<>();
        for (TrendDataItem item : conductivityData) {
            conductivityMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> phMap = new HashMap<>();
        for (TrendDataItem item : phData) {
            phMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> salinityMap = new HashMap<>();
        for (TrendDataItem item : salinityData) {
            salinityMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> nitrogenMap = new HashMap<>();
        for (TrendDataItem item : nitrogenData) {
            nitrogenMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> phosphorusMap = new HashMap<>();
        for (TrendDataItem item : phosphorusData) {
            phosphorusMap.put(item.getTimePoint(), item.getAvgValue());
        }
        
        Map<String, Double> potassiumMap = new HashMap<>();
        for (TrendDataItem item : potassiumData) {
            potassiumMap.put(item.getTimePoint(), item.getAvgValue());
        }

        // 按完整时间轴填充数据，缺失数据填充0
        List<Double> soilTemperature = new ArrayList<>();
        List<Double> soilHumidity = new ArrayList<>();
        List<Double> conductivity = new ArrayList<>();
        List<Double> phValue = new ArrayList<>();
        List<Double> salinity = new ArrayList<>();
        List<Double> nitrogen = new ArrayList<>();
        List<Double> phosphorus = new ArrayList<>();
        List<Double> potassium = new ArrayList<>();
        
        for (String timePoint : xAxis) {
            soilTemperature.add(tempMap.getOrDefault(timePoint, 0.0));
            soilHumidity.add(humidityMap.getOrDefault(timePoint, 0.0));
            conductivity.add(conductivityMap.getOrDefault(timePoint, 0.0));
            phValue.add(phMap.getOrDefault(timePoint, 0.0));
            salinity.add(salinityMap.getOrDefault(timePoint, 0.0));
            nitrogen.add(nitrogenMap.getOrDefault(timePoint, 0.0));
            phosphorus.add(phosphorusMap.getOrDefault(timePoint, 0.0));
            potassium.add(potassiumMap.getOrDefault(timePoint, 0.0));
        }

        result.setXAxis(xAxis);
        result.setSoilTemperature(soilTemperature);
        result.setSoilHumidity(soilHumidity);
        result.setConductivity(conductivity);
        result.setPhValue(phValue);
        result.setSalinity(salinity);
        result.setNitrogen(nitrogen);
        result.setPhosphorus(phosphorus);
        result.setPotassium(potassium);

        return result;
    }

    /**
     * 生成完整的时间轴
     * @param isHourly 是否按小时
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间轴列表
     */
    private List<String> generateTimeAxis(boolean isHourly, LocalDateTime startTime, LocalDateTime endTimeForAxis) {
        List<String> timeAxis = new ArrayList<>();
        
        if (isHourly) {
            // 生成24小时时间轴（固定24个点）
            // 从当前小时的整点往前推23小时
            LocalDateTime current = endTimeForAxis.minusHours(23);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
            for (int i = 0; i < 24; i++) {
                timeAxis.add(current.format(formatter));
                current = current.plusHours(1);
            }
        } else {
            // 生成按天的时间轴（7天或30天）
            // 计算实际天数差
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startTime.toLocalDate(), endTimeForAxis.toLocalDate()) + 1;
            int days = (int) daysBetween;
            
            // 从endTimeForAxis往前推(days-1)天，生成days个时间点
            LocalDateTime current = endTimeForAxis.minusDays(days - 1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
            
            for (int i = 0; i < days; i++) {
                timeAxis.add(current.format(formatter));
                current = current.plusDays(1);
            }
        }
        
        return timeAxis;
    }
}

