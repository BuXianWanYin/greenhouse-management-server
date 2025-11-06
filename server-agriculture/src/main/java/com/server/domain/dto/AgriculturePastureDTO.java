package com.server.domain.dto;

import lombok.Data;

/**
 * @author bxwy
 * @date 2025-11-07
 */
@Data
public class AgriculturePastureDTO {
    private Long id; //ID
    private Long pasture_id;  //agriculture_crop_batch表的温室id
    private String area;   //温室面积
    private String remaining_area;   //温室剩余面积
    private Long crop_area;   //agriculture_crop_batch表的种植面积
    private Long fish_area;
    private Long big_breeding_quantity; //最大批次数量
    private Long breeding_quantity;  //当前批次数量
}
