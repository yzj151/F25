package com.learn.api.service;


/**
 * 登录token验证
 */

public interface ITokenService {

    public String getToken(String userId,String password);//生成token

    public String getUserid(String token);

    public boolean checkSign(String token,String password);
}
