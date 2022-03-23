package com.learn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.LogType;
import com.learn.api.annotation.SystemLog;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.model.UmsUserView;
import com.learn.api.service.IUserViewService;
import com.learn.common.CommonPage;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * UV统计数据管理控制器
 */

@RestController
@CrossOrigin
@Api(tags = "UVController", description = "UserView统计数据管理")
@RequestMapping("/uv")
public class UmsUVController {

    @Reference(
            version = "1.0.0",
            interfaceName = "com.learn.api.service.IUserViewService",
            interfaceClass = IUserViewService.class,
            timeout = 120000
    )
    private IUserViewService service;

    @ApiOperation(value = "UserView统计数据管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @SystemLog(description = "UserView统计数据管理", type = LogType.UMS_USER_VIEW_LIST)
    @ResponseBody
    @UserLoginToken
    public CommonResult<CommonPage<UmsUserView>> getUVList(
            @RequestParam(value = "start",required = true) String start,
            @RequestParam (value="end",required = true) String end,
            @RequestParam (value="type",required = true) String type
    ){
        CommonPage c = service.listUV(start,end,type);
        return CommonResult.success(c);
    }

    @ApiOperation(value = "UserView统计类型管理")
    @RequestMapping(value = "/type", method = RequestMethod.GET)
    //@SystemLog(description = "UserView统计类型管理", type = LogType.UMS_USER_VIEW_TYPE_LIST)
    @ResponseBody
    @UserLoginToken
    public CommonResult<CommonPage<UmsUserView>> getUVType(){
        CommonPage c = null;
        try {
            c = service.listTypeUV();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(c);
    }


}
