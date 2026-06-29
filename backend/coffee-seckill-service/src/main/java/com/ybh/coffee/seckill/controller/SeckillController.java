package com.ybh.coffee.seckill.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ybh.coffee.common.api.Result;
import com.ybh.coffee.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seckill")
public class SeckillController {
    private final SeckillService seckillService;

    @PostMapping("/vouchers/{voucherId}/orders")
    @SentinelResource(value = "createSeckillOrder", fallback = "fallback")
    public Result<Long> create(@PathVariable Long voucherId) {
        return Result.ok(seckillService.createSeckillOrder(voucherId));
    }

    public Result<Long> fallback(Long voucherId, Throwable throwable) {
        return Result.fail(429, "当前抢购人数较多，请稍后再试");
    }
}
