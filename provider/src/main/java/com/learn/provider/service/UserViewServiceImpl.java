package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.learn.api.model.UmsLogType;
import com.learn.api.model.UmsLogTypeExample;
import com.learn.api.model.UmsUserView;
import com.learn.api.service.IUserViewService;
import com.learn.common.CommonPage;
import com.learn.provider.mapper.UmsLogTypeMapper;
import com.learn.provider.mapper.redis.mapper.RedisUtilMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * UV服务 -- 柱状图
 */

@Service(
        version = "1.0.0",
        interfaceName = "com.learn.api.service.IUserViewService",
        interfaceClass = IUserViewService.class
)

@Transactional
public class UserViewServiceImpl implements IUserViewService {
    @Autowired
    private UmsLogTypeMapper typeMapper;

    @Autowired
    private RedisUtilMapper mapper;
    @Override
    public CommonPage listUV(String start, String end, String type) {
        List uvList=forDate(start,end,type);

        return CommonPage.restPage(uvList);
    }
    private List<UmsUserView> forDate(String start,String end,String type){//知道开始和结束日期，循环每一天从redis中得到数据
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<UmsUserView> uvList =new ArrayList();

        try{
            Date d1=sdf.parse(start);
            Date d2=sdf.parse(end);
            Date tmp=d1;
            Calendar dd=Calendar.getInstance();

            dd.setTime(d1);

            while(tmp.getTime()<=d2.getTime()){
                tmp=dd.getTime();//当天日期值
                //从redis中取得当天的数据
                Set<Object> s=mapper.getAllKeys(sdf.format(tmp)+"_"+type);

                s.stream().forEach(c->{
                    int count=Integer.parseInt(mapper.get(c.toString()).toString());//每种日志类型的个数
                    //1989-01-01
                    uvList.add(new UmsUserView(c.toString().substring(0,10),count));//取出查询日期 并把次数进去

                });


                dd.add(Calendar.DAY_OF_MONTH,1);//+1天

            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return uvList;


    }
    @Override
    public CommonPage listTypeUV() throws Exception {
        UmsLogTypeExample example=new UmsLogTypeExample();
        example.createCriteria().andFlagEqualTo(1);

        List<UmsLogType> list=typeMapper.selectByExample(example);

        return CommonPage.restPage(list);
    }
}