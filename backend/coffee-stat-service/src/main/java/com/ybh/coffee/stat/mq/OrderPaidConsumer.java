package com.ybh.coffee.stat.mq;

import cn.hutool.core.util.IdUtil;
import com.ybh.coffee.common.dto.OrderPaidEvent;
import com.ybh.coffee.stat.mapper.StatDailySummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPaidConsumer {
    private final StatDailySummaryMapper statMapper;

    @RabbitListener(queues = "coffee.order.paid.queue")
    public void onPaid(OrderPaidEvent event) {
        statMapper.upsertPaid(IdUtil.getSnowflakeNextId(), event.getVoucherId(), event.getPayAmount());
    }
}
