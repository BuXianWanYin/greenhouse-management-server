package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureGrowthNode;
import com.server.mapper.AgricultureGrowthNodeMapper;
import com.server.service.AgricultureGrowthNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生长关键节点Service业务层处理
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Service
public class AgricultureGrowthNodeServiceImpl extends ServiceImpl<AgricultureGrowthNodeMapper, AgricultureGrowthNode> implements AgricultureGrowthNodeService
{
    @Autowired
    private AgricultureGrowthNodeMapper agricultureGrowthNodeMapper;

    /**
     * 查询生长关键节点
     *
     * @param nodeId 生长关键节点主键
     * @return 生长关键节点
     */
    @Override
    public AgricultureGrowthNode selectAgricultureGrowthNodeByNodeId(Long nodeId)
    {
        return getById(nodeId);
    }

    /**
     * 查询生长关键节点列表
     *
     * @param agricultureGrowthNode 生长关键节点
     * @return 生长关键节点
     */
    @Override
    public List<AgricultureGrowthNode> selectAgricultureGrowthNodeList(AgricultureGrowthNode agricultureGrowthNode)
    {
        LambdaQueryWrapper<AgricultureGrowthNode> queryWrapper = new LambdaQueryWrapper<>();
        if (agricultureGrowthNode.getBatchId() != null) {
            queryWrapper.eq(AgricultureGrowthNode::getBatchId, agricultureGrowthNode.getBatchId());
        }
        if (agricultureGrowthNode.getNodeType() != null) {
            queryWrapper.eq(AgricultureGrowthNode::getNodeType, agricultureGrowthNode.getNodeType());
        }
        if (agricultureGrowthNode.getNodeStatus() != null) {
            queryWrapper.eq(AgricultureGrowthNode::getNodeStatus, agricultureGrowthNode.getNodeStatus());
        }
        if (agricultureGrowthNode.getRemindStatus() != null) {
            queryWrapper.eq(AgricultureGrowthNode::getRemindStatus, agricultureGrowthNode.getRemindStatus());
        }
        queryWrapper.orderByAsc(AgricultureGrowthNode::getExpectedDate);
        queryWrapper.orderByDesc(AgricultureGrowthNode::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 新增生长关键节点
     *
     * @param agricultureGrowthNode 生长关键节点
     * @return 结果
     */
    @Override
    public int insertAgricultureGrowthNode(AgricultureGrowthNode agricultureGrowthNode)
    {
        return save(agricultureGrowthNode) ? 1 : 0;
    }

    /**
     * 修改生长关键节点
     *
     * @param agricultureGrowthNode 生长关键节点
     * @return 结果
     */
    @Override
    public int updateAgricultureGrowthNode(AgricultureGrowthNode agricultureGrowthNode)
    {
        return updateById(agricultureGrowthNode) ? 1 : 0;
    }

    /**
     * 批量删除生长关键节点
     *
     * @param nodeIds 需要删除的生长关键节点主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureGrowthNodeByNodeIds(Long[] nodeIds)
    {
        return removeByIds(Arrays.asList(nodeIds)) ? nodeIds.length : 0;
    }

    /**
     * 删除生长关键节点信息
     *
     * @param nodeId 生长关键节点主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureGrowthNodeByNodeId(Long nodeId)
    {
        return removeById(nodeId) ? 1 : 0;
    }
}

