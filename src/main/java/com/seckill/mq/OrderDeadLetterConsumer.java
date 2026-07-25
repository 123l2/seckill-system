package com.seckill.mq;

import com.seckill.config.RabbitMQConfig;
import com.seckill.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderDeadLetterConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderDeadLetterConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.ORDER_DEAD_LETTER_QUEUE)
    public void handleExpiredOrder(Order order) {
        log.info("Order {} expired, auto-cancelling", order.getOrderNo());
        // update order status to cancelled in DB
    }
}
