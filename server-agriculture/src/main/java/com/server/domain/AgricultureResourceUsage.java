package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 农资使用记录表
 * 
 * @author server
 * @date 2025-01-XX
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_resource_usage")
@ApiModel(value = "AgricultureResourceUsage", description = "农资使用记录表")
public class AgricultureResourceUsage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "usage_id", type = IdType.AUTO)
    @ApiModelProperty(value = "使用记录ID")
    private Long usageId;

    @TableField(value = "resource_id")
    @ApiModelProperty(value = "农资ID")
    private Long resourceId;

    @TableField(value = "batch_id")
    @ApiModelProperty(value = "种植批次ID")
    private Long batchId;

    @TableField(value = "task_id")
    @ApiModelProperty(value = "批次任务ID")
    private Long taskId;

    @TableField(value = "usage_quantity")
    @ApiModelProperty(value = "使用数量")
    private BigDecimal usageQuantity;

    @TableField(value = "measure_unit")
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    @TableField(value = "usage_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用日期")
    private LocalDateTime usageDate;

    @TableField(value = "usage_type")
    @ApiModelProperty(value = "使用类型(0是领用,1是消耗,2是入库)")
    private String usageType;

    @TableField(value = "operator")
    @ApiModelProperty(value = "操作人")
    private String operator;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态(0正常/已归还,1已撤销,2使用中)")
    private String status;

    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    private Long orderNum;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者ID")
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}

