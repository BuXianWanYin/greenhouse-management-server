package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;


/** 
 * @author bxwy
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_crop_batch")
@ApiModel(value = "AgricultureCropBatch" , description="种植批次表")
public class AgricultureCropBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "batch_id", type = IdType.AUTO)
    @ApiModelProperty(value="批次ID")
    private Long batchId;

    @TableField(value="batch_name")
    @ApiModelProperty(value="批次名称")
    private String batchName;

    @TableField(value="class_id")
    @ApiModelProperty(value="种质ID（关联agriculture_class表）")
    private Long classId;

    @TableField(value="plan_year")
    @ApiModelProperty(value="计划年份")
    private Integer planYear;

    @TableField(value="season_type")
    @ApiModelProperty(value="季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季）")
    private String seasonType;

    @TableField(value="rotation_plan_id")
    @ApiModelProperty(value="轮作计划ID（关联agriculture_rotation_plan表）")
    private Long rotationPlanId;

    @TableField(value="planting_density")
    @ApiModelProperty(value="种植密度（株/亩）")
    private Double plantingDensity;

    @TableField(value="expected_harvest_time")
    @ApiModelProperty(value="预期收获时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedHarvestTime;

    @TableField(value="current_growth_stage")
    @ApiModelProperty(value="当前生长阶段（seedling=幼苗期,growth=生长期,flowering=开花期,fruiting=结果期,mature=成熟期）")
    private String currentGrowthStage;

    @TableField(value="growth_stage_start_time")
    @ApiModelProperty(value="当前生长阶段开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime growthStageStartTime;

    @TableField(value="total_growth_days")
    @ApiModelProperty(value="总生长天数")
    private Integer totalGrowthDays;

    @TableField(value="actual_harvest_time")
    @ApiModelProperty(value="实际收获时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualHarvestTime;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="温室ID")
    private Long pastureId;

    @TableField(value="crop_area")
    @ApiModelProperty(value="种植面积(亩)")
    private Double cropArea;

    @TableField(value="start_time")
    @ApiModelProperty(value="开始时间")
    private LocalDateTime startTime;

    @TableField(value="status")
    @ApiModelProperty(value="状态")
    private String status;

    @TableField(value="order_num")
    @ApiModelProperty(value="排序")
    private Long orderNum;

    @TableField(value="del_flag")
    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @TableField(value="responsible_person_id")
    @ApiModelProperty(value="负责人Id")
    private Long responsiblePersonId;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者ID")
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value="harvest")
    @ApiModelProperty(value="收获标志(0代表已收获，1代表未收获)")
    private String harvest;
}
