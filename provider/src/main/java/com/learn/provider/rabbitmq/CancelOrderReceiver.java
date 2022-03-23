package com.learn.provider.rabbitmq;


import com.learn.api.service.IOmsOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 取消订单接收者
 */

@Component
@RabbitListener(queues = "mall.order.cancel")
public class CancelOrderReceiver {
    private static Logger LOGGER= LoggerFactory.getLogger(CancelOrderSender.class);

    @Autowired
    private IOmsOrderService omsOrderService;

    @RabbitHandler
    public void handler(Long orderId){
        omsOrderService.cancelOrder(orderId);
    }

}