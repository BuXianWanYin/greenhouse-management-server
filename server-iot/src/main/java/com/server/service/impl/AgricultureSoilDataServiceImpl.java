package com.server.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureSoilDataMapper;
import com.server.domain.AgricultureSoilData;
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
        return list();
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
}

