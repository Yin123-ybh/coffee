package com.ybh.coffee.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ybh.coffee.stat.entity.StatDailySummary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface StatDailySummaryMapper extends BaseMapper<StatDailySummary> {
    @Update("""
            INSERT INTO stat_daily_summary(id, stat_date, voucher_id, order_count, pay_amount, success_count)
            VALUES(#{id}, CURRENT_DATE, #{voucherId}, 1, #{payAmount}, 1)
            ON DUPLICATE KEY UPDATE
              order_count = order_count + 1,
              pay_amount = pay_amount + VALUES(pay_amount),
              success_count = success_count + 1
            """)
    int upsertPaid(@Param("id") Long id, @Param("voucherId") Long voucherId, @Param("payAmount") Integer payAmount);
}
