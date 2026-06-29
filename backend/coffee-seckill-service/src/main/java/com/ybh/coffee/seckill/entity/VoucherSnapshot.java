package com.ybh.coffee.seckill.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("voucher")
public class VoucherSnapshot {
    private Long id;
    private Long shopId;
    private String title;
    private Integer payValue;
    private Integer status;
}
