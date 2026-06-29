package com.ybh.coffee.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("shop")
public class Shop {
    private Long id;
    private String name;
    private String type;
    private String area;
    private String address;
    private String coverUrl;
    private BigDecimal score;
    private Integer avgPrice;
    private Integer heat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
