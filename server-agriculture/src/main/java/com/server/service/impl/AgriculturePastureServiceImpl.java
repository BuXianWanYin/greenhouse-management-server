package com.server.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.domain.AgriculturePasture;
import com.server.exception.ServiceException;
import com.server.mapper.AgriculturePastureMapper;
import com.server.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.service.AgriculturePastureService;
import com.server.service.AgricultureCropBatchService;

import static com.server.constant.RabbitMQConstant.*;

/**
 * 温室Service业务层处理
 *
 * @author bxwy
 * @date 2025-11-07
 */
@Service
public class AgriculturePastureServiceImpl extends ServiceImpl<AgriculturePastureMapper, AgriculturePasture> implements AgriculturePastureService {
    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AgricultureCropBatchService agricultureCropBatchService;

    /**
     * 查询温室
     *
     * @param id 温室主键
     * @return 温室
     */
    @Override
    public AgriculturePasture selectAgriculturePastureById(Long id) {
        return getById(id);
    }

    /**
     * 查询温室列表
     *
     * @param agriculturePasture 温室
     * @return 温室
     */
    @Override
    public List<AgriculturePasture> selectAgriculturePastureList(AgriculturePasture agriculturePasture) {

        return list();
    }

    /**
     * 新增温室
     *
     * @param agriculturePasture 温室
     * @return 结果
     */
    @Override
    public int insertAgriculturePasture(AgriculturePasture agriculturePasture) {
        return agriculturePastureMapper.insert(agriculturePasture);
    }

    /**
     * 修改温室
     *
     * @param agriculturePasture 温室
     * @return 结果
     */
    @Override
    public int updateAgriculturePasture(AgriculturePasture agriculturePasture) {
        validate(agriculturePasture);
        int update = agriculturePastureMapper.updateById(agriculturePasture);
        agriculturePasture.setUpdateBy(SecurityUtils.getUsername());
        rabbitTemplate.convertAndSend(FB_EXCHANGE, "*", agriculturePasture);
        return update;
    }

    /**
     * 批量删除温室
     *
     * @param ids 需要删除的温室主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePastureByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除温室信息
     *
     * @param id 温室主键
     * @return 结果
     */
    @Override
    public int deleteAgriculturePastureById(Long id) {
        return removeById(id) ? 1 : 0;
    }


    /**
     * 校验
     *
     * @param agriculturePasture
     */
    private void validate(AgriculturePasture agriculturePasture) {
        // 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
        LambdaQueryWrapper<AgriculturePasture> lambda = new QueryWrapper<AgriculturePasture>().lambda();
        lambda
                .eq(AgriculturePasture::getName, agriculturePasture.getName());
        AgriculturePasture info = agriculturePastureMapper.selectOne(lambda);
        if (!ObjectUtils.isEmpty(info) && info.getId() != agriculturePasture.getId()) {
            throw new ServiceException(AgricultureConstants.Pasture_NAME_EXIST);
        }
    }

}
