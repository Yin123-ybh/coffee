package com.ybh.coffee.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("product_item")
public class ProductItem {
    private Long id;
    private Long shopId;
    private String name;
    private String category;
    private String description;
    private Integer price;
    private Integer stock;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
