package com.ybh.coffee.stat.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatRabbitConfig {
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange("coffee.order.exchange", true, false);
    }

    @Bean
    public Queue paidQueue() {
        return new Queue("coffee.order.paid.queue", true);
    }

    @Bean
    public Binding paidBinding() {
        return BindingBuilder.bind(paidQueue()).to(orderExchange()).with("paid");
    }
}
