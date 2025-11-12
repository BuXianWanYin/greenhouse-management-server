package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bxwy
 * @description 采摘信息表
 */
@ApiModel(value = "AgriculturePartitionFood", description = "采摘信息表")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(value = "agriculture_partition_food")
public class AgriculturePartitionFood extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "采摘ID")
    private Long id;

    @ApiModelProperty(value = "批次id", example = "123", required = true)
    @NotBlank(message = "批次id为空")
    @TableField(value = "ia_partition_id")
    private String iaPartitionId;

    @ApiModelProperty(value = "种质ID", example = "1", required = true)
    @NotNull(message = "种质ID为空")
    @TableField(value = "class_id")
    private Long classId;

    @ApiModelProperty(value = "采摘日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "date")
    private Date date;

    @ApiModelProperty(value = "采摘重量(kg)", example = "123.22")
    @TableField(value = "weight")
    private Double weight;
}
