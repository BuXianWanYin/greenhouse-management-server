package com.server.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "分页批次食品查询")
@Data
public class AgriculturePartitionFoodPageDTO {
    @ApiModelProperty(value = "批次id", example = "2341", required = true)
    private String partitionId;
}
