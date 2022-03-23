package com.learn.api.service;

import com.learn.api.dto.UmsAdminLoginParam;
import com.learn.api.model.UmsAdmin;

/**
 * 登录
 */

public interface IUserService {

    public UmsAdmin login(UmsAdminLoginParam user);
    public UmsAdmin findByUsername(String username);
    public UmsAdmin findByUmsAdmin(String token);
    public UmsAdmin findUserById(Long userId);
    public UmsAdmin reg(UmsAdmin user);

}
