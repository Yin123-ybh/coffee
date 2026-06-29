package com.ybh.coffee.stat.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("stat_daily_summary")
public class StatDailySummary {
    private Long id;
    private LocalDate statDate;
    private Long voucherId;
    private Integer orderCount;
    private Integer payAmount;
    private Integer successCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
