package com.seckill.service;

import com.seckill.entity.Order;
import com.seckill.entity.Stock;
import com.seckill.mq.OrderMessageSender;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Long> seckillScript;
    private final OrderMessageSender orderMessageSender;

    public SeckillService(RedisTemplate<String, Object> redisTemplate,
                          RedisScript<Long> seckillScript,
                          OrderMessageSender orderMessageSender) {
        this.redisTemplate = redisTemplate;
        this.seckillScript = seckillScript;
        this.orderMessageSender = orderMessageSender;
    }

    @PostConstruct
    public void init() {
        // pre-load stock for product 1
        String key = "seckill:stock:1";
        redisTemplate.opsForValue().setIfAbsent(key, 100, 1, TimeUnit.DAYS);
    }

    /**
     * Execute seckill via Lua script for atomicity.
     * @return 1=success, 0=sold out, 2=duplicate
     */
    public long seckill(Long userId, Long productId) {
        String stockKey = "seckill:stock:" + productId;
        String userKey  = "seckill:users:" + productId;

        Long result = redisTemplate.execute(seckillScript,
                Collections.singletonList(stockKey), userKey, String.valueOf(userId));

        if (result != null && result == 1) {
            // async order creation
            Order order = new Order();
            order.setUserId(userId);
            order.setProductId(productId);
            order.setOrderNo("SK" + System.currentTimeMillis());
            order.setAmount(new BigDecimal("99.99"));
            order.setStatus(0);
            order.setCreateTime(LocalDateTime.now());
            order.setExpireTime(LocalDateTime.now().plusMinutes(30));
            orderMessageSender.sendOrderMessage(order, 30 * 60 * 1000);
        }
        return result != null ? result : 0;
    }

    public Integer getRemainingStock(Long productId) {
        Object val = redisTemplate.opsForValue().get("seckill:stock:" + productId);
        return val != null ? Integer.parseInt(val.toString()) : 0;
    }
}
