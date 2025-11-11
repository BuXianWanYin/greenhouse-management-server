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
 * 农资库存表
 * 
 * @author server
 * @date 2025-01-XX
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_resource_inventory")
@ApiModel(value = "AgricultureResourceInventory", description = "农资库存表")
public class AgricultureResourceInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "inventory_id", type = IdType.AUTO)
    @ApiModelProperty(value = "库存ID")
    private Long inventoryId;

    @TableField(value = "resource_id")
    @ApiModelProperty(value = "农资ID")
    private Long resourceId;

    @TableField(value = "current_stock")
    @ApiModelProperty(value = "当前库存")
    private BigDecimal currentStock;

    @TableField(value = "min_stock")
    @ApiModelProperty(value = "最小库存")
    private BigDecimal minStock;

    @TableField(value = "max_stock")
    @ApiModelProperty(value = "最大库存")
    private BigDecimal maxStock;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

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

