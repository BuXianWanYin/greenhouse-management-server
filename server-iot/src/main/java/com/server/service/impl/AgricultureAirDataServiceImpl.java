package com.server.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureAirDataMapper;
import com.server.domain.AgricultureAirData;
import com.server.service.AgricultureAirDataService;

/**
 * 温度湿度光照传感器数据Service业务层处理
 * 
 * @author server
 * @date 2025-11-03
 */
@Service
public class AgricultureAirDataServiceImpl extends ServiceImpl<AgricultureAirDataMapper, AgricultureAirData> implements AgricultureAirDataService
{
    @Autowired
    private AgricultureAirDataMapper agricultureAirDataMapper;

    /**
     * 查询温度湿度光照传感器数据
     * 
     * @param id 温度湿度光照传感器数据主键
     * @return 温度湿度光照传感器数据
     */
    @Override
    public AgricultureAirData selectAgricultureAirDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询温度湿度光照传感器数据列表
     * 
     * @param agricultureAirData 温度湿度光照传感器数据
     * @return 温度湿度光照传感器数据
     */
    @Override
    public List<AgricultureAirData> selectAgricultureAirDataList(AgricultureAirData agricultureAirData)
    {
        return list();
    }

    /**
     * 新增温度湿度光照传感器数据
     * 
     * @param agricultureAirData 温度湿度光照传感器数据
     * @return 结果
     */
    @Override
    public int insertAgricultureAirData(AgricultureAirData agricultureAirData)
    {
        return agricultureAirDataMapper.insert(agricultureAirData);
    }

    /**
     * 修改温度湿度光照传感器数据
     * 
     * @param agricultureAirData 温度湿度光照传感器数据
     * @return 结果
     */
    @Override
    public int updateAgricultureAirData(AgricultureAirData agricultureAirData)
    {
        return agricultureAirDataMapper.updateById(agricultureAirData);
    }

    /**
     * 批量删除温度湿度光照传感器数据
     * 
     * @param ids 需要删除的温度湿度光照传感器数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAirDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除温度湿度光照传感器数据信息
     * 
     * @param id 温度湿度光照传感器数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureAirDataById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}

