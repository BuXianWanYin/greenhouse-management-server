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
 * 年度种植规划实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_annual_plan")
@ApiModel(value = "AgricultureAnnualPlan", description = "年度种植规划表")
public class AgricultureAnnualPlan extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 计划ID */
    @TableId(value = "plan_id", type = IdType.AUTO)
    @ApiModelProperty(value = "计划ID")
    @Excel(name = "计划ID")
    private Long planId;

    /** 计划年份 */
    @TableField(value = "plan_year")
    @ApiModelProperty(value = "计划年份")
    @Excel(name = "计划年份")
    private Integer planYear;

    /** 计划名称 */
    @TableField(value = "plan_name")
    @ApiModelProperty(value = "计划名称")
    @Excel(name = "计划名称")
    private String planName;

    /** 温室ID（关联agriculture_pasture表，NULL表示全温室） */
    @TableField(value = "pasture_id")
    @ApiModelProperty(value = "温室ID（关联agriculture_pasture表，NULL表示全温室）")
    @Excel(name = "温室ID")
    private Long pastureId;

    /** 计划类型（annual=年度计划,seasonal=季节性计划） */
    @TableField(value = "plan_type")
    @ApiModelProperty(value = "计划类型（annual=年度计划,seasonal=季度计划）")
    @Excel(name = "计划类型", readConverterExp = "annual=年度计划,seasonal=季度计划")
    private String planType;

    /** 计划状态（0=草稿,1=已发布,2=执行中,3=已完成,4=已取消） */
    @TableField(value = "plan_status")
    @ApiModelProperty(value = "计划状态（0=草稿,1=已发布,2=执行中,3=已完成,4=已取消）")
    @Excel(name = "计划状态", readConverterExp = "0=草稿,1=已发布,2=执行中,3=已完成,4=已取消")
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

    /** 计划总面积（亩） */
    @TableField(value = "total_area")
    @ApiModelProperty(value = "计划总面积（亩）")
    @Excel(name = "计划总面积（亩）")
    private Double totalArea;

    /** 计划描述 */
    @TableField(value = "plan_description")
    @ApiModelProperty(value = "计划描述")
    @Excel(name = "计划描述")
    private String planDescription;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;
}

