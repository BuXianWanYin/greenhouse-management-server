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
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 农资资源表
 * 
 * @author server
 * @date 2025-01-XX
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_resource")
@ApiModel(value = "AgricultureResource", description = "农资资源表")
public class AgricultureResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "resource_id", type = IdType.AUTO)
    @ApiModelProperty(value = "农资ID")
    private Long resourceId;

    @TableField(value = "resource_code")
    @ApiModelProperty(value = "农资编码")
    private String resourceCode;

    @TableField(value = "resource_name")
    @ApiModelProperty(value = "农资名称")
    private String resourceName;

    @TableField(value = "resource_type")
    @ApiModelProperty(value = "农资类型(0是物料,1是机械)")
    private String resourceType;

    @TableField(value = "resource_image")
    @ApiModelProperty(value = "农资图片")
    private String resourceImage;

    @TableField(value = "measure_unit")
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

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

