package com.seckill.mq;

import com.seckill.config.RabbitMQConfig;
import com.seckill.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public OrderMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderMessage(Order order, long delayMs) {
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
