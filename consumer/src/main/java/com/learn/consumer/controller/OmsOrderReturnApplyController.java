package com.learn.consumer.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.LogType;
import com.learn.api.annotation.SystemLog;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.dto.OmsReturnApplyQueryParam;
import com.learn.api.dto.OmsUpdateStatusParam;
import com.learn.api.model.OmsOrderReturnApply;
import com.learn.api.service.IOmsOrderReturnApplyService;
import com.learn.common.CommonPage;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单脱货原因控制器
 */

@RestController
@Api(tags ="OmsOrderReturnReasonController",description = "订单退货申请管理")
@CrossOrigin
@RequestMapping("/returnApply")
public class OmsOrderReturnApplyController {

    @Reference(
            version = "1.0.0.",
            interfaceName = "com.learn.api.service.IOmsOrderReturnApplyService",
            interfaceClass = IOmsOrderReturnApplyService.class
    )
    private IOmsOrderReturnApplyService service;

    @ApiOperation("分页查询退货申请")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    public CommonResult<CommonPage<OmsOrderReturnApply>> list(OmsReturnApplyQueryParam queryParam,
                                                              @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                                              @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        return CommonResult.success(service.list(queryParam,pageSize,pageNum));
    }

    @ApiOperation("获取退货申请详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    //用于获取URL中路径的参数值,参数名由RequestMapping注解请求路径时指定,常用于restful风格的api中,传递参数格式:直接在url后添加需要传递的值即可
    public CommonResult getItem(@PathVariable Long id){
        OmsOrderReturnApply result = service.getItem(id);

        return CommonResult.success(result);
    }

    @ApiOperation("批量删除申请")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "删除退货申请", type = LogType.ORDER_DELETE_RETURN)
    public CommonResult delete(@RequestParam("ids")List<Long> ids){
        int count = service.delete(ids);
        if(count>0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("修改申请状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "修改申请状态", type = LogType.ORDER_UPDATE_RETURN_STATUS)
    public CommonResult updateStatus(@PathVariable Long id, @RequestBody OmsUpdateStatusParam statusParam){
        CommonResult commonResult = null;

        try {
            OmsOrderReturnApply omsOrderReturnApply = service.updateStatus(id,statusParam);
            commonResult = CommonResult.success(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commonResult;
    }
}
