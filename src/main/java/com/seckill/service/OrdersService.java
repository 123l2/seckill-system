package com.seckill.service;

import com.seckill.entity.Order;
import com.seckill.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersService {

    private static final Logger log = LoggerFactory.getLogger(OrdersService.class);

    private final OrderMapper orderMapper;

    public OrdersService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Transactional
    public void saveOrder(Order order) {
        orderMapper.insert(order);
        log.info("Order saved: {}", order.getOrderNo());
    }

    @Transactional
    public void cancelOrder(String orderNo) {
        int affected = orderMapper.cancelExpiredOrder(orderNo);
        if (affected > 0) {
            log.info("Order cancelled: {}", orderNo);
        }
    }
}
