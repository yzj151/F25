package com.learn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.LogType;
import com.learn.api.annotation.SystemLog;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.dto.OmsMoneyInfoParam;
import com.learn.api.dto.OmsOrderDeliveryParam;
import com.learn.api.dto.OmsOrderQueryParam;
import com.learn.api.dto.OmsReceiverInfoParam;
import com.learn.api.model.OmsOrder;
import com.learn.api.service.IOmsOrderReturnApplyService;
import com.learn.api.service.IOmsOrderService;
import com.learn.common.CommonPage;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单详情控制器
 */

@Controller
@Api(tags = "OmsOrderController", description = "订单管理")
@RequestMapping("/order")
@CrossOrigin
public class OmsOrderController {
    @Reference( //调用远程服务 与provider与之对应
            version = "1.0.0.",
            interfaceName = "com.learn.api.service.IOmsOrderService",
            interfaceClass = IOmsOrderService.class
    )
    private IOmsOrderService service;

    @ApiOperation("查询订单")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody //返回对象转为json
    @UserLoginToken //需要token验证
    @SystemLog(description = "查询订单", type = LogType.ORDER_LIST) //系统日志

    public CommonResult<CommonPage<OmsOrder>> list(OmsOrderQueryParam queryParam,
                                                   @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        return CommonResult.success(service.list(queryParam,pageSize,pageNum));
    }

    @ApiOperation("获取订单详情:订单信息、商品信息、操作记录")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "获取订单详情:订单信息、商品信息、操作记录", type = LogType.ORDER_DETAIL)

    public CommonResult<OmsOrder> detail(@PathVariable Long id){
        OmsOrder order=service.detail(id);
        return CommonResult.success(order);
    }

    @ApiOperation("备注订单")
    @RequestMapping(value = "/update/note", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken

    public CommonResult updateNote(@RequestParam("id") Long id,
                                    @RequestParam("note") String note,
                                   @RequestParam("status") Integer status){
        CommonResult commonResult;
        try{
            service.updateNote(id,note,status);
            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;

    }

    @ApiOperation("批量删除订单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "批量删除订单", type = LogType.ORDER_DELETE)
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        CommonResult commonResult;
        try{
            ids.forEach(c-> service.delete(c));

            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }


    @ApiOperation("修改收货人信息")
    @RequestMapping(value = "/update/receiverInfo", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    public CommonResult updateReceiverInfo(@RequestBody OmsReceiverInfoParam receiverInfoParam){
        CommonResult commonResult;
        try{
            service.updateReceiverInfo(receiverInfoParam);

            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }


    @ApiOperation("修改订单费用信息")
    @RequestMapping(value = "/update/moneyInfo", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    public CommonResult updateReceiverInfo(@RequestBody OmsMoneyInfoParam infoParam){
        CommonResult commonResult;
        try{
            service.updateMoneyInfo(infoParam);

            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }

    @ApiOperation("批量关闭订单")
    @RequestMapping(value = "/update/close", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "批量关闭订单", type = LogType.ORDER_CLOE_UPDATE)

    public CommonResult close(@RequestParam("ids") List<Long> ids,@RequestParam String note){
        CommonResult commonResult;
        try{
            ids.stream().forEach(id->service.close(id,note));


            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }


    @ApiOperation("批量发货")
    @RequestMapping(value = "/update/delivery", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    public CommonResult delivery(@RequestBody List<OmsOrderDeliveryParam> deliveryParamList){
        CommonResult commonResult;
        try{
            deliveryParamList.stream().forEach(param->service.delivery(param));


            commonResult=CommonResult.success(1);
        }catch(Exception e){
            commonResult=CommonResult.failed();
            e.printStackTrace();
        }
        return commonResult;
    }

    @ApiOperation("取消单个超时订单")
    @RequestMapping(value = "/cancelOrder",method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "取消单个超时订单", type = LogType.ORDER_CANCEL)

    public CommonResult cancelOrder(@RequestParam("ids") List<Long> ids,@RequestParam("minute") int minute){
        service.sendDelayMessageCancelOrder(ids.get(0),minute);
        return CommonResult.success(null);
    }


}
