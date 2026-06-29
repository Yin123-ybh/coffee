package com.ybh.coffee.seckill.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.ybh.coffee.common.context.UserContext;
import com.ybh.coffee.common.dto.SeckillOrderMessage;
import com.ybh.coffee.common.exception.BizException;
import com.ybh.coffee.seckill.entity.VoucherSnapshot;
import com.ybh.coffee.seckill.mapper.VoucherSnapshotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeckillService {
    private static final String TOPIC = "coffee.seckill.order";
    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT = new DefaultRedisScript<>("""
            local stockKey = KEYS[1]
            local userKey = KEYS[2]
            local userId = ARGV[1]
            local stock = tonumber(redis.call('get', stockKey) or '-1')
            if stock <= 0 then
                return 1
            end
            if redis.call('sismember', userKey, userId) == 1 then
                return 2
            end
            redis.call('decr', stockKey)
            redis.call('sadd', userKey, userId)
            return 0
            """, Long.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, SeckillOrderMessage> kafkaTemplate;
    private final VoucherSnapshotMapper voucherSnapshotMapper;

    public Long createSeckillOrder(Long voucherId) {
        Long userId = UserContext.getUserId();
        String stockKey = "seckill:stock:" + voucherId;
        String userKey = "seckill:order:users:" + voucherId;
        Long result = redisTemplate.execute(SECKILL_SCRIPT, List.of(stockKey, userKey), userId.toString());
        if (result == null || result == 1) {
            throw new BizException("优惠券库存不足");
        }
        if (result == 2) {
            throw new BizException("同一用户不能重复下单");
        }

        VoucherSnapshot voucher = voucherSnapshotMapper.selectById(voucherId);
        if (voucher == null) {
            throw new BizException("优惠券不存在");
        }
        Long orderId = SNOWFLAKE.nextId();
        SeckillOrderMessage message = new SeckillOrderMessage(orderId, userId, voucherId,
                voucher.getShopId(), voucher.getTitle(), voucher.getPayValue());
        kafkaTemplate.send(TOPIC, voucherId.toString(), message);
        return orderId;
    }
}
