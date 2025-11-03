package com.server.domain.vo;

import com.server.domain.AgricultureBatchTask;
import com.server.domain.AgricultureDeviceSensorAlert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("批次任务详情对象")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchTaskDetailVO {

    @ApiModelProperty("批次任务信息")
    private AgricultureBatchTask batchTask;

    @ApiModelProperty("该批次任务区间内的传感器预警信息")
    private List<AgricultureDeviceSensorAlert> sensorAlertList;

    @ApiModelProperty("该批次任务区间内的预警条数")
    private Integer alertCount;
}