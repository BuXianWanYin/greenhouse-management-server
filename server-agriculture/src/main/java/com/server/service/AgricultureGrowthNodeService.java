package com.server.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureGrowthNode;

/**
 * 生长关键节点Service接口
 *
 * @author bxwu
 * @date 2025-11-05
 */
public interface AgricultureGrowthNodeService extends IService<AgricultureGrowthNode>
{
    /**
     * 查询生长关键节点
     *
     * @param nodeId 生长关键节点主键
     * @return 生长关键节点
     */
    public AgricultureGrowthNode selectAgricultureGrowthNodeByNodeId(Long nodeId);

    /**
     * 查询生长关键节点列表
     *
     * @param agricultureGrowthNode 生长关键节点
     * @return 生长关键节点集合
     */
    public List<AgricultureGrowthNode> selectAgricultureGrowthNodeList(AgricultureGrowthNode agricultureGrowthNode);

    /**
     * 新增生长关键节点
     *
     * @param agricultureGrowthNode 生长关键节点
     * @return 结果
     */
    public int insertAgricultureGrowthNode(AgricultureGrowthNode agricultureGrowthNode);

    /**
     * 修改生长关键节点
     *
     * @param agricultureGrowthNode 生长关键节点
     * @return 结果
     */
    public int updateAgricultureGrowthNode(AgricultureGrowthNode agricultureGrowthNode);

    /**
     * 批量删除生长关键节点
     *
     * @param nodeIds 需要删除的生长关键节点主键集合
     * @return 结果
     */
    public int deleteAgricultureGrowthNodeByNodeIds(Long[] nodeIds);

    /**
     * 删除生长关键节点信息
     *
     * @param nodeId 生长关键节点主键
     * @return 结果
     */
    public int deleteAgricultureGrowthNodeByNodeId(Long nodeId);
}

