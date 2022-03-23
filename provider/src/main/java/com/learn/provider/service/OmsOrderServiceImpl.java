package com.learn.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.learn.api.dto.OmsMoneyInfoParam;
import com.learn.api.dto.OmsOrderDeliveryParam;
import com.learn.api.dto.OmsOrderQueryParam;
import com.learn.api.dto.OmsReceiverInfoParam;
import com.learn.api.model.*;
import com.learn.api.service.IOmsOrderReturnReasonService;
import com.learn.api.service.IOmsOrderService;
import com.learn.common.CommonPage;
import com.learn.provider.mapper.OmsOrderItemMapper;
import com.learn.provider.mapper.OmsOrderMapper;
import com.learn.provider.mapper.OmsOrderOperateHistoryMapper;
import com.learn.provider.mapper.PmsSkuStockMapper;
import com.learn.provider.rabbitmq.CancelOrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *订单详情服务
 */
@Service(
        version = "1.0.0.",
        interfaceName = "com.learn.api.service.IOmsOrderService",
        interfaceClass = IOmsOrderService.class
)
@Transactional
public class OmsOrderServiceImpl implements IOmsOrderService {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private OmsOrderOperateHistoryMapper hdao;

    @Autowired
    private PmsSkuStockMapper pdao;

    @Autowired
    private OmsOrderItemMapper idao;

    @Autowired
    private CancelOrderSender cancelOrderSender;
    @Autowired
    private OmsOrderMapper dao;

    @CacheEvict(cacheNames = {"Order"},key="#id",allEntries = true,beforeInvocation = false)
    @Override
    public int delete(Long id) {
        clearOrder();
        OmsOrder record=new OmsOrder();
        record.setDeleteStatus(1);//设成已删除状态
        OmsOrderExample example=new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdEqualTo(id);//代付款状态的记录可以被删除


        return dao.updateByExampleSelective(record,example);
    }


    public void clearOrder(){
        cacheManager.getCache("OrderList").clear();
    }

    @Cacheable(cacheNames = {"Order"}, unless = "#result == null", key = "#id")
    @Override
    public OmsOrder detail(Long id) {

        return dao.selectByPrimaryKey(id);
    }


    @CachePut(cacheNames = {"Order"},key="#id")
    @Override
    public OmsOrder updateNote(Long id, String note, Integer status) {
        clearOrder();
        OmsOrder order=this.detail(id);
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());

        int count=dao.updateByPrimaryKeySelective(order);

        //增加订单操作历史
        OmsOrderOperateHistory history=new OmsOrderOperateHistory();
        history.setCreateTime(new Date());
        history.setOrderId(id);
        history.setOperateMan("后台管理员");
        history.setOrderStatus(status);

        hdao.insert(history);

        return order;
    }


    @CachePut(cacheNames = {"Order"},key="#result.id")
    @Override
    public OmsOrder updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        clearOrder();
        OmsOrder order=this.detail(receiverInfoParam.getOrderId());
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count=dao.updateByPrimaryKeySelective(order);
        //
        //增加订单操作历史
        OmsOrderOperateHistory history=new OmsOrderOperateHistory();
        history.setCreateTime(new Date());
        history.setOrderId(receiverInfoParam.getOrderId());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(receiverInfoParam.getStatus());

        hdao.insert(history);
        return order;
    }


    @CachePut(cacheNames = {"Order"},key="#result.id")
    @Override
    public OmsOrder updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
        clearOrder();
        OmsOrder order=this.detail(moneyInfoParam.getOrderId());
        order.setId(moneyInfoParam.getOrderId());
        order.setFreightAmount(moneyInfoParam.getFreightAmount());
        order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
        order.setModifyTime(new Date());

        int count=dao.updateByPrimaryKey(order);

        //增加订单操作历史
        OmsOrderOperateHistory history=new OmsOrderOperateHistory();
        history.setCreateTime(new Date());
        history.setOrderId(moneyInfoParam.getOrderId());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(moneyInfoParam.getStatus());
        history.setNote("修改费用信息");
        hdao.insert(history);
        return order;
    }


    @CachePut(cacheNames = {"Order"},key="#result.id")
    @Override
    public OmsOrder close(Long id, String note) {

        clearOrder();
        OmsOrder order=this.detail(id);
        order.setStatus(4);
        OmsOrderExample example=new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdEqualTo(id);
        int count=dao.updateByPrimaryKeySelective(order);


        //增加订单操作历史
        OmsOrderOperateHistory history=new OmsOrderOperateHistory();
        history.setCreateTime(new Date());
        history.setOrderId(id);
        history.setOperateMan("后台管理员");
        history.setOrderStatus(4);
        history.setNote("订单关闭："+note);
        hdao.insert(history);

        return order;
    }



    @Cacheable(cacheNames = {"OrderList"},unless = "#result==null",
            key="T(String).valueOf(#pageNum+'-'+#pageSize)"+
            ".concat(#queryParam.orderSn!=null?#queryParam.orderSn:'orderSn')"+
            ".concat(#queryParam.receiverKeyword!=null?#queryParam.receiverKeyword:'rk')"+
            ".concat(#queryParam.status!=null?#queryParam.status:'s')"+
            ".concat(#queryParam.orderType!=null?#queryParam.orderType:'orderType')"+
            ".concat(#queryParam.sourceType!=null?#queryParam.sourceType:'sourceType')"+
            ".concat(#queryParam.createTime!=null?#queryParam.createTime:'createTime')"

    )

    @Override
    public CommonPage list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<OmsOrder> list=dao.getOrderList(queryParam);
        return CommonPage.restPage(list);
    }


    @CachePut(cacheNames = {"Order"},key="#result.id")
    @Override
    public OmsOrder delivery(OmsOrderDeliveryParam deliveryParam) {

        clearOrder();
        OmsOrder omsOrder=this.detail(deliveryParam.getOrderId());
        omsOrder.setDeliverySn(deliveryParam.getDeliverySn());
        omsOrder.setDeliveryCompany(deliveryParam.getDeliveryCompany());
        omsOrder.setDeliveryTime(new Date());
        omsOrder.setStatus(2);//已发货
        dao.updateByPrimaryKey(omsOrder);
        //增加订单操作历史
        OmsOrderOperateHistory history=new OmsOrderOperateHistory();
        history.setCreateTime(new Date());
        history.setOrderId(deliveryParam.getOrderId());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(2);
        history.setNote("完成发货");
        hdao.insert(history);
        return omsOrder;
    }


    @Override
    public void sendDelayMessageCancelOrder(Long orderId, int minute) {
        long delayTimes=minute*60*1000;
        cancelOrderSender.sendMessage(orderId,delayTimes);
    }


    @CachePut(cacheNames = {"Order"},key="#orderId")
    @Override
    public OmsOrder cancelOrder(Long orderId) {
        clearOrder();
        OmsOrder order=this.detail(orderId);

        if(order!=null){
            //修改订单状态为取消
            order.setStatus(4);
            dao.updateByPrimaryKey(order);
            //库存明细解除锁定
            OmsOrderItemExample example=new OmsOrderItemExample();
            example.createCriteria().andOrderIdEqualTo(orderId);
            List <OmsOrderItem> list=idao.selectByExample(example);

            List<Long> plist=list.stream().map(c->c.getProductId()).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(plist)){
                //库存表
                PmsSkuStock pmsSkuStock=new PmsSkuStock();
                PmsSkuStockExample example1=new PmsSkuStockExample();
                example1.createCriteria().andProductIdIn(plist);
                pmsSkuStock.setLockStock(0);//解除锁定
                pdao.updateByExampleSelective(pmsSkuStock,example1);
            }

        }

        return order;
    }
}
