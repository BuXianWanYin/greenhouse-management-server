package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureResource;

import java.util.List;

/**
 * 农资资源Service接口
 * 
 * @author server
 * @date 2025-01-XX
 */
public interface AgricultureResourceService extends IService<AgricultureResource> {

    /**
     * 查询农资资源列表
     * 
     * @param agricultureResource 农资资源
     * @return 农资资源集合
     */
    List<AgricultureResource> selectAgricultureResourceList(AgricultureResource agricultureResource);

    /**
     * 新增农资资源
     * 
     * @param agricultureResource 农资资源
     * @return 结果
     */
    int addAgricultureResource(AgricultureResource agricultureResource);

    /**
     * 修改农资资源
     * 
     * @param agricultureResource 农资资源
     * @return 结果
     */
    int updateAgricultureResource(AgricultureResource agricultureResource);

    /**
     * 删除农资资源信息
     * 
     * @param resourceId 农资资源主键
     * @return 结果
     */
    int deleteAgricultureResourceById(Long resourceId);
}

