package com.server.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
import com.server.domain.vo.BatchTaskDetailVO;
import com.server.mapper.*;
import com.server.service.*;
import com.server.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.server.service.AgriculturePartitionFoodService;
import com.server.service.AgricultureDeviceSensorAlertService;

/**
 * 采摘食品Service业务层处理
 * 
 * @author bxwy
 * @date 2025-06-24
 */
@Service
public class AgriculturePartitionFoodServiceImpl extends ServiceImpl<AgriculturePartitionFoodMapper, AgriculturePartitionFood> implements AgriculturePartitionFoodService
{
    // 注入采摘食品的Mapper，用于操作采摘食品表
    @Autowired
    private AgriculturePartitionFoodMapper agriculturePartitionFoodMapper;

    // 注入批次Mapper，用于操作批次表
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    // 注入温室Mapper，用于操作温室表
    @Autowired
    private AgriculturePastureMapper pastureMapper;

    // 注入批次任务Mapper，用于操作批次任务表
    @Autowired
    private AgricultureBatchTaskMapper batchTaskMapper;

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;
    @Autowired
    private AgricultureThresholdConfigService agricultureThresholdConfigService;
    @Autowired
    private AgricultureDeviceSensorAlertService sensorAlertService;

    @Value("${codepath.path}")
    private String codepath;


    /**
     * 查询采摘食品
     * 
     * @param id 采摘食品主键
     * @return 采摘食品
     */
    @Override
    public AgriculturePartitionFood selectagriculturePartitionFoodById(String id)
    {
        return agriculturePartitionFoodMapper.selectById(id);
    }

    /**
     * 查询采摘食品列表
     * 
     * @param agriculturePartitionFood 采摘食品
     * @return 采摘食品列表
     */
    @Override
    public List<AgriculturePartitionFood> selectagriculturePartitionFoodList(AgriculturePartitionFood agriculturePartitionFood)
    {
        LambdaQueryWrapper<AgriculturePartitionFood> lambdaQueryWrapper = new QueryWrapper<AgriculturePartitionFood>().lambda();
        return agriculturePartitionFoodMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增采摘食品
     * 
     * @param agriculturePartitionFood 采摘食品
     * @return 结果
     */
    @Override
    public int insertagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood)
    {
        return agriculturePartitionFoodMapper.insert(agriculturePartitionFood);
    }

    /**
     * 修改采摘食品
     * 
     * @param agriculturePartitionFood 采摘食品
     * @return 结果
     */
    @Override
    public int updateagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood)
    {
        return agriculturePartitionFoodMapper.updateById(agriculturePartitionFood);
    }

    /**
     * 批量删除采摘食品
     * 
     * @param ids 需要删除的采摘食品主键
     * @return 结果
     */
    @Override
    public int deleteagriculturePartitionFoodByIds(String[] ids)
    {
        return agriculturePartitionFoodMapper.deleteById(ids);
    }

    /**
     * 删除采摘食品信息
     * 
     * @param id 采摘食品主键
     * @return 结果
     */
    @Override
    public int deleteagriculturePartitionFoodById(String id)
    {
        return agriculturePartitionFoodMapper.deleteById(id);
    }

    @Override
    public List<AgriculturePartitionFood> fy(AgriculturePartitionFoodPageDTO baseDTO) {
        QueryWrapper wrapper = new QueryWrapper<Map<String, Object>>();
        wrapper.eq("ia_partition_id",baseDTO.getPartitionId());
        List<AgriculturePartitionFood> agriculturePartitionFoods = agriculturePartitionFoodMapper.selectList(wrapper);
        //生成前端访问页面的条形二维码
        agriculturePartitionFoods.forEach(bean -> {
            try {
                // 生成二维码内容为溯源码跳转地址
                String codeUrl = codepath + bean.getId();
                String barcodeBase64 = QRCodeUtil.generateQRCode(codeUrl);
                // 对应字段设置Base64字符串
                bean.setBarcode(barcodeBase64);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return agriculturePartitionFoods;
    }

    /**
     * 新增采摘 生成二维码展示
     */
//    @Override
//    public List<AgriculturePartitionFood> XzList(AgriculturePartitionFood agriculturePartitionFood) {
//        LambdaQueryWrapper<AgriculturePartitionFood> lambdaQueryWrapper = new QueryWrapper<AgriculturePartitionFood>().lambda();
//        lambdaQueryWrapper.eq(agriculturePartitionFood.getIaPartitionId() != null,
//                AgriculturePartitionFood::getIaPartitionId,
//                agriculturePartitionFood.getIaPartitionId());
//        List<AgriculturePartitionFood> agriculturePartitionFoods = agriculturePartitionFoodMapper.selectList(lambdaQueryWrapper);
//        //生成前端访问页面的条形二维码
//        agriculturePartitionFoods.forEach(bean -> {
//            try {
//                // 直接生成二维码的Base64字符串
//            String barcodeBase64 = QRCodeUtil.generateQRCode(bean.getId().toString());
//                // 对应字段设置Base64字符串
//                bean.setBarcode(barcodeBase64);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return agriculturePartitionFoods;
//    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
}
