# 分布式咖啡订购平台

基于黑马点评业务改造的前后端分离微服务项目，围绕咖啡门店、商品、优惠券、秒杀下单、订单状态流转和运营统计进行拆分。项目重点体现高并发秒杀、缓存预扣减、异步下单、消息可靠性、服务治理和网关统一入口。

## 技术栈

后端：Spring Boot、Spring Cloud Alibaba、Spring Cloud Gateway、Nacos、Sentinel、MyBatis-Plus、MySQL、Redis、Kafka、RabbitMQ、Maven

前端：Vue 3、Vite、TypeScript、Pinia、Axios

## 模块划分

```text
backend/
  coffee-common          公共返回体、异常、DTO、上下文
  coffee-gateway         网关路由、跨域、鉴权过滤、Sentinel 网关限流
  coffee-product-service 商品与门店服务
  coffee-coupon-service  优惠券服务、热点优惠券缓存
  coffee-seckill-service 秒杀服务、Redis Lua 预扣减、Kafka 投递
  coffee-order-service   订单服务、Kafka 消费、事务落库、幂等校验
  coffee-stat-service    统计服务、消息消费与运营指标
frontend/                独立前端项目，通过 Gateway 调用后端接口
docs/sql/                MySQL 初始化脚本
docs/api.md              接口说明
```

## 核心业务链路

1. 用户在前端浏览门店、咖啡商品和优惠券。
2. 秒杀请求进入 Gateway，完成跨域、鉴权和限流过滤。
3. 秒杀服务通过 Redis Lua 原子执行库存预扣减、用户资格校验和一人一单判断。
4. 通过校验的请求写入 Kafka，快速返回排队成功，削减数据库瞬时写入压力。
5. 订单服务消费 Kafka 消息，使用事务、MySQL 唯一索引和状态机完成订单落库。
6. 异常消息进入 RabbitMQ 死信链路，便于补偿和排查。
7. 统计服务消费订单事件，沉淀销量、成交额、秒杀成功数等运营指标。

## 本地启动

启动中间件：

```bash
docker compose up -d mysql redis kafka rabbitmq nacos
```

初始化数据库：

```bash
mysql -h 127.0.0.1 -P 3306 -u root -proot coffee_platform < docs/sql/init.sql
```

编译后端：

```bash
cd backend
mvn clean package -DskipTests
```

启动前端：

```bash
cd frontend
npm install
npm run dev
```

访问：`http://localhost:5173`

## 简历对应亮点

- 使用 Spring Cloud Alibaba 拆分商品、订单、优惠券、秒杀、统计等业务服务。
- 使用 Nacos 做服务注册发现和配置管理。
- 使用 Gateway 统一处理路由、跨域、用户透传和请求过滤。
- 使用 Redis Lua 解决秒杀库存超卖和重复下单问题。
- 使用 Kafka 异步削峰，订单服务事务落库并通过唯一索引兜底幂等。
- 使用 RabbitMQ 死信队列保存异常订单消息，便于后续补偿。
- 使用 Sentinel 对秒杀接口、热点参数和服务调用进行限流、熔断与降级。
- 使用 MyBatis-Plus 完成分层 CRUD 和业务查询。
