package com.learn.api.service;


import com.learn.api.dto.OmsMoneyInfoParam;
import com.learn.api.dto.OmsOrderDeliveryParam;
import com.learn.api.dto.OmsOrderQueryParam;
import com.learn.api.dto.OmsReceiverInfoParam;
import com.learn.api.model.OmsOrder;
import com.learn.common.CommonPage;

/**
 * 订单详情
 */

public interface IOmsOrderService {
    /**
     * 批量删除订单
     */
    int delete(Long id);

    /**
     * 获取指定订单详情
     */
    OmsOrder detail(Long id);

    /**
     * 修改订单备注
     */

    OmsOrder updateNote(Long id, String note, Integer status);


    /**
     * 修改订单收货人信息
     */

    OmsOrder updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);


    /**
     * 修改订单费用信息
     */

    OmsOrder updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);


    /**
     * 批量关闭订单
     */

    OmsOrder close(Long id, String note);


    public CommonPage list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) ;

    /**
     * 批量发货
     */

    OmsOrder delivery(OmsOrderDeliveryParam deliveryParam);



    /**
     * 发送延迟消息取消订单
     */

    //根据id 取消订单
    public void sendDelayMessageCancelOrder(Long orderId,int minute);


    public OmsOrder cancelOrder(Long orderId) ;
}