package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.learn.api.model.UmsLog;
import com.learn.api.service.IUserLogService;
import com.learn.provider.mapper.UmsLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 注册服务
 */
@Service(
        version = "1.0.0",
        interfaceName = "com.learn.api.service.IUserLogService",
        interfaceClass = IUserLogService.class
)

@Transactional
public class UserLogServiceImpl implements IUserLogService {
    @Autowired
    private UmsLogMapper dao;


    @Override
    public Integer insert(UmsLog log) {
        return dao.insert(log);
    }
}