package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 年度计划批次关联实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_plan_batch")
@ApiModel(value = "AgriculturePlanBatch", description = "年度计划批次关联表")
public class AgriculturePlanBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    @Excel(name = "主键ID")
    private Long id;

    /** 年度计划ID（关联agriculture_annual_plan表） */
    @TableField(value = "plan_id")
    @ApiModelProperty(value = "年度计划ID（关联agriculture_annual_plan表）")
    @Excel(name = "年度计划ID")
    private Long planId;

    /** 批次ID（关联agriculture_crop_batch表） */
    @TableField(value = "batch_id")
    @ApiModelProperty(value = "批次ID（关联agriculture_crop_batch表）")
    @Excel(name = "批次ID")
    private Long batchId;

    /** 创建者 */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    @Excel(name = "创建者")
    private String createBy;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

