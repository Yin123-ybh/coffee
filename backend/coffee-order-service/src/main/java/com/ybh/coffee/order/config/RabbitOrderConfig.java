package com.ybh.coffee.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitOrderConfig {
    public static final String ORDER_EXCHANGE = "coffee.order.exchange";
    public static final String ORDER_RETRY_QUEUE = "coffee.order.retry.queue";
    public static final String ORDER_DLX = "coffee.order.dlx";
    public static final String ORDER_DEAD_QUEUE = "coffee.order.dead.queue";

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange orderDlx() {
        return new DirectExchange(ORDER_DLX, true, false);
    }

    @Bean
    public Queue orderRetryQueue() {
        return new Queue(ORDER_RETRY_QUEUE, true, false, false, Map.of(
                "x-dead-letter-exchange", ORDER_DLX,
                "x-dead-letter-routing-key", "dead",
                "x-message-ttl", 30000
        ));
    }

    @Bean
    public Queue orderDeadQueue() {
        return new Queue(ORDER_DEAD_QUEUE, true);
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(orderRetryQueue()).to(orderExchange()).with("retry");
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(orderDeadQueue()).to(orderDlx()).with("dead");
    }
}
