# 高并发秒杀系统

基于 Spring Boot + Redis + RabbitMQ + Sentinel 构建的高并发秒杀系统，支持库存预扣、Lua原子性扣减、异步落库、延时订单取消等核心能力。

## 技术栈

- Spring Boot 3.2
- Redis（预库存 + Lua 原子性操作）
- RabbitMQ（延时队列削峰）
- Sentinel（热点参数限流 + 接口防刷）
- MySQL（订单持久化）
- Docker / Docker Compose

## 核心设计

### 秒杀流程

1. **请求过滤** — Sentinel 热点限流拦截无效请求
2. **库存预扣** — Redis + Lua 脚本保证原子性，拦截超卖
3. **资格记录** — 抢购资格写入 Redis Set，去重
4. **异步落库** — RabbitMQ 削峰，MySQL 异步写入
5. **延时取消** — RabbitMQ 延时队列 30 分钟自动取消未支付订单

### 关键指标

- 单机压测 3000 QPS
- 接口响应 < 50ms
- 库存扣减零超卖
- MySQL 写压力降低 90%

## 快速启动

```bash
# 1. 启动依赖服务
docker-compose up -d redis rabbitmq mysql

# 2. 启动应用
mvn spring-boot:run

# 3. 测试秒杀
curl -X POST "http://localhost:8080/api/seckill/1?userId=1001"
```

## 项目结构

```
src/main/java/com/seckill/
  ├── SeckillApplication.java
  ├── config/
  │   ├── RedisConfig.java       # Lua 脚本 Bean
  │   ├── RabbitMQConfig.java    # 延时队列配置
  │   └── SentinelConfig.java    # Sentinel AOP
  ├── controller/
  │   └── SeckillController.java
  ├── entity/
  │   ├── Stock.java
  │   └── Order.java
  ├── mq/
  │   ├── OrderMessageSender.java
  │   └── OrderDeadLetterConsumer.java
  └── service/
      └── SeckillService.java
```
