package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureCropBatch;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 种植批次Mapper接口
 *
 * @author bxwy
 * @date 2025-05-28
 */
@Repository
public interface AgricultureCropBatchMapper  extends BaseMapper<AgricultureCropBatch>
{
    /**
     * 根据条件查询作物批次信息，并包含相关的分类图片
     *
     * @param queryWrapper 查询条件Wrapper，用于动态拼接SQL
     * @return 符合条件的作物批次列表
     */
    @Select("SELECT b.*, c.class_image as class_image,c.class_name as class_name,u.nick_name as nick_name " +
            "FROM agriculture_crop_batch b " +
            "LEFT JOIN agriculture_class c ON b.class_id = c.class_id " +
            "LEFT JOIN sys_user u on u.user_id=b.responsible_person_id " +
            "${ew.customSqlSegment}")
    List<AgricultureCropBatch> selectCropBatchWithClassImages(@Param(Constants.WRAPPER) Wrapper<AgricultureCropBatch> queryWrapper);
}
