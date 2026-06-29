package com.ybh.coffee.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillOrderMessage implements Serializable {
    private Long orderId;
    private Long userId;
    private Long voucherId;
    private Long shopId;
    private String voucherTitle;
    private Integer payAmount;
}
