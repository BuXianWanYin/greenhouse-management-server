package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.server.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 轮作计划实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_rotation_plan")
@ApiModel(value = "AgricultureRotationPlan", description = "轮作计划表")
public class AgricultureRotationPlan extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 轮作计划ID */
    @TableId(value = "rotation_id", type = IdType.AUTO)
    @ApiModelProperty(value = "轮作计划ID")
    @Excel(name = "轮作计划ID")
    private Long rotationId;

    /** 轮作计划名称 */
    @TableField(value = "rotation_name")
    @ApiModelProperty(value = "轮作计划名称")
    @Excel(name = "轮作计划名称")
    private String rotationName;

    /** 计划年份 */
    @TableField(value = "plan_year")
    @ApiModelProperty(value = "计划年份")
    @Excel(name = "计划年份")
    private Integer planYear;

    /** 温室ID（关联agriculture_pasture表） */
    @TableField(value = "pasture_id")
    @ApiModelProperty(value = "温室ID（关联agriculture_pasture表）")
    @Excel(name = "温室ID")
    private Long pastureId;

    /** 轮作周期（年） */
    @TableField(value = "rotation_cycle")
    @ApiModelProperty(value = "轮作周期（年）")
    @Excel(name = "轮作周期（年）")
    private Integer rotationCycle;

    /** 轮作描述 */
    @TableField(value = "rotation_description")
    @ApiModelProperty(value = "轮作描述")
    @Excel(name = "轮作描述")
    private String rotationDescription;

    /** 状态（0=草稿,1=执行中,2=已完成） */
    @TableField(value = "rotation_status")
    @ApiModelProperty(value = "状态（0=草稿,1=执行中,2=已完成）")
    @Excel(name = "状态", readConverterExp = "0=草稿,1=执行中,2=已完成")
    private String rotationStatus;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;
}

