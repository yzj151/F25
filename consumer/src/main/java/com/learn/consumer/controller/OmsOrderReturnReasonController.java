package com.learn.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.LogType;
import com.learn.api.annotation.SystemLog;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.model.OmsOrderReturnReason;
import com.learn.api.service.IOmsOrderReturnReasonService;
import com.learn.common.CommonPage;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 */
@RestController
@Api(tags ="OmsOrderReturnReasonController",description = "退货原因管理")
@CrossOrigin
@RequestMapping("/returnReason")
public class OmsOrderReturnReasonController {

    @Reference(
            version = "1.0.0.",
            interfaceName = "com.learn.api.service.IOmsOrderReturnReasonService",
            interfaceClass = IOmsOrderReturnReasonService.class
    )
    private IOmsOrderReturnReasonService service;


    @ApiOperation("批量删除退货原因")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "批量删除退货原因", type = LogType.ORDER_DELETE_RETURN_REASON)
    public CommonResult delete(@RequestParam("ids") List<Long> ids){

        int count=service.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("修改退货原因")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "修改退货原因", type = LogType.ORDER_UPDATE_RETURN_REASON)
    public CommonResult update(@PathVariable Long id,@RequestBody OmsOrderReturnReason returnReason){
        CommonResult commonResult;
        try{
            service.update(id,returnReason);
            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }


    @ApiOperation("修改退货原因启用状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "修改退货原因启用状态", type = LogType.ORDER_UPDATE_RETURN_REASON_STATUS)
    public CommonResult updateStatus(@RequestParam(value = "status") Integer status,
                                     @RequestParam("ids") Long id){


        CommonResult commonResult;
        try{
            service.updateStatus(id,status);
            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;

    }


    @ApiOperation("添加退货原因")
    @ResponseBody
    @PostMapping(value = "/create")
    @SystemLog(description = "添加退货原因",type= LogType.ORDER_ADD_RETURN_REASON)
    @UserLoginToken
    public CommonResult create(@RequestBody OmsOrderReturnReason returnReason){
        CommonResult commonResult;

        try{
            service.create(returnReason);
            commonResult=CommonResult.success(1);

        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }


    @ApiOperation("分页查询全部退货原因")
    @ResponseBody
    @GetMapping(value = "/list")
    @SystemLog(description = "分页查询全部退货原因",type= LogType.ORDER_RETURN_REASON_LIST)
    @UserLoginToken
    public CommonResult<CommonPage<OmsOrderReturnReason>>
    list(@RequestParam(value="pageSize",defaultValue = "5") Integer pageSize,
         @RequestParam(value="pageNum",defaultValue = "1") Integer pageNum){


        CommonPage commonPage=service.list(pageSize,pageNum);
        return CommonResult.success(commonPage);
    }


    @ApiOperation("获取单个退还原因详情信息")
    @ResponseBody
    @GetMapping(value = "/{id}")
    @SystemLog(description = "获取单个退还原因详情信息",type= LogType.ORDER_RETURN_REASON)
    @UserLoginToken
    public CommonResult<OmsOrderReturnReason> getItem(@PathVariable Long id){
        return CommonResult.success(service.getOmsOrderReturnReason(id));
    }

}