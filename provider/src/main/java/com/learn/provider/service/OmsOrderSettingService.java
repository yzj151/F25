package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.learn.api.model.OmsOrderSetting;
import com.learn.api.service.IOmsOrderSettingService;
import com.learn.provider.mapper.OmsOrderSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单设置服务
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.learn.api.service.IOmsOrderSettingService",
        interfaceClass = IOmsOrderSettingService.class
)
@Transactional
public class OmsOrderSettingService implements IOmsOrderSettingService {

    @Autowired
    private OmsOrderSettingMapper dao;

    @Cacheable(cacheNames = {"OrderSetting"},unless = "#result==null",key = "#id")
    @Override
    public OmsOrderSetting getItem(Long id) {
        return dao.selectByPrimaryKey(id);
    }

    @CachePut(cacheNames = {"OrderSetting"},key = "#id")
    @Override
    public OmsOrderSetting update(Long id, OmsOrderSetting omsOrderSetting) {
        omsOrderSetting.setId(id);
        dao.updateByPrimaryKey(omsOrderSetting);
        return omsOrderSetting;
    }
}
