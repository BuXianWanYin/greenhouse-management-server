package com.server.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgriculturePartitionFood;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
import com.server.domain.vo.TraceabilityDetailVO;

/**
 * 采摘食品Service接口
 * 
 * @author bxwy
 * @date 2025-06-24
 */
public interface AgriculturePartitionFoodService extends IService<AgriculturePartitionFood>
{

    /**
     * 查询采摘食品
     * 
     * @param id 采摘食品主键
     * @return 采摘食品
     */
    public AgriculturePartitionFood selectagriculturePartitionFoodById(String id);

    /**
     * 查询采摘食品列表
     * 
     * @param agriculturePartitionFood 采摘食品
     * @return 采摘食品集合
     */
    public List<AgriculturePartitionFood> selectagriculturePartitionFoodList(AgriculturePartitionFood agriculturePartitionFood);

    /**
     * 新增采摘食品
     * 
     * @param agriculturePartitionFood 采摘食品
     * @return 结果
     */
    public int insertagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood);

    /**
     * 修改采摘食品
     * 
     * @param agriculturePartitionFood 采摘食品
     * @return 结果
     */
    public int updateagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood);

    /**
     * 批量删除采摘食品
     * 
     * @param ids 需要删除的采摘食品主键集合
     * @return 结果
     */
    public int deleteagriculturePartitionFoodByIds(String[] ids);

    /**
     * 删除采摘食品信息
     * 
     * @param id 采摘食品主键
     * @return 结果
     */
    public int deleteagriculturePartitionFoodById(String id);

    /**
     * 新增采摘 生成二维码展示
     */
    List<AgriculturePartitionFood> fy(AgriculturePartitionFoodPageDTO baseDTO);
    /**
     * 根据溯源码查询溯源详情信息，包括批次、温室、批次任务、环境数据等
     * @param traceId 溯源码（溯源id）
     * @param queryIp 查询IP
     * @param userAgent 用户代理
     * @param queryType 查询类型
     * @return TraceabilityDetailVO 溯源详情VO
     * @throws RuntimeException 如果溯源信息不存在
     */
    TraceabilityDetailVO getTraceabilityDetailById(String traceId, String queryIp, String userAgent, String queryType, Date firstTraceTime);

    // 重载原来的方法
    TraceabilityDetailVO getTraceabilityDetailById(String traceId);

}
