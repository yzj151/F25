package com.learn.consumer.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.model.UmsAdmin;
import com.learn.api.service.ITokenService;
import com.learn.api.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截器 token验证 (超时重新登录)
 */

@Component
//@component （把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）
//泛指各种组件，就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。
public class AuthenticationInterceptor implements HandlerInterceptor {


    @Reference(
            version = "1.0.0",
            interfaceName = "com.learn.api.service.ITokenService",
            interfaceClass = ITokenService.class,
            timeout = 120000
    )
    private ITokenService tokenService;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.learn.api.service.IUserService",
            interfaceClass = IUserService.class,
            timeout = 120000
    )
    private IUserService service;

    @Override//执行之前执行 在执行核心业务之前进行验证 token验证
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从http头部取出token
        String token=request.getHeader("Authorization");//token
        if(!(handler instanceof HandlerMethod)){//如果不是映射到方法级别的 就通过
            return true;
        }

        HandlerMethod handlerMethod=(HandlerMethod) handler;//是映射到方法级别的，取出该方法
        Method method=handlerMethod.getMethod();
        if(method.isAnnotationPresent(UserLoginToken.class)){//如果该方法被UserLoginToken修饰了 则必须如下
            UserLoginToken userLoginToken=method.getAnnotation(UserLoginToken.class);
            if(userLoginToken.required()==true){//登录需要验证
                if(token==null){
                    throw new RuntimeException("无token，请重新登录！");
                }
                //获取token中的user id
                token=token.split("@")[1];
                long userid=Long.parseLong(tokenService.getUserid(token));

                //找到用户
                UmsAdmin admin=service.findUserById(userid);
                if(admin==null){
                    throw new RuntimeException("用户不存在，请重新登录！");
                }
                //通过密码验证的token才是真正的合法用户

                if(tokenService.checkSign(token,admin.getPassword())){
                    return true;
                }else{
                    throw new RuntimeException("token验证失败了！");
                }
            }
        }

        return true;
    }

    @Override//执行阶段执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("After");
    }
}
