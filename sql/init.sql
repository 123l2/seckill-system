CREATE DATABASE IF NOT EXISTS seckill DEFAULT CHARACTER SET utf8mb4;

USE seckill;

-- Stock table
CREATE TABLE IF NOT EXISTS stock (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT       NOT NULL COMMENT '商品ID',
    product_name    VARCHAR(128) NOT NULL COMMENT '商品名称',
    total_stock     INT          NOT NULL DEFAULT 0 COMMENT '总库存',
    remaining_stock INT          NOT NULL DEFAULT 0 COMMENT '剩余库存',
    version         INT          NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Order table
CREATE TABLE IF NOT EXISTS orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL COMMENT '用户ID',
    product_id  BIGINT       NOT NULL COMMENT '商品ID',
    order_no    VARCHAR(64)  NOT NULL COMMENT '订单号',
    amount      DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
    status      TINYINT      NOT NULL DEFAULT 0 COMMENT '0-待支付 1-已支付 2-已取消',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expire_time DATETIME     NOT NULL COMMENT '过期时间',
    pay_time    DATETIME     NULL COMMENT '支付时间',
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Init product data
INSERT INTO stock (product_id, product_name, total_stock, remaining_stock) VALUES
(1, '限量版机械键盘', 100, 100),
(2, '无线降噪耳机', 50, 50);
