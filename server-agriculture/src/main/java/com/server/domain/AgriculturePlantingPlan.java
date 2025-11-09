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
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 种植计划实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_planting_plan")
@ApiModel(value = "AgriculturePlantingPlan", description = "种植计划表")
public class AgriculturePlantingPlan extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 种植计划ID */
    @TableId(value = "plan_id", type = IdType.AUTO)
    @ApiModelProperty(value = "种植计划ID")
    @Excel(name = "种植计划ID")
    private Long planId;

    /** 计划名称 */
    @TableField(value = "plan_name")
    @ApiModelProperty(value = "计划名称")
    @Excel(name = "计划名称")
    private String planName;

    /** 计划年份 */
    @TableField(value = "plan_year")
    @ApiModelProperty(value = "计划年份")
    @Excel(name = "计划年份")
    private Integer planYear;

    /** 计划类型（annual=年度计划,seasonal=季节性计划,rotation=轮作计划） */
    @TableField(value = "plan_type")
    @ApiModelProperty(value = "计划类型（annual=年度计划,seasonal=季度计划,rotation=轮作计划）")
    @Excel(name = "计划类型", readConverterExp = "annual=年度计划,seasonal=季度计划,rotation=轮作计划")
    private String planType;

    /** 季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季，仅用于季度计划） */
    @TableField(value = "season_type")
    @ApiModelProperty(value = "季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季，仅用于季度计划）")
    @Excel(name = "季节类型", readConverterExp = "spring=春季,summer=夏季,autumn=秋季,winter=冬季")
    private String seasonType;

    /** 父计划ID（关联agriculture_planting_plan表，年度计划的parent_plan_id为NULL，季度计划的parent_plan_id指向所属的年度计划） */
    @TableField(value = "parent_plan_id")
    @ApiModelProperty(value = "父计划ID（年度计划为NULL，季度计划指向所属的年度计划ID）")
    @Excel(name = "父计划ID")
    private Long parentPlanId;

    /** 温室ID（关联agriculture_pasture表） */
    @TableField(value = "pasture_id")
    @ApiModelProperty(value = "温室ID（关联agriculture_pasture表）")
    @Excel(name = "温室ID")
    private Long pastureId;

    /** 轮作周期（年，仅用于轮作计划） */
    @TableField(value = "rotation_cycle")
    @ApiModelProperty(value = "轮作周期（年，仅用于轮作计划）")
    @Excel(name = "轮作周期（年）")
    private Integer rotationCycle;

    /** 计划描述 */
    @TableField(value = "plan_description")
    @ApiModelProperty(value = "计划描述")
    @Excel(name = "计划描述")
    private String planDescription;

    /** 计划状态（0=未开始,1=执行中,2=已完成,3=已取消） */
    @TableField(value = "plan_status")
    @ApiModelProperty(value = "计划状态（0=未开始,1=执行中,2=已完成,3=已取消）")
    @Excel(name = "计划状态", readConverterExp = "0=未开始,1=执行中,2=已完成,3=已取消")
    private String planStatus;

    /** 计划开始日期 */
    @TableField(value = "start_date")
    @ApiModelProperty(value = "计划开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate startDate;

    /** 计划结束日期 */
    @TableField(value = "end_date")
    @ApiModelProperty(value = "计划结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate endDate;

    /** 实际开始日期（自动计算） */
    @TableField(value = "actual_start_date")
    @ApiModelProperty(value = "实际开始日期（自动计算）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate actualStartDate;

    /** 实际结束日期（自动计算） */
    @TableField(value = "actual_end_date")
    @ApiModelProperty(value = "实际结束日期（自动计算）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate actualEndDate;

    /** 计划总面积（亩） */
    @TableField(value = "total_area")
    @ApiModelProperty(value = "计划总面积（亩）")
    @Excel(name = "计划总面积（亩）")
    private Double totalArea;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;
}

