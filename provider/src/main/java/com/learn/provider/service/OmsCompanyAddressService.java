package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.learn.api.model.OmsCompanyAddress;
import com.learn.api.model.OmsCompanyAddressExample;
import com.learn.api.service.IOmsCompanyAddressService;
import com.learn.provider.mapper.OmsCompanyAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单发货地址设置服务
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.learn.api.service.IOmsCompanyAddressService",
        interfaceClass = IOmsCompanyAddressService.class
)
@Transactional
public class OmsCompanyAddressService implements IOmsCompanyAddressService {

    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;

    @Cacheable(cacheNames = {"AddressList"},unless = "#result==null")
    public List<OmsCompanyAddress> list(){
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
