package com.learn.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.AipFace;
import com.learn.api.annotation.LogType;
import com.learn.api.annotation.SystemLog;
import com.learn.api.annotation.UserLoginToken;
import com.learn.api.dto.AIBaiduFaceBean;
import com.learn.api.dto.AIFaceBean;
import com.learn.api.dto.UmsAdminLoginParam;
import com.learn.api.model.UmsAdmin;
import com.learn.api.service.ITokenService;
import com.learn.api.service.IUserService;
import com.learn.common.AIFactoryUtil;
import com.learn.common.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理控制器 -- 密码 人脸识别
 */

@RestController
@RequestMapping("/admin")
@CrossOrigin//解决跨域问题
@Api(tags = "UmsUserController",description = "后台用户管理")

//用户登入登出操作
public class UmsUserController {

    @Value("${jwt.tokenHead}") //从配置文档中取配置
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;


    private AipFace aipFace= AIFactoryUtil.getAipFace();
    @Reference(//用在消费端，表明使用的是服务端的服务
            version = "1.0.0",
            interfaceName = "com.learn.api.service.IUserService",
            interfaceClass = IUserService.class,
            timeout = 120000
    )
    private IUserService service;


    @Reference(
            version = "1.0.0",
            interfaceName = "com.learn.api.service.ITokenService",
            interfaceClass = ITokenService.class,
            timeout = 120000
    )
    private ITokenService tokenService;


    @ApiOperation(value = "密码登录后返回token")
    @PostMapping("/login")
    @ResponseBody //无需token 登陆之后才能生成token
    public CommonResult login(@RequestBody UmsAdminLoginParam user) {

        UmsAdmin user1 = service.findByUsername(user.getUsername());
        if (user1 == null) {//没有注册
            return CommonResult.validateFailed("用户不存在！");
        } else {
            if (!user1.getPassword().equals(user.getPassword())) {
                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("error_code", "500");
                return CommonResult.failed(tokenMap);
            } else {//合法用户
                System.out.println("即将登录");
                //做token
                String token = tokenService.getToken(user1.getId().toString(), user1.getPassword());
                Map<String, String> tokenMap = new HashMap<>();
                tokenMap.put("token", token);
                tokenMap.put("error_code", "200");
                tokenMap.put("tokenHead", tokenHead);
                 return CommonResult.success(tokenMap);
            }


        }


    }
    @PostMapping("/flogin")
    @ApiOperation(value = "人脸登录后返回token")//前端送给后端的数据
    @ResponseBody
    public CommonResult faceLogin(@RequestBody AIFaceBean faceBean){
        String groupList="login";
        Map<String,String> tokenMap=new HashMap();
        JSONObject resultObject=aipFace.search(faceBean.getImgdata(),"BASE64",groupList,null);

        AIBaiduFaceBean aiBaiduFaceBean=JSON.parseObject(resultObject.toString(), AIBaiduFaceBean.class);

        if(aiBaiduFaceBean.getError_code().equals("0") &&aiBaiduFaceBean.getError_msg().equals("SUCCESS") ){//脸部识别正确
            //设置大于80分，表示是同一个人

            if(aiBaiduFaceBean.getResult().getUser_list().get(0).getScore()>80){
                faceBean.setError_code(aiBaiduFaceBean.getError_code());
                faceBean.setError_msg(aiBaiduFaceBean.getError_msg());
                String userid=aiBaiduFaceBean.getResult().getUser_list().get(0).getUser_id();//图片名称

                UmsAdmin admin=service.findUserById(Long.parseLong(userid));
                String password=admin.getPassword();
                String username=admin.getUsername();
                String token=tokenService.getToken(userid,password);

                tokenMap.put("error_code",aiBaiduFaceBean.getError_code());
                tokenMap.put("token",token);
                tokenMap.put("tokenHead",tokenHead);
                tokenMap.put("username",username);
                tokenMap.put("password",password);
                return CommonResult.success(tokenMap);
            }else{
                tokenMap.put("error_code",aiBaiduFaceBean.getError_code());
                CommonResult.failed(tokenMap);
            }
        }else{
            tokenMap.put("error_code",aiBaiduFaceBean.getError_code());
            CommonResult.failed(tokenMap);
        }
        return CommonResult.success(aiBaiduFaceBean);
    }


    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value="/info")
    @ResponseBody
    @SystemLog(description = "获取当前登录用户信息",type = LogType.USER_INFO)
    @UserLoginToken(required = true) //已进入需要进行跟踪，是否执行核心业务，token验证
    public CommonResult getAdminInfo(HttpServletRequest request){
        //取得前端发送来的token码
        String token=request.getHeader(tokenHeader);
        //Bearer@XXXX
        String tokenM=token.split("@")[1];

        UmsAdmin admin=service.findByUmsAdmin(tokenM);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("username",admin.getUsername());
        tokenMap.put("roles",new String[]{"TEST"});
        tokenMap.put("icon",admin.getIcon());
        return CommonResult.success(tokenMap);
    }


    @ApiOperation(value = "登出功能")
    @PostMapping(value="/logout")
    @ResponseBody
    @UserLoginToken(required = true)
    @SystemLog(description = "退出",type = LogType.USER_LOGIN_OUT)
    public CommonResult logout(){
        return CommonResult.success(null);
    }


    @ApiOperation(value = "用户注册")
    @PostMapping(value="/reg")
    @ResponseBody
    public CommonResult reg(@RequestBody UmsAdmin umsAdmin){
        if(service.findByUsername(umsAdmin.getUsername())!=null){//已经被注册了
            Map<String,String> tokenMap=new HashMap();
            tokenMap.put("error_code","500");
            return CommonResult.failed(tokenMap);
        }
        //没有被注册
        UmsAdmin a=new UmsAdmin();
        a=service.reg(umsAdmin);
        //操作百度，人脸识别

        HashMap<String,String> options =new HashMap<>();
        options.put("user_info",umsAdmin.getPassword());
        String userid=a.getId().toString();
        String b64=umsAdmin.getPic().substring(22)  ;  //人脸识别的图像的byte码 22位之后是图像数据
        String group="login";
        aipFace.addUser(b64,"BASE64",group,userid,options);


        Map<String,String> tokenMap=new HashMap();
        tokenMap.put("error_code","200");
        return CommonResult.success(tokenMap);

    }

}