package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.learn.api.dto.UmsAdminLoginParam;
import com.learn.api.model.UmsAdmin;
import com.learn.api.service.IUserService;
import com.learn.provider.mapper.UmsAdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户页面服务 -- 登录、注册
 */

@Service( //服务提供者实现相关的服务接口，当消费端调用相关的类时，最终会调用提供者的实现方法。
        version = "1.0.0",
        interfaceName = "com.learn.api.service.IUserService",
        interfaceClass = IUserService.class
)
@Transactional//利用spring管理事物
public class UserServiceImpl implements IUserService {
    @Autowired
    private UmsAdminMapper dao;

    @Autowired
    private TokenServiceImpl tokenService;


    @Override
    public UmsAdmin login(UmsAdminLoginParam user) {
        return null;
    }

    @Override
    public UmsAdmin findByUsername(String username) {

        return dao.findByUsername(username);
    }

    @Override
    public UmsAdmin findByUmsAdmin(String token) {
        String userId = tokenService.getUserid(token);
        UmsAdmin admin = this.findUserById(Long.parseLong(userId));
        return admin;
    }

    @Override
    public UmsAdmin findUserById(Long userId) {
        return dao.selectByPrimaryKey(userId);
    }

    @Override
    public UmsAdmin reg(UmsAdmin user) {
        String icon = "http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20190129/1e34aef2a409119018a4c6258e39ecfb_222_222.png";
        user.setIcon(icon);
        dao.insert(user);
        return user;
    }
}
