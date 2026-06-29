package com.ybh.coffee.stat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybh.coffee.common.api.Result;
import com.ybh.coffee.stat.entity.StatDailySummary;
import com.ybh.coffee.stat.mapper.StatDailySummaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatController {
    private final StatDailySummaryMapper statMapper;

    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        List<StatDailySummary> list = statMapper.selectList(new LambdaQueryWrapper<StatDailySummary>());
        int orderCount = list.stream().mapToInt(StatDailySummary::getOrderCount).sum();
        int payAmount = list.stream().mapToInt(StatDailySummary::getPayAmount).sum();
        int successCount = list.stream().mapToInt(StatDailySummary::getSuccessCount).sum();
        return Result.ok(Map.of("orderCount", orderCount, "payAmount", payAmount, "successCount", successCount));
    }

    @GetMapping("/vouchers/top")
    public Result<List<StatDailySummary>> topVouchers() {
        List<StatDailySummary> list = statMapper.selectList(new LambdaQueryWrapper<StatDailySummary>());
        return Result.ok(list.stream()
                .sorted(Comparator.comparing(StatDailySummary::getSuccessCount).reversed())
                .limit(10)
                .toList());
    }
}
