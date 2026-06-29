package com.ybh.coffee.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ybh.coffee.product.entity.ProductItem;
import com.ybh.coffee.product.entity.Shop;
import com.ybh.coffee.product.mapper.ProductItemMapper;
import com.ybh.coffee.product.mapper.ShopMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ShopMapper shopMapper;
    private final ProductItemMapper productItemMapper;

    public List<Shop> queryShops(String keyword, String type) {
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<Shop>()
                .eq(type != null && !type.isBlank(), Shop::getType, type)
                .and(keyword != null && !keyword.isBlank(), q -> q.like(Shop::getName, keyword)
                        .or().like(Shop::getArea, keyword)
                        .or().like(Shop::getAddress, keyword))
                .orderByDesc(Shop::getHeat);
        return shopMapper.selectList(wrapper);
    }

    public List<ProductItem> queryItems(Long shopId) {
        return productItemMapper.selectList(new LambdaQueryWrapper<ProductItem>()
                .eq(shopId != null, ProductItem::getShopId, shopId)
                .eq(ProductItem::getStatus, 1)
                .orderByAsc(ProductItem::getPrice));
    }

    public ProductItem getItem(Long id) {
        return productItemMapper.selectById(id);
    }
}
