package com.ybh.coffee.coupon.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybh.coffee.coupon.entity.Voucher;
import com.ybh.coffee.coupon.mapper.VoucherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private static final String CACHE_KEY = "coffee:voucher:";
    private final VoucherMapper voucherMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<Voucher> queryByShop(Long shopId) {
        return voucherMapper.selectList(new LambdaQueryWrapper<Voucher>()
                .eq(shopId != null, Voucher::getShopId, shopId)
                .eq(Voucher::getStatus, 1)
                .orderByAsc(Voucher::getPayValue));
    }

    public Voucher detail(Long id) {
        String key = CACHE_KEY + id;
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Voucher voucher) {
            return voucher;
        }
        Voucher voucher = voucherMapper.selectById(id);
        if (voucher != null) {
            redisTemplate.opsForValue().set(key, voucher, Duration.ofMinutes(10));
        }
        return voucher;
    }
}
