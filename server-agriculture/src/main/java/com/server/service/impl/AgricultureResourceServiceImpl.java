package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureResource;
import com.server.mapper.AgricultureResourceMapper;
import com.server.service.AgricultureResourceService;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 
     * @param resourceId 农资资源主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureResourceById(Long resourceId) {
        return agricultureResourceMapper.deleteById(resourceId);
    }
}

