package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureAirData;
import com.server.domain.dto.TrendDataVO;

/**
 * 温度湿度光照传感器数据Service接口
 * 
 * @author server
 * @date 2025-11-03
 */
public interface AgricultureAirDataService extends IService<AgricultureAirData>
{
    /**
     * 查询温度湿度光照传感器数据
     * 
     * @param id 温度湿度光照传感器数据主键
     * @return 温度湿度光照传感器数据
     */
    public AgricultureAirData selectAgricultureAirDataById(Long id);

    /**
     * 查询温度湿度光照传感器数据列表
     * 
     * @param agricultureAirData 温度湿度光照传感器数据
     * @return 温度湿度光照传感器数据集合
     */
    public List<AgricultureAirData> selectAgricultureAirDataList(AgricultureAirData agricultureAirData);

    /**
     * 新增温度湿度光照传感器数据
     * 
     * @param agricultureAirData 温度湿度光照传感器数据
     * @return 结果
     */
    public int insertAgricultureAirData(AgricultureAirData agricultureAirData);

    /**
     * 修改温度湿度光照传感器数据
     * 
     * @param agricultureAirData 温度湿度光照传感器数据
     * @return 结果
     */
    public int updateAgricultureAirData(AgricultureAirData agricultureAirData);

    /**
     * 批量删除温度湿度光照传感器数据
     * 
     * @param ids 需要删除的温度湿度光照传感器数据主键集合
     * @return 结果
     */
    public int deleteAgricultureAirDataByIds(Long[] ids);

    /**
     * 删除温度湿度光照传感器数据信息
     * 
     * @param id 温度湿度光照传感器数据主键
     * @return 结果
     */
    public int deleteAgricultureAirDataById(Long id);

    /**
     * 查询气象趋势数据
     * 
     * @param pastureId 温室ID
     * @param range 时间范围：'day'(24小时), 'week'(7天), 'month'(30天)
     * @return 趋势数据
     */
    public TrendDataVO getTrendData(Long pastureId, String range);
}

