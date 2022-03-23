package com.learn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.LogType;
import com.learn.api.annotation.SystemLog;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.model.OmsOrderSetting;
import com.learn.api.service.IOmsOrderSettingService;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 订单设置控制器
 */

@RestController
@Api(tags = "OmsOrderSettingController", description = "订单设置管理")
@RequestMapping("/orderSetting")
@CrossOrigin
public class OmsOrderSettingController {
    @Reference( //用在消费端，表明使用的是服务端的服务
            version = "1.0.0",interfaceClass = IOmsOrderSettingService.class,
            interfaceName = "com.learn.api.service.IOmsOrderSettingService",
            timeout =120000
    )
    private IOmsOrderSettingService service;

    @ApiOperation("获取指定订单设置") //构建Api文档的
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody //将java对象转换为json格式的数据
    @UserLoginToken //自定义
    @SystemLog(description = "获取指定订单设置", type = LogType.ORDER_DETAIL_SETTING) //日志写入
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id){
        OmsOrderSetting omsOrderSetting = service.getItem(id);
        return CommonResult.success(omsOrderSetting);
    }

    @ApiOperation("修改指定订单设置")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "修改指定订单设置", type = LogType.ORDER_UPDATE_DETAIL_SETTING)
    public CommonResult update(@PathVariable Long id,@RequestBody OmsOrderSetting omsOrderSetting){
        CommonResult commonResult = null;
        try {
            service.update(id,omsOrderSetting);
            commonResult = CommonResult.success(1);
        }catch (Exception e){
            CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }
}
