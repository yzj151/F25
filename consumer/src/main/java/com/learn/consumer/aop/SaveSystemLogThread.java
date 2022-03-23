package com.learn.consumer.aop;

import com.learn.api.model.UmsLog;
import com.learn.api.service.IUserLogService;

/**
 * 注册拦截
 */

public class SaveSystemLogThread implements Runnable{

    private IUserLogService service;
    private UmsLog log;

    public SaveSystemLogThread(IUserLogService service,UmsLog log){
        this.log = log;
        this.service = service;
    }

    @Override
    public void run() {
        if(log.getLogType()!=null){
            service.insert(log);
        }

    }
}
