package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.server.json.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @TableField(value="plan_id")
    @ApiModelProperty(value="种植计划ID（关联agriculture_planting_plan表）")
    private Long planId;

    @TableField(value="planting_density")
    @ApiModelProperty(value="种植密度（株/亩）")
    private Double plantingDensity;

    @TableField(value="expected_harvest_time")
    @ApiModelProperty(value="预期收获时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedHarvestTime;

    @TableField(value="actual_harvest_time")
    @ApiModelProperty(value="实际收获时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @TableField(value="status")
    @ApiModelProperty(value="状态")
    private String status;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value="harvest")
    @ApiModelProperty(value="收获标志(0代表已收获，1代表未收获)")
    private String harvest;

    /** 种质图片（关联字段，不存储到数据库） */
    @TableField(exist = false)
    @ApiModelProperty(value = "种质图片")
    private String classImage;

    /** 种质名称（关联字段，不存储到数据库） */
    @TableField(exist = false)
    @ApiModelProperty(value = "种质名称")
    private String className;

    /** 负责人昵称（关联字段，不存储到数据库） */
    @TableField(exist = false)
    @ApiModelProperty(value = "负责人昵称")
    private String nickName;
}
