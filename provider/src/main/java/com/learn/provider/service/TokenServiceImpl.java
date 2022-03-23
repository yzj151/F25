package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.learn.api.service.ITokenService;
import com.learn.api.service.IUserService;

import java.util.Date;



/**
 * jwt token认证
 * 用户调用登录接口，登陆成功后获取到jwt的token
 * 之后用户每次调用接口都在http的header中添加一个叫Authorization的头，值为jwt的token；
 * 后台对其验证，获取用户信息，实现认证
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.learn.api.service.ITokenService",
        interfaceClass = ITokenService.class
)

public class TokenServiceImpl implements ITokenService {

    private static final long EXPIRE_TIME=5*60*1000000;

    @Override
    public String getToken(String userId, String password) {

        Date date=new Date(System.currentTimeMillis()+EXPIRE_TIME);//5分钟失效
        String token=JWT.create().withAudience(userId).withExpiresAt(date).sign(Algorithm.HMAC256(password)); //生成token 加密处理

        return token; //返回给前端

    }

    @Override
    public String getUserid(String token) {
        String userid=JWT.decode(token).getAudience().get(0);

        return userid;
    }

    @Override
    public boolean checkSign(String token, String password) { //token验证
        if(token==null){
            throw new RuntimeException("无token，请重新登录！");
        }
        try{
            //生成token验证器
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(password)).build();
            jwtVerifier.verify(token);

        }catch(Exception e){
            throw new RuntimeException("token 无效，请重新获取");
        }
        return true;
    }
}