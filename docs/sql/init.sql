CREATE DATABASE IF NOT EXISTS coffee_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE coffee_platform;

DROP TABLE IF EXISTS stat_daily_summary;
DROP TABLE IF EXISTS voucher_order;
DROP TABLE IF EXISTS seckill_voucher;
DROP TABLE IF EXISTS voucher;
DROP TABLE IF EXISTS product_item;
DROP TABLE IF EXISTS shop;

CREATE TABLE shop (
  id BIGINT PRIMARY KEY,
  name VARCHAR(80) NOT NULL,
  type VARCHAR(32) NOT NULL,
  area VARCHAR(32) NOT NULL,
  address VARCHAR(120) NOT NULL,
  cover_url VARCHAR(255) NOT NULL,
  score DECIMAL(3,1) NOT NULL DEFAULT 5.0,
  avg_price INT NOT NULL DEFAULT 0,
  heat INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_shop_type_area (type, area),
  KEY idx_shop_heat (heat)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE product_item (
  id BIGINT PRIMARY KEY,
  shop_id BIGINT NOT NULL,
  name VARCHAR(80) NOT NULL,
  category VARCHAR(32) NOT NULL,
  description VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  stock INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_shop (shop_id),
  KEY idx_product_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE voucher (
  id BIGINT PRIMARY KEY,
  shop_id BIGINT NOT NULL,
  title VARCHAR(120) NOT NULL,
  sub_title VARCHAR(160) NOT NULL,
  pay_value INT NOT NULL,
  actual_value INT NOT NULL,
  begin_at DATETIME NOT NULL,
  end_at DATETIME NOT NULL,
  status TINYINT NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_voucher_shop (shop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE seckill_voucher (
  voucher_id BIGINT PRIMARY KEY,
  stock INT NOT NULL,
  limit_per_user INT NOT NULL DEFAULT 1,
  begin_at DATETIME NOT NULL,
  end_at DATETIME NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE voucher_order (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  voucher_id BIGINT NOT NULL,
  shop_id BIGINT NOT NULL,
  voucher_title VARCHAR(120) NOT NULL,
  pay_amount INT NOT NULL,
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1待支付 2已支付 3已取消 4已核销',
  order_no VARCHAR(40) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  paid_at DATETIME NULL,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_voucher (user_id, voucher_id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_order_user (user_id),
  KEY idx_order_voucher (voucher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE stat_daily_summary (
  id BIGINT PRIMARY KEY,
  stat_date DATE NOT NULL,
  voucher_id BIGINT NOT NULL,
  order_count INT NOT NULL DEFAULT 0,
  pay_amount INT NOT NULL DEFAULT 0,
  success_count INT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_stat_voucher_day (voucher_id, stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO shop (id, name, type, area, address, cover_url, score, avg_price, heat) VALUES
(1, '山城手冲咖啡', '精品咖啡', '五角场', '政通路 88 号', '/images/coffee-1.jpg', 4.8, 32, 1260),
(2, '巷口拿铁工坊', '咖啡甜品', '人民广场', '广西北路 219 号', '/images/coffee-2.jpg', 4.7, 28, 980),
(3, '北岸烘焙实验室', '烘焙轻食', '静安寺', '胶州路 310 号', '/images/coffee-3.jpg', 4.6, 45, 760);

INSERT INTO product_item (id, shop_id, name, category, description, price, stock, status) VALUES
(101, 1, '埃塞俄比亚手冲', '手冲', '花香、柑橘酸和轻盈茶感', 3600, 80, 1),
(102, 1, '冰博客拿铁', '拿铁', '低温浓缩奶底，口感厚实', 3200, 120, 1),
(201, 2, '燕麦拿铁', '拿铁', '燕麦奶搭配中深烘豆', 2800, 160, 1),
(202, 2, '巴斯克芝士蛋糕', '甜品', '焦香表层和绵密奶香', 2600, 60, 1),
(301, 3, '可颂早餐套餐', '轻食', '可颂、沙拉、热美式', 4500, 50, 1);

INSERT INTO voucher (id, shop_id, title, sub_title, pay_value, actual_value, begin_at, end_at, status) VALUES
(1001, 1, '9.9 元抢 30 元咖啡券', '限时秒杀，到店核销', 990, 3000, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 1),
(1002, 2, '18 元代 35 元饮品券', '全场饮品可用', 1800, 3500, NOW(), DATE_ADD(NOW(), INTERVAL 20 DAY), 1),
(1003, 3, '39 元早餐双人券', '轻食套餐组合', 3900, 6800, NOW(), DATE_ADD(NOW(), INTERVAL 25 DAY), 1);

INSERT INTO seckill_voucher (voucher_id, stock, limit_per_user, begin_at, end_at) VALUES
(1001, 50, 1, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY)),
(1002, 80, 1, NOW(), DATE_ADD(NOW(), INTERVAL 20 DAY)),
(1003, 30, 1, NOW(), DATE_ADD(NOW(), INTERVAL 25 DAY));
