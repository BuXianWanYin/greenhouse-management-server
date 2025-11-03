package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureSoilData;

/**
 * 土壤8参数传感器数据Service接口
 * 
 * @author bxwy
 * @date 2025-11-03
 */
public interface AgricultureSoilDataService extends IService<AgricultureSoilData>
{
    /**
     * 查询土壤8参数传感器数据
     * 
     * @param id 土壤8参数传感器数据主键
     * @return 土壤8参数传感器数据
     */
    public AgricultureSoilData selectAgricultureSoilDataById(Long id);

    /**
     * 查询土壤8参数传感器数据列表
     * 
     * @param agricultureSoilData 土壤8参数传感器数据
     * @return 土壤8参数传感器数据集合
     */
    public List<AgricultureSoilData> selectAgricultureSoilDataList(AgricultureSoilData agricultureSoilData);

    /**
     * 新增土壤8参数传感器数据
     * 
     * @param agricultureSoilData 土壤8参数传感器数据
     * @return 结果
     */
    public int insertAgricultureSoilData(AgricultureSoilData agricultureSoilData);

    /**
     * 修改土壤8参数传感器数据
     * 
     * @param agricultureSoilData 土壤8参数传感器数据
     * @return 结果
     */
    public int updateAgricultureSoilData(AgricultureSoilData agricultureSoilData);

    /**
     * 批量删除土壤8参数传感器数据
     * 
     * @param ids 需要删除的土壤8参数传感器数据主键集合
     * @return 结果
     */
    public int deleteAgricultureSoilDataByIds(Long[] ids);

    /**
     * 删除土壤8参数传感器数据信息
     * 
     * @param id 土壤8参数传感器数据主键
     * @return 结果
     */
    public int deleteAgricultureSoilDataById(Long id);
}

