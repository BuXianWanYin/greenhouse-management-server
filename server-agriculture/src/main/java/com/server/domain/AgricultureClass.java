package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author bxwy
 * @description 种质信息表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_class")
@ApiModel(value = "AgricultureClass", description = "种质信息表")
public class AgricultureClass extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "class_id", type = IdType.AUTO)
    @ApiModelProperty(value = "种质ID")
    private Long classId;

    /** 种类（fruit=瓜果,vegetable=蔬菜,other=其他） */
    @TableField(value = "category")
    @ApiModelProperty(value = "种类（fruit=瓜果,vegetable=蔬菜,other=其他）")
    private String category;

    @TableField(value = "class_name")
    @ApiModelProperty(value = "种质名称")
    private String className;

    @TableField(value = "class_image")
    @ApiModelProperty(value = "种质图片")
    private String classImage;

    @TableField(value = "class_des")
    @ApiModelProperty(value = "宣传语")
    private String classDes;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

}
