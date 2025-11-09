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
 * 种植计划明细实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_plan_detail")
@ApiModel(value = "AgriculturePlanDetail", description = "轮作计划明细表")
public class AgriculturePlanDetail extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    @TableId(value = "detail_id", type = IdType.AUTO)
    @ApiModelProperty(value = "明细ID")
    @Excel(name = "明细ID")
    private Long detailId;

    /** 种植计划ID（关联agriculture_planting_plan表） */
    @TableField(value = "plan_id")
    @ApiModelProperty(value = "种植计划ID（关联agriculture_planting_plan表）")
    @Excel(name = "种植计划ID")
    private Long planId;

    /** 种质ID（关联agriculture_class表） */
    @TableField(value = "class_id")
    @ApiModelProperty(value = "种质ID（关联agriculture_class表）")
    @Excel(name = "种质ID")
    private Long classId;

    /** 轮作顺序（1,2,3...） */
    @TableField(value = "rotation_order")
    @ApiModelProperty(value = "轮作顺序（1,2,3...）")
    @Excel(name = "轮作顺序")
    private Integer rotationOrder;

    /** 季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季） */
    @TableField(value = "season_type")
    @ApiModelProperty(value = "季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季）")
    @Excel(name = "季节类型", readConverterExp = "spring=春季,summer=夏季,autumn=秋季,winter=冬季")
    private String seasonType;

    /** 种植面积（亩） */
    @TableField(value = "planting_area")
    @ApiModelProperty(value = "种植面积（亩）")
    @Excel(name = "种植面积（亩）")
    private Double plantingArea;

    /** 种植密度（株/亩） */
    @TableField(value = "planting_density")
    @ApiModelProperty(value = "种植密度（株/亩）")
    @Excel(name = "种植密度（株/亩）")
    private Double plantingDensity;

    /** 预期开始日期 */
    @TableField(value = "expected_start_date")
    @ApiModelProperty(value = "预期开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预期开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate expectedStartDate;

    /** 预期结束日期 */
    @TableField(value = "expected_end_date")
    @ApiModelProperty(value = "预期结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预期结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate expectedEndDate;

    /** 实际开始日期 */
    @TableField(value = "actual_start_date")
    @ApiModelProperty(value = "实际开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate actualStartDate;

    /** 实际结束日期 */
    @TableField(value = "actual_end_date")
    @ApiModelProperty(value = "实际结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate actualEndDate;
}

