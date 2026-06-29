# 接口说明

网关地址：`http://localhost:8080`

## 商品服务

- `GET /api/products/shops?keyword=&type=` 查询门店
- `GET /api/products/items?shopId=` 查询商品
- `GET /api/products/items/{id}` 商品详情

## 优惠券服务

- `GET /api/coupons/vouchers?shopId=` 查询优惠券
- `GET /api/coupons/vouchers/{id}` 优惠券详情

## 秒杀服务

- `POST /api/seckill/vouchers/{voucherId}/orders` 秒杀下单

请求头：`X-User-Id: 1`

## 订单服务

- `GET /api/orders/my` 查询我的订单
- `GET /api/orders/{id}` 查询订单详情
- `POST /api/orders/{id}/pay` 支付订单
- `POST /api/orders/{id}/cancel` 取消订单

## 统计服务

- `GET /api/stats/overview` 运营概览
- `GET /api/stats/vouchers/top` 优惠券排行
