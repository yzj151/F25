package com.learn.consumer.config;

import com.learn.consumer.aop.SystemLogAspect;
import com.learn.consumer.interceptor.AuthenticationInterceptor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 */

@Configuration
public class IntecrceptorConfig implements WebMvcConfigurer {
    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private SystemLogAspect systemLogAspect;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有请求，通过判断是否有@LoginRequired注解， 决定是否需要登录
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/**");
        registry.addInterceptor(systemLogAspect).addPathPatterns("/**");
    }
}