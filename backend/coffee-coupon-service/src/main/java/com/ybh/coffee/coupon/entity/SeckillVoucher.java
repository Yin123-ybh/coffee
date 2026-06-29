package com.ybh.coffee.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("seckill_voucher")
public class SeckillVoucher {
    @TableId
    private Long voucherId;
    private Integer stock;
    private Integer limitPerUser;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;
}
