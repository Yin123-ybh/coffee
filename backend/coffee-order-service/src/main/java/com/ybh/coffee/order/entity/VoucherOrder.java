package com.ybh.coffee.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("voucher_order")
public class VoucherOrder {
    private Long id;
    private Long userId;
    private Long voucherId;
    private Long shopId;
    private String voucherTitle;
    private Integer payAmount;
    private Integer status;
    private String orderNo;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime updatedAt;
}
