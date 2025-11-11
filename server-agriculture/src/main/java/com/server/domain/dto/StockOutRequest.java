package com.server.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 出库请求对象
 * 用于接收前端出库操作的JSON参数
 * 
 * @author server
 * @date 2025-01-XX
 */
@Data
@ApiModel(description = "出库请求对象")
public class StockOutRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "农资ID", required = true)
    private Long resourceId;

    @ApiModelProperty(value = "出库数量", required = true)
    private BigDecimal quantity;

    @ApiModelProperty(value = "备注")
    private String remark;
}

