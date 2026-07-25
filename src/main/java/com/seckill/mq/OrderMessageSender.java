package com.seckill.mq;

import com.seckill.config.RabbitMQConfig;
import com.seckill.entity.Order;
import com.seckill.service.OrdersService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final OrdersService ordersService;

    public OrderMessageSender(RabbitTemplate rabbitTemplate, OrdersService ordersService) {
        this.rabbitTemplate = rabbitTemplate;
        this.ordersService = ordersService;
    }

    public void sendOrderMessage(Order order, long delayMs) {
        // persist order first
        ordersService.saveOrder(order);

        // send to delay queue
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_DELAY_EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_DELAY,
                order,
                msg -> {
                    msg.getMessageProperties().setDelayLong(delayMs);
                    return msg;
                });
    }
}
