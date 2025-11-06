package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgriculturePasture;


/**
 * 温室Service接口
 * 
 * @author bxwy
 * @date 2025-11-07
 */
public interface AgriculturePastureService  extends IService<AgriculturePasture>
{
    /**
     * 查询温室
     * 
     * @param id 温室主键
     * @return 温室
     */
    public AgriculturePasture selectAgriculturePastureById(Long id);

    /**
     * 查询温室列表
     * 
     * @param agriculturePasture 温室
     * @return 温室集合
     */
    public List<AgriculturePasture> selectAgriculturePastureList(AgriculturePasture agriculturePasture);

    /**
     * 新增温室
     * 
     * @param agriculturePasture 温室
     * @return 结果
     */
    public int insertAgriculturePasture(AgriculturePasture agriculturePasture);

    /**
     * 修改温室
     * 
     * @param agriculturePasture 温室
     * @return 结果
     */
    public int updateAgriculturePasture(AgriculturePasture agriculturePasture);

    /**
     * 批量删除温室
     * 
     * @param ids 需要删除的温室主键集合
     * @return 结果
     */
    public int deleteAgriculturePastureByIds(Long[] ids);

    /**
     * 删除温室信息
     * 
     * @param id 温室主键
     * @return 结果
     */
    public int deleteAgriculturePastureById(Long id);


}
