package com.server.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 入库请求对象
 * 用于接收前端入库操作的JSON参数
 * 
 * @author server
 * @date 2025-01-XX
 */
@Data
@ApiModel(description = "入库请求对象")
public class StockInRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "农资ID", required = true)
    private Long resourceId;

    @ApiModelProperty(value = "入库数量", required = true)
    private BigDecimal quantity;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "备注")
    private String remark;
}

