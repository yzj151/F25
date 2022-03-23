package com.learn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.model.OmsCompanyAddress;
import com.learn.api.service.IOmsCompanyAddressService;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单发货地址控制器
 */

@Controller
@Api(tags = "OmsCompanyAddressController", description = "收货地址管理")
@RequestMapping("/companyAddress")
@CrossOrigin
public class OmsCompanyAddressController {
    @Reference(
            version = "1.0.0",interfaceClass = IOmsCompanyAddressService.class,
            interfaceName = "com.learn.api.service.IOmsCompanyAddressService",
            timeout =120000
    )
    private IOmsCompanyAddressService companyAddressService;

    @ApiOperation("获取所有收货地址")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    @UserLoginToken
    public CommonResult<List<OmsCompanyAddress>> list(){
        List<OmsCompanyAddress> companyAddressesList = companyAddressService.list();
        return CommonResult.success(companyAddressesList);
    }

}
