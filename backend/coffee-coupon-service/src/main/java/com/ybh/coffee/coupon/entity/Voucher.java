package com.ybh.coffee.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("voucher")
public class Voucher {
    private Long id;
    private Long shopId;
    private String title;
    private String subTitle;
    private Integer payValue;
    private Integer actualValue;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
