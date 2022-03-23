package com.learn.api.annotation;


import java.lang.annotation.*;

/**
 * 系统日志
 */

@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)//运行时启动
@Documented //
public @interface SystemLog {
    //日志名称
    String description() default  "";
    //日志类型
    LogType type();
}
