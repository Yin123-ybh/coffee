package com.ybh.coffee.coupon.controller;

import com.ybh.coffee.common.api.Result;
import com.ybh.coffee.coupon.entity.Voucher;
import com.ybh.coffee.coupon.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping
    public Result<List<Voucher>> list(@RequestParam(required = false) Long shopId) {
        return Result.ok(voucherService.queryByShop(shopId));
    }

    @GetMapping("/{id}")
    public Result<Voucher> detail(@PathVariable Long id) {
        return Result.ok(voucherService.detail(id));
    }
}
