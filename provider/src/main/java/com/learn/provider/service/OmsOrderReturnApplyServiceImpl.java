package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.learn.api.dto.OmsReturnApplyQueryParam;
import com.learn.api.dto.OmsUpdateStatusParam;
import com.learn.api.model.OmsOrderReturnApply;
import com.learn.api.model.OmsOrderReturnApplyExample;
import com.learn.api.service.IOmsOrderReturnApplyService;
import com.learn.common.CommonPage;
import com.learn.provider.mapper.OmsOrderReturnApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单申请处理服务
 */

@Service(
        version = "1.0.0.",
        interfaceName = "com.learn.api.service.IOmsOrderReturnApplyService",
        interfaceClass = IOmsOrderReturnApplyService.class
)
@Transactional
public class OmsOrderReturnApplyServiceImpl implements IOmsOrderReturnApplyService {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private OmsOrderReturnApplyMapper omsOrderReturnApplyMapper;

    //springEL
    @Cacheable(cacheNames = {"OrderReturnApplyList"},unless = "#result==null",
    key = "T(String).valueOf(#pageNum+'-'+#pageSize)"+
            ".concat(#queryParam.id!=null?#queryParam.id:'id')"+
            ".concat(#queryParam.receiverKeyword!=null?#queryParam.receiverKeyword:'rk')"+
            ".concat(#queryParam.status!=null?#queryParam.status:'s')"+
            ".concat(#queryParam.createTime!=null?#queryParam.createTime:'ct')"+
            ".concat(#queryParam.handleMan!=null?#queryParam.handleMan:'hand')"+
            ".concat(#queryParam.handleTime!=null?#queryParam.handleTime:'ht')"

    )
    @Override
    public CommonPage list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<OmsOrderReturnApply> list = omsOrderReturnApplyMapper.getOrderReturnApplyList(queryParam);
        return CommonPage.restPage(list);
    }

    @Cacheable(cacheNames = {"OrderReturnApply"},unless = "#result==null",key = "#id")
            @Override
    public OmsOrderReturnApply getItem(Long id) {
        return omsOrderReturnApplyMapper.selectByPrimaryKey(id);
    }

    public void clearOrderReturnReason(){
        cacheManager.getCache("OrderReturnApplyList").clear();
    }

    @CacheEvict(cacheNames = {"OrderReturnApply"},key = "#id",allEntries = true,beforeInvocation = false)
    @Override
    public int delete(List<Long> ids) {
        clearOrderReturnReason();
        OmsOrderReturnApplyExample example = new OmsOrderReturnApplyExample();
        example.createCriteria().andIdIn(ids).andStatusEqualTo(3);//只删除已拒绝的
        return omsOrderReturnApplyMapper.deleteByExample(example);
    }

    @CachePut(cacheNames = {"OrderReturnApply"},unless = "#result==null",key = "#id")
    @Override
    public OmsOrderReturnApply updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = this.getItem(id);

        if(status.equals(1)){//确认退货
            returnApply.setId(id);
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else if(status.equals(2)){//完成退货
            returnApply.setId(id);
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else if(status.equals(3)){
            returnApply.setId(id);
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else{
            return returnApply;
        }

        omsOrderReturnApplyMapper.updateByPrimaryKeySelective(returnApply);
        return returnApply;
    }


}
