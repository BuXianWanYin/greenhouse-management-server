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
import com.server.domain.vo.TraceabilityDetailVO;
import com.server.mapper.*;
import com.server.service.*;
import com.server.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.server.service.AgriculturePartitionFoodService;
import com.server.service.AgricultureTraceabilityLogService;
import com.server.service.AgricultureDeviceSensorAlertService;

/**
 * 分区食品 采摘Service业务层处理
 * 
 * @author server
 * @date 2025-06-24
 */
@Service
public class AgriculturePartitionFoodServiceImpl extends ServiceImpl<AgriculturePartitionFoodMapper, AgriculturePartitionFood> implements AgriculturePartitionFoodService
{
    // 注入分区食品采摘的Mapper，用于操作分区食品采摘表
    @Autowired
    private AgriculturePartitionFoodMapper agriculturePartitionFoodMapper;

    // 注入分区Mapper，用于操作分区表
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    // 注入温室Mapper，用于操作温室表
    @Autowired
    private AgriculturePastureMapper pastureMapper;

    // 注入批次任务Mapper，用于操作批次任务表
    @Autowired
    private AgricultureBatchTaskMapper batchTaskMapper;


    // 注入溯源查询记录服务
    @Autowired
    private AgricultureTraceabilityLogService traceabilityLogService;

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;
    @Autowired
    private AgricultureThresholdConfigService agricultureThresholdConfigService;
    @Autowired
    private AgricultureDeviceSensorAlertService sensorAlertService;

    @Value("${codepath.path}")
    private String codepath;

    /**
     * 根据溯源码查询溯源详情信息，包括分区、温室、批次任务、环境数据等
     *
     * @param traceId 溯源码（溯源id）
     * @param queryIp 查询IP
     * @param userAgent 用户代理
     * @param queryType 查询类型
     * @return TraceabilityDetailVO 溯源详情VO
     * @throws RuntimeException 如果溯源信息不存在
     */
    @Override
    public TraceabilityDetailVO getTraceabilityDetailById(String traceId, String queryIp, String userAgent, String queryType, Date firstTraceTime) {
        // 1. 查溯源表
        AgriculturePartitionFood food = agriculturePartitionFoodMapper.selectById(traceId);
        if (food == null) {
            throw new RuntimeException("溯源信息不存在");
        }

        // 2. 首次溯源时间处理
        if (food.getFirstTraceTime() == null && firstTraceTime != null) {
            food.setFirstTraceTime(firstTraceTime);
            agriculturePartitionFoodMapper.updateById(food);
        }

        // 3. 记录查询日志
        traceabilityLogService.recordTraceabilityQuery(traceId, food.getIaPartitionId(), queryIp, userAgent, queryType,food.getFoodType());

        // 4. 查询该溯源码的溯源次数
        Long traceCount = traceabilityLogService.getTraceabilityCountByCode(traceId);

        // 5. 查分区
        AgricultureCropBatch cropBatch = agricultureCropBatchMapper.selectById(food.getIaPartitionId());

        // 6. 格式化分区的创建时间为年-月-日格式
        String formattedCreateTime = null;
        if (cropBatch != null && cropBatch.getCreateTime() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            formattedCreateTime = dateFormat.format(cropBatch.getCreateTime());
        }

        // 7. 查大棚
        AgriculturePasture pasture = null;
        if (cropBatch != null) {
            pasture = pastureMapper.selectById(cropBatch.getPastureId());
        }

        // 8. 查所有批次任务
        List<AgricultureBatchTask> batchTaskList = batchTaskMapper.selectList(
            new QueryWrapper<AgricultureBatchTask>().eq("batch_id", food.getIaPartitionId())
        );
        List<BatchTaskDetailVO> batchTaskDetailList = new ArrayList<>();
        for (AgricultureBatchTask batchTask : batchTaskList) {
            BatchTaskDetailVO detailVO = new BatchTaskDetailVO();
            detailVO.setBatchTask(batchTask);

            // 只处理有实际开始和结束时间的任务
            if (batchTask.getActualStart() != null && batchTask.getActualFinish() != null) {
                // 气象和水质数据已删除，不再设置相关字段
            }
            batchTaskDetailList.add(detailVO);
        }

        // 9. 查设备及阈值配置（只返回有阈值配置的所有阈值配置信息）
        List<Long> deviceIds = agricultureDeviceService.selectDeviceIdsByPastureAndBatch(
            cropBatch != null ? Long.valueOf(cropBatch.getPastureId()) : null,
            cropBatch != null ? Long.valueOf(cropBatch.getBatchId()) : null
        );
        List<AgricultureThresholdConfig> thresholdConfigList = new ArrayList<>();
        if (deviceIds != null && !deviceIds.isEmpty()) {
            thresholdConfigList = agricultureThresholdConfigService.selectByDeviceIds(deviceIds);
        }

        // 10. 查大棚和分区下的所有预警信息
        List<AgricultureDeviceSensorAlert> allSensorAlerts = sensorAlertService.list(
            new QueryWrapper<AgricultureDeviceSensorAlert>()
                .eq("pasture_id", cropBatch.getPastureId())
                .eq("batch_id", cropBatch.getBatchId())
        );

        // 11. 归属到批次任务区间
        for (BatchTaskDetailVO detailVO : batchTaskDetailList) {
            AgricultureBatchTask batchTask = detailVO.getBatchTask();
            if (batchTask.getActualStart() != null && batchTask.getActualFinish() != null) {
                LocalDateTime start = toLocalDateTime(batchTask.getActualStart());
                LocalDateTime end = toLocalDateTime(batchTask.getActualFinish());
                
                // 如果开始时间和结束时间在同一天，将结束时间调整为当天的23:59:59
                if (start != null && end != null && start.toLocalDate().equals(end.toLocalDate())) {
                    end = end.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                }
                
                // 新建 final 局部变量
                final LocalDateTime realStart, realEnd;
                if (start != null && end != null && start.isAfter(end)) {
                    realStart = end;
                    realEnd = start;
                } else {
                    realStart = start;
                    realEnd = end;
                }
                List<AgricultureDeviceSensorAlert> alertsInTask = allSensorAlerts.stream()
                    .filter(alert -> {
                        LocalDateTime alertTime = alert.getAlertTime();
                        return alertTime != null &&
                            !alertTime.isBefore(realStart) &&
                            !alertTime.isAfter(realEnd);
                    })
                    .collect(Collectors.toList());
                detailVO.setSensorAlertList(alertsInTask);
                detailVO.setAlertCount(alertsInTask.size());
            } else {
                detailVO.setSensorAlertList(Collections.emptyList());
                detailVO.setAlertCount(0);
            }
        }

        // 12. 组装VO
        TraceabilityDetailVO vo = new TraceabilityDetailVO();
        vo.setFoodInfo(food);
        vo.setCropBatch(cropBatch);
        vo.setPastureInfo(pasture);
        vo.setBatchTaskDetailList(batchTaskDetailList);
        vo.setCropBatchCreateTimeFormatted(formattedCreateTime);
        vo.setTraceCount(traceCount);
        vo.setThresholdConfigList(thresholdConfigList);
        vo.setSensorAlertList(allSensorAlerts);
        return vo;
    }

    /**
     * 根据溯源码查询溯源详情信息（重载方法，不记录日志）
     *
     * @param traceId 溯源码（溯源id）
     * @return TraceabilityDetailVO 溯源详情VO
     * @throws RuntimeException 如果溯源信息不存在
     */
    @Override
    public TraceabilityDetailVO getTraceabilityDetailById(String traceId) {
        // 调用带日志记录的方法，传入默认值
        return getTraceabilityDetailById(traceId, null, null, null,null);
    }

    /**
     * 查询分区食品 采摘
     * 
     * @param id 分区食品 采摘主键
     * @return 分区食品 采摘
     */
    @Override
    public AgriculturePartitionFood selectagriculturePartitionFoodById(String id)
    {
        return agriculturePartitionFoodMapper.selectById(id);
    }

    /**
     * 查询分区食品 采摘列表
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 分区食品 采摘
     */
    @Override
    public List<AgriculturePartitionFood> selectagriculturePartitionFoodList(AgriculturePartitionFood agriculturePartitionFood)
    {
        LambdaQueryWrapper<AgriculturePartitionFood> lambdaQueryWrapper = new QueryWrapper<AgriculturePartitionFood>().lambda();
        return agriculturePartitionFoodMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增分区食品 采摘
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 结果
     */
    @Override
    public int insertagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood)
    {
        return agriculturePartitionFoodMapper.insert(agriculturePartitionFood);
    }

    /**
     * 修改分区食品 采摘
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 结果
     */
    @Override
    public int updateagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood)
    {
        return agriculturePartitionFoodMapper.updateById(agriculturePartitionFood);
    }

    /**
     * 批量删除分区食品 采摘
     * 
     * @param ids 需要删除的分区食品 采摘主键
     * @return 结果
     */
    @Override
    public int deleteagriculturePartitionFoodByIds(String[] ids)
    {
        return agriculturePartitionFoodMapper.deleteById(ids);
    }

    /**
     * 删除分区食品 采摘信息
     * 
     * @param id 分区食品 采摘主键
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
