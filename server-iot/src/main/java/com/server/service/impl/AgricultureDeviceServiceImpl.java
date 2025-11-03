package com.server.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceType;
import com.server.domain.vo.AgricultureDeviceVO;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.mapper.AgricultureDeviceTypeMapper;
import com.server.service.AgricultureDeviceService;
import com.server.constant.RabbitMQConstant;
import com.server.utils.SecurityUtils;
import com.server.utils.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 设备信息Service业务层处理
 *
 * @author server
 * @date 2025-05-26
 */
@Service
public class AgricultureDeviceServiceImpl extends ServiceImpl<AgricultureDeviceMapper, AgricultureDevice> implements AgricultureDeviceService {
    @Autowired
    private AgricultureDeviceMapper agricultureDeviceMapper;
    @Autowired
    private AgricultureDeviceTypeMapper agricultureDeviceTypeMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public AgricultureDevice selectAgricultureDeviceById(String id) {
        return getById(id);
    }

    /**
     * 查询设备信息列表
     *
     * @param agricultureDevice 设备信息
     * @return VO设备信息列表
     */
    @Override
    public List<AgricultureDeviceVO> selectAgricultureDeviceListVO(AgricultureDevice agricultureDevice) {
        QueryWrapper<AgricultureDevice> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(agricultureDevice.getDeviceName())) {
            wrapper.like("d.device_name", agricultureDevice.getDeviceName());
        }
        if (agricultureDevice.getPastureId() != null) {
            wrapper.eq("d.pasture_id", agricultureDevice.getPastureId());
        }
        if (agricultureDevice.getDeviceTypeId() != null) {
            wrapper.eq("d.device_type_id", agricultureDevice.getDeviceTypeId());
        }
        return agricultureDeviceMapper.selectAgricultureDeviceVOList(wrapper);
    }

    /**
     * 查询设备信息列表
     *
     * @param agricultureDevice 设备信息
     * @return 设备信息
     */
    @Override
    public List<AgricultureDevice> selectAgricultureDeviceList(AgricultureDevice agricultureDevice) {
        return list();
    }

    /**
     * 新增设备信息
     *
     * @param agricultureDevice 设备信息
     * @return 结果
     */
    @Override
    public Long insertAgricultureDevice(AgricultureDevice agricultureDevice) {

        agricultureDeviceMapper.insert(agricultureDevice);
        return agricultureDevice.getId();
    }

    /**
     * 修改设备信息
     *
     * @param agricultureDevice 设备信息
     * @return 结果
     */
    @Override
    public int updateAgricultureDevice(AgricultureDevice agricultureDevice) {
        int update = agricultureDeviceMapper.updateById(agricultureDevice);
        String username;
        try {
            username = SecurityUtils.getUsername();
            if (org.apache.commons.lang3.StringUtils.isEmpty(username)) {
                username = "system";
            }
        } catch (Exception e) {
            username = "system";
        }
        agricultureDevice.setUpdateBy(username);

        rabbitTemplate.convertAndSend(RabbitMQConstant.FB_EXCHANGE, "*", agricultureDevice);
        return update;
    }

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceByIds(String[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0; // removeByIds 返回 boolean
    }

    /**
     * 删除设备信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceById(String id) {
        return removeById(id) ? 1 : 0; // removeById 返回 boolean
    }


    //根据大棚和id查询设备列表
    @Override
    public List<Long> selectDeviceIdsByPastureAndBatch(Long pastureId, Long batchId) {
        return lambdaQuery()
                .eq(AgricultureDevice::getPastureId, pastureId)
                .list()
                .stream()
                .map(AgricultureDevice::getId)
                .collect(Collectors.toList());
    }
}

