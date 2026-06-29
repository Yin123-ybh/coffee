package com.ybh.coffee.order.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybh.coffee.common.context.UserContext;
import com.ybh.coffee.common.dto.OrderPaidEvent;
import com.ybh.coffee.common.dto.SeckillOrderMessage;
import com.ybh.coffee.common.exception.BizException;
import com.ybh.coffee.order.entity.VoucherOrder;
import com.ybh.coffee.order.mapper.VoucherOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final VoucherOrderMapper orderMapper;
    private final RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(SeckillOrderMessage message) {
        VoucherOrder order = new VoucherOrder();
        order.setId(message.getOrderId());
        order.setOrderNo("CO" + IdUtil.getSnowflakeNextIdStr());
        order.setUserId(message.getUserId());
        order.setVoucherId(message.getVoucherId());
        order.setShopId(message.getShopId());
        order.setVoucherTitle(message.getVoucherTitle());
        order.setPayAmount(message.getPayAmount());
        order.setStatus(1);
        try {
            orderMapper.insert(order);
        } catch (DuplicateKeyException e) {
            throw new BizException("用户已购买过该优惠券");
        }
    }

    public List<VoucherOrder> myOrders() {
        return orderMapper.selectList(new LambdaQueryWrapper<VoucherOrder>()
                .eq(VoucherOrder::getUserId, UserContext.getUserId())
                .orderByDesc(VoucherOrder::getCreatedAt));
    }

    public VoucherOrder detail(Long id) {
        VoucherOrder order = orderMapper.selectById(id);
        if (order == null || !order.getUserId().equals(UserContext.getUserId())) {
            throw new BizException("订单不存在");
        }
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    public void pay(Long id) {
        VoucherOrder order = detail(id);
        if (order.getStatus() != 1) {
            throw new BizException("订单状态不可支付");
        }
        order.setStatus(2);
        order.setPaidAt(LocalDateTime.now());
        orderMapper.updateById(order);
        rabbitTemplate.convertAndSend("coffee.order.exchange", "paid",
                new OrderPaidEvent(order.getId(), order.getVoucherId(), order.getPayAmount()));
    }

    public void cancel(Long id) {
        VoucherOrder order = detail(id);
        if (order.getStatus() != 1) {
            throw new BizException("订单状态不可取消");
        }
        order.setStatus(3);
        orderMapper.updateById(order);
    }
}
