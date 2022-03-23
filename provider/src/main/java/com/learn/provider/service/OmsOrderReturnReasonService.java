package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.learn.api.model.OmsOrderReturnReason;
import com.learn.api.model.OmsOrderReturnReasonExample;
import com.learn.api.service.IOmsOrderReturnReasonService;
import com.learn.common.CommonPage;
import com.learn.provider.mapper.OmsOrderReturnReasonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单退货原因服务
 */

@Service(
        version = "1.0.0.",
        interfaceName = "com.learn.api.service.IOmsOrderReturnReasonService",
        interfaceClass = IOmsOrderReturnReasonService.class
)
@Transactional //springboot事务管理
public class OmsOrderReturnReasonService implements IOmsOrderReturnReasonService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private OmsOrderReturnReasonMapper omsOrderReturnReasonMapper;


    @CachePut(cacheNames={"OrderReturnReason"},key="#returnReason.id")
    @Override
    //增加订单退货原因
    public OmsOrderReturnReason create(OmsOrderReturnReason returnReason) {
        clearOrderReturnReason();

        returnReason.setCreateTime(new Date());
        omsOrderReturnReasonMapper.insert(returnReason);

        return returnReason;
    }


    public void clearOrderReturnReason(){
        cacheManager.getCache("OrderReturnReasonList").clear();
    }


    @Cacheable(cacheNames = {"OrderReturnReasonList"},
            unless ="#result==null",key="#pageNum+'-'+#pageSize" )//SpringEl
    @Override
    //页面分页展示
    public CommonPage list(Integer pageSize, Integer pageNum) {
        //分页 Page
        PageHelper.startPage(pageNum,pageSize);
        OmsOrderReturnReasonExample example=new OmsOrderReturnReasonExample();
        example.setOrderByClause("sort desc");

        return CommonPage.restPage(omsOrderReturnReasonMapper.selectByExample(example));
    }

    @CacheEvict(cacheNames = {"OrderReturnReason"},key="#ids[0]",allEntries = true,
            beforeInvocation = false)
    @Override
    public int delete(List<Long> ids) {
        clearOrderReturnReason();
        OmsOrderReturnReasonExample example=new OmsOrderReturnReasonExample();
        example.createCriteria().andIdIn(ids);

        return omsOrderReturnReasonMapper.deleteByExample(example);
    }


    @CachePut(cacheNames = {"OrderReturnReason"},key = "#id")
    @Override
    public OmsOrderReturnReason update(Long id, OmsOrderReturnReason returnReason) {
        clearOrderReturnReason();
        returnReason.setId(id);
        omsOrderReturnReasonMapper.updateByPrimaryKey(returnReason);


        return returnReason;
    }
    @Cacheable(cacheNames = {"OrderReturnReason"},unless ="#result==null",key="#id" )
    @Override
    public OmsOrderReturnReason getOmsOrderReturnReason(Long id) {
        return omsOrderReturnReasonMapper.selectByPrimaryKey(id);
    }


    @CachePut(cacheNames = {"OrderReturnReason"},unless = "#result==null",key = "#id")
    @Override
    public OmsOrderReturnReason updateStatus(Long id, Integer status) {

        clearOrderReturnReason();
        OmsOrderReturnReason record=null;
        if(!status.equals(0)&&!status.equals(1)){
            return record;
        }

        record=getOmsOrderReturnReason(id);
        record.setStatus(status);
        OmsOrderReturnReasonExample example=new OmsOrderReturnReasonExample();
        example.createCriteria().andIdEqualTo(id);
        omsOrderReturnReasonMapper.updateByExampleSelective(record,example);
        return record;
    }
}