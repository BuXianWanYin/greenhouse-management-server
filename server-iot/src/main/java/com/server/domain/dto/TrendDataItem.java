package com.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 趋势数据项
 * 用于数据库查询结果映射
 * 
 * @author system
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrendDataItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 时间点（格式化后的字符串，用于图表显示）
     */
    private String timePoint;

    /**
     * 时间（原始时间，用于排序）
     * 注意：可能为LocalDateTime（按小时）或LocalDate（按天）
     */
    private Object time;

    /**
     * 平均值
     */
    private Double avgValue;
}

