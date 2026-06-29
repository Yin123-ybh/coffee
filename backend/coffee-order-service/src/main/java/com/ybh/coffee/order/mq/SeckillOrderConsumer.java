package com.ybh.coffee.order.mq;

import com.ybh.coffee.common.dto.SeckillOrderMessage;
import com.ybh.coffee.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeckillOrderConsumer {
    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = "coffee.seckill.order", containerFactory = "seckillKafkaListenerContainerFactory")
    public void consume(SeckillOrderMessage message, Acknowledgment ack) {
        try {
            orderService.createOrder(message);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("create seckill order failed, message={}", message, e);
            rabbitTemplate.convertAndSend("coffee.order.exchange", "retry", message);
            ack.acknowledge();
        }
    }
}
