package com.ybh.coffee.product.controller;

import com.ybh.coffee.common.api.Result;
import com.ybh.coffee.product.entity.ProductItem;
import com.ybh.coffee.product.entity.Shop;
import com.ybh.coffee.product.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductQueryService productQueryService;

    @GetMapping("/shops")
    public Result<List<Shop>> shops(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String type) {
        return Result.ok(productQueryService.queryShops(keyword, type));
    }

    @GetMapping("/items")
    public Result<List<ProductItem>> items(@RequestParam(required = false) Long shopId) {
        return Result.ok(productQueryService.queryItems(shopId));
    }

    @GetMapping("/items/{id}")
    public Result<ProductItem> item(@PathVariable Long id) {
        return Result.ok(productQueryService.getItem(id));
    }
}
