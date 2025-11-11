package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureResource;
import com.server.domain.AgricultureResourceUsage;
import com.server.mapper.AgricultureResourceMapper;
import com.server.service.AgricultureResourceService;
import com.server.service.AgricultureResourceUsageService;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 农资资源Service业务层处理
 * 
 * @author server
 * @date 2025-01-XX
 */
@Service
public class AgricultureResourceServiceImpl extends ServiceImpl<AgricultureResourceMapper, AgricultureResource> implements AgricultureResourceService {

    @Autowired
    private AgricultureResourceMapper agricultureResourceMapper;

    @Autowired
    private AgricultureResourceUsageService agricultureResourceUsageService;

    /**
     * 查询农资资源列表
     * 
     * @param agricultureResource 农资资源
     * @return 农资资源
     */
    @Override
    public List<AgricultureResource> selectAgricultureResourceList(AgricultureResource agricultureResource) {
        LambdaQueryWrapper<AgricultureResource> lambdaQueryWrapper = new QueryWrapper<AgricultureResource>().lambda();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(agricultureResource.getResourceName()), 
                AgricultureResource::getResourceName, agricultureResource.getResourceName());
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(agricultureResource.getResourceType()), 
                AgricultureResource::getResourceType, agricultureResource.getResourceType());
        return agricultureResourceMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增农资资源
     * 
     * @param agricultureResource 农资资源
     * @return 结果
     */
    @Override
    public int addAgricultureResource(AgricultureResource agricultureResource) {
        return agricultureResourceMapper.insert(agricultureResource);
    }

    /**
     * 修改农资资源
     * 
     * @param agricultureResource 农资资源
     * @return 结果
     */
    @Override
    public int updateAgricultureResource(AgricultureResource agricultureResource) {
        return agricultureResourceMapper.updateById(agricultureResource);
    }

    /**
     * 删除农资资源信息
     * 注意：会同时删除关联的使用记录（如果数据库外键约束不是CASCADE，则需要先删除使用记录）
     * 
     * @param resourceId 农资资源主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgricultureResourceById(Long resourceId) {
        // 先删除关联的使用记录（防止外键约束错误）
        // 如果数据库外键约束已改为CASCADE，这一步数据库会自动处理，但代码层面保留以确保兼容性
        LambdaQueryWrapper<AgricultureResourceUsage> usageWrapper = new QueryWrapper<AgricultureResourceUsage>().lambda();
        usageWrapper.eq(AgricultureResourceUsage::getResourceId, resourceId);
        agricultureResourceUsageService.remove(usageWrapper);
        
        // 删除农资资源
        // 注意：库存表的外键约束是CASCADE，会自动删除关联的库存记录
        return agricultureResourceMapper.deleteById(resourceId);
    }

    /**
     * 批量删除农资资源信息
     * 重写此方法以确保批量删除时也正确处理关联的使用记录
     * 
     * @param idList 农资资源主键集合
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (idList == null || idList.isEmpty()) {
            return false;
        }
        
        // 先批量删除关联的使用记录（防止外键约束错误）
        // 如果数据库外键约束已改为CASCADE，这一步数据库会自动处理，但代码层面保留以确保兼容性
        LambdaQueryWrapper<AgricultureResourceUsage> usageWrapper = new QueryWrapper<AgricultureResourceUsage>().lambda();
        usageWrapper.in(AgricultureResourceUsage::getResourceId, idList);
        agricultureResourceUsageService.remove(usageWrapper);
        
        // 批量删除农资资源
        // 注意：库存表的外键约束是CASCADE，会自动删除关联的库存记录
        return super.removeByIds(idList);
    }
}

