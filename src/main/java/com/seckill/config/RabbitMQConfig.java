package com.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_DELAY_EXCHANGE   = "order.delay.exchange";
    public static final String ORDER_DELAY_QUEUE      = "order.delay.queue";
    public static final String ORDER_DEAD_LETTER_QUEUE = "order.dead.queue";
    public static final String ROUTING_KEY_DELAY       = "order.delay";
    public static final String ROUTING_KEY_DEAD        = "order.dead";

    @Bean
    public CustomExchange delayExchange() {
        return new CustomExchange(ORDER_DELAY_EXCHANGE, "x-delayed-message", true, false);
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(ORDER_DELAY_QUEUE).build();
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(ROUTING_KEY_DELAY).noargs();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(ORDER_DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(delayExchange()).with(ROUTING_KEY_DEAD).noargs();
    }
}
