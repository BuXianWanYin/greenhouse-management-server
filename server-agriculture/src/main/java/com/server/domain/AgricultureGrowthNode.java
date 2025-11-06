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
 * 生长关键节点实体类
 *
 * @author bxwy
 * @date 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_growth_node")
@ApiModel(value = "AgricultureGrowthNode", description = "生长关键节点表")
public class AgricultureGrowthNode extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 节点ID */
    @TableId(value = "node_id", type = IdType.AUTO)
    @ApiModelProperty(value = "节点ID")
    @Excel(name = "节点ID")
    private Long nodeId;

    /** 批次ID（关联agriculture_crop_batch表） */
    @TableField(value = "batch_id")
    @ApiModelProperty(value = "批次ID（关联agriculture_crop_batch表）")
    @Excel(name = "批次ID")
    private Long batchId;

    /** 节点类型（sowing=播种,transplanting=移栽,flowering=开花,fruiting=结果,harvest=收获） */
    @TableField(value = "node_type")
    @ApiModelProperty(value = "节点类型（sowing=播种,transplanting=移栽,flowering=开花,fruiting=结果,harvest=收获）")
    @Excel(name = "节点类型", readConverterExp = "sowing=播种,transplanting=移栽,flowering=开花,fruiting=结果,harvest=收获")
    private String nodeType;

    /** 节点名称 */
    @TableField(value = "node_name")
    @ApiModelProperty(value = "节点名称")
    @Excel(name = "节点名称")
    private String nodeName;

    /** 预期日期 */
    @TableField(value = "expected_date")
    @ApiModelProperty(value = "预期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预期日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate expectedDate;

    /** 实际日期 */
    @TableField(value = "actual_date")
    @ApiModelProperty(value = "实际日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际日期", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDate actualDate;

    /** 提前提醒天数 */
    @TableField(value = "remind_days")
    @ApiModelProperty(value = "提前提醒天数")
    @Excel(name = "提前提醒天数")
    private Integer remindDays;

    /** 提醒状态（0=未提醒,1=已提醒,2=已完成） */
    @TableField(value = "remind_status")
    @ApiModelProperty(value = "提醒状态（0=未提醒,1=已提醒,2=已完成）")
    @Excel(name = "提醒状态", readConverterExp = "0=未提醒,1=已提醒,2=已完成")
    private String remindStatus;

    /** 节点状态（0=未开始,1=进行中,2=已完成） */
    @TableField(value = "node_status")
    @ApiModelProperty(value = "节点状态（0=未开始,1=进行中,2=已完成）")
    @Excel(name = "节点状态", readConverterExp = "0=未开始,1=进行中,2=已完成")
    private String nodeStatus;

    /** 节点描述 */
    @TableField(value = "node_description")
    @ApiModelProperty(value = "节点描述")
    @Excel(name = "节点描述")
    private String nodeDescription;

    /** 节点图片（多个图片URL，逗号分隔） */
    @TableField(value = "node_images")
    @ApiModelProperty(value = "节点图片（多个图片URL，逗号分隔）")
    @Excel(name = "节点图片")
    private String nodeImages;

    /** 节点视频（多个视频URL，逗号分隔） */
    @TableField(value = "node_videos")
    @ApiModelProperty(value = "节点视频（多个视频URL，逗号分隔）")
    @Excel(name = "节点视频")
    private String nodeVideos;
}

