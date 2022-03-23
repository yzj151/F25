package com.learn.consumer.aop;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.learn.api.annotation.SystemLog;
import com.learn.api.model.UmsAdmin;
import com.learn.api.model.UmsLog;
import com.learn.api.service.ITokenService;
import com.learn.api.service.IUserLogService;
import com.learn.api.service.IUserService;
import com.learn.common.IpInfoUtil;
import com.learn.common.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Aspect //切面  日志处理
@Component
@Slf4j
//拦截器 执行方法之前被其拦截
public class SystemLogAspect implements HandlerInterceptor {
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

    @Reference(
            version = "1.0.0",
            interfaceName = "com.learn.api.service.IUserLogService",
            interfaceClass = IUserLogService.class,
            timeout = 120000
    )
    private IUserLogService servicelog;

    private static final ThreadLocal<Date> beginTimeThreadLocal=new NamedThreadLocal<>("ThreadLocal begin time");

    @Pointcut("@annotation(com.learn.api.annotation.SystemLog)") //所有备注SystemLog的方法都视为切面
    //@Pointcut("execution(* com.learn.consumer.controller.*.*(..))")
    public void controllerAspect(){
        System.out.println("ok");
    }

    @Before("controllerAspect()")
    public void before(JoinPoint joinPoint){ //核心业务 连接点
        Date beginTime=new Date();
        beginTimeThreadLocal.set(beginTime);

    }


    @AfterReturning("controllerAspect()")
    public void after(JoinPoint joinPoint){ //在核心业务之后执行
        try{

            ServletRequestAttributes attributes=(ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request=attributes.getRequest();//真正的前端请求对象



            Map<String,String[]> requestParams=request.getParameterMap();

            String token=request.getHeader("Authorization");//token
            if(token==null){
                throw new RuntimeException("无token，请重新登录！");
            }
            token=token.split("@")[1];
            long userid=Long.parseLong(tokenService.getUserid(token));

            //找到用户
            UmsAdmin admin=service.findUserById(userid);

            if(admin==null){
                throw new RuntimeException("用户不存在，请重新登录！");
            }

            //通过密码验证的token才是真正的合法用户

            if(tokenService.checkSign(token,admin.getPassword())){
                //存储log
                //模拟多个用户 访问本系统
                //取修饰在连接点方法的注解的属性
                Map<String,Object> map=getControllerMethodInfo(joinPoint);
                String descripton=map.get("descripton").toString();
                int type=Integer.parseInt(map.get("type").toString());

                UmsLog log=new UmsLog(); //日志生成
                final Random random=new Random();
                int userId=random.nextInt(100);
                log.setUserid(userId);
                log.setName(descripton);//标题
                log.setLogType(type);//日志类型
                log.setRequestUrl(request.getRequestURI());
                log.setRequestType(request.getMethod());
                //必须把，转变为&& 否则我们做flink分析 无法分割
                String str=JSON.toJSONString(requestParams).replace(",","&&");
                log.setRequestParam(str.replace("\"","\'"));

                log.setIp(IpInfoUtil.getIpAddr(request));
                log.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                log.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                long begintime=beginTimeThreadLocal.get().getTime();
                long endtime=System.currentTimeMillis();
                Long cost=endtime-begintime;


                log.setCostTime(cost.intValue());
                //持久化进入到mysql中
                ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(servicelog,log));

            }else{
                throw new RuntimeException("token验证失败了！");
            }


        }catch(Exception e){
            e.printStackTrace();
        }

    }


    public static Map<String,Object> getControllerMethodInfo(JoinPoint joinPoint){
        Map <String,Object>map =new HashMap<>(16);
        //获取目标类
        String targetName=joinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName=joinPoint.getSignature().getName();
        //获取形参
        Object [] arguments=joinPoint.getArgs();
        //用反射 生成类对象
        try {
            Class targetClass=Class.forName(targetName);

            //获取该类中的所有方法
            Method[] methods=targetClass.getMethods();

            for(Method method:methods){
                if(!method.getName().equals(methodName)){
                    continue;
                }
                //防止找到的是重载的其他方法，因此通过形参个数确定是本方法
                if(method.getParameterTypes().length!=arguments.length){
                    continue;
                }
                //真正的找到了该方法

                String descripton=method.getAnnotation(SystemLog.class).description();
                int type=method.getAnnotation(SystemLog.class).type().ordinal();//取得枚举的序号
                map.put("descripton",descripton);
                map.put("type",type);

            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;

    }
}