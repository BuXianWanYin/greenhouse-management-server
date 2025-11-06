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
 * 生长阶段实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_growth_stage")
@ApiModel(value = "AgricultureGrowthStage", description = "生长阶段表")
public class AgricultureGrowthStage extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 生长阶段ID */
    @TableId(value = "stage_id", type = IdType.AUTO)
    @ApiModelProperty(value = "生长阶段ID")
    @Excel(name = "生长阶段ID")
    private Long stageId;

    /** 批次ID（关联agriculture_crop_batch表） */
    @TableField(value = "batch_id")
    @ApiModelProperty(value = "批次ID（关联agriculture_crop_batch表）")
    @Excel(name = "批次ID")
    private Long batchId;

    /** 生长阶段类型（seedling=幼苗期,growth=生长期,flowering=开花期,fruiting=结果期,mature=成熟期） */
    @TableField(value = "stage_type")
    @ApiModelProperty(value = "生长阶段类型（seedling=幼苗期,growth=生长期,flowering=开花期,fruiting=结果期,mature=成熟期）")
    @Excel(name = "生长阶段类型", readConverterExp = "seedling=幼苗期,growth=生长期,flowering=开花期,fruiting=结果期,mature=成熟期")
    private String stageType;

    /** 阶段名称 */
    @TableField(value = "stage_name")
    @ApiModelProperty(value = "阶段名称")
    @Excel(name = "阶段名称")
    private String stageName;

    /** 阶段顺序（1,2,3...） */
    @TableField(value = "stage_order")
    @ApiModelProperty(value = "阶段顺序（1,2,3...）")
    @Excel(name = "阶段顺序")
    private Integer stageOrder;

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

    /** 阶段状态（0=未开始,1=进行中,2=已完成） */
    @TableField(value = "stage_status")
    @ApiModelProperty(value = "阶段状态（0=未开始,1=进行中,2=已完成）")
    @Excel(name = "阶段状态", readConverterExp = "0=未开始,1=进行中,2=已完成")
    private String stageStatus;

    /** 阶段描述 */
    @TableField(value = "stage_description")
    @ApiModelProperty(value = "阶段描述")
    @Excel(name = "阶段描述")
    private String stageDescription;
}

