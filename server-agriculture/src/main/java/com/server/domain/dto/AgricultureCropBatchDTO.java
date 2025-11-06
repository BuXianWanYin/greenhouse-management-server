package com.server.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AgricultureCropBatchDTO {
    private String batchName;
    // 从 agriculture_crop_batch 表获取
    @TableId(value="batch_id",type = IdType.AUTO)
    private String batchId;
    private Long classId;            // 种质ID（关联agriculture_class表）
    private Long pastureId;            // 从 agriculture_crop_batch 表获取
    private LocalDateTime startTime;
    private double cropArea;       // 从 agriculture_crop_batch 表获取
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDate createTime;           // 从 agriculture_crop_batch 表获取
    private String classImage;         // 从 agriculture_class 表获取
    private String className;
    private String nickName;
    private String jobName;            // agriculture_job
    private String harvest;
}
