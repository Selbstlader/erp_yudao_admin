# 多语言/多币种支持数据库设计

## 1.1 产品多语言表 (erp_product_i18n)

```sql
CREATE TABLE erp_product_i18n (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  locale VARCHAR(10) NOT NULL COMMENT '语言代码(如en_US, zh_CN)',
  name VARCHAR(200) NOT NULL COMMENT '本地化产品名称',
  description TEXT COMMENT '本地化产品描述',
  keywords VARCHAR(500) COMMENT 'SEO关键词',
  meta_title VARCHAR(200) COMMENT 'SEO标题',
  meta_description VARCHAR(500) COMMENT 'SEO描述',
  standard VARCHAR(200) COMMENT '本地化产品规格',
  remark TEXT COMMENT '本地化备注',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_locale (product_id, locale, deleted)
) COMMENT '产品多语言信息表';
```

## 1.2 货币表 (erp_currency)

```sql
CREATE TABLE erp_currency (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(10) NOT NULL COMMENT '货币代码(如USD, CNY)',
  name VARCHAR(50) NOT NULL COMMENT '货币名称',
  symbol VARCHAR(10) NOT NULL COMMENT '货币符号',
  exchange_rate DECIMAL(19,6) NOT NULL COMMENT '兑换率(相对于基准货币)',
  is_base BIT NOT NULL DEFAULT 0 COMMENT '是否为基准货币',
  decimal_places INT NOT NULL DEFAULT 2 COMMENT '小数位数',
  rounding_mode TINYINT NOT NULL DEFAULT 0 COMMENT '舍入模式(0-四舍五入,1-向上,2-向下)',
  status TINYINT NOT NULL COMMENT '状态(0-禁用, 1-启用)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '货币表';
```

## 1.3 汇率历史表 (erp_exchange_rate_history)

```sql
CREATE TABLE erp_exchange_rate_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  currency_id BIGINT NOT NULL COMMENT '货币ID',
  exchange_rate DECIMAL(19,6) NOT NULL COMMENT '兑换率',
  effective_date DATE NOT NULL COMMENT '生效日期',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  KEY idx_currency_date (currency_id, effective_date)
) COMMENT '汇率历史表';
```

## 1.4 市场表 (erp_market)

```sql
CREATE TABLE erp_market (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '市场代码',
  name VARCHAR(100) NOT NULL COMMENT '市场名称',
  countries VARCHAR(500) COMMENT '覆盖国家(JSON格式)',
  default_currency_id BIGINT COMMENT '默认货币ID',
  default_locale VARCHAR(10) COMMENT '默认语言',
  status TINYINT NOT NULL COMMENT '状态(0-禁用, 1-启用)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '市场表';
```

## 1.5 产品价格表 (erp_product_price)

```sql
CREATE TABLE erp_product_price (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  currency_id BIGINT NOT NULL COMMENT '货币ID',
  market_id BIGINT NOT NULL COMMENT '市场ID',
  sale_price DECIMAL(19,6) NOT NULL COMMENT '销售价格',
  min_price DECIMAL(19,6) NOT NULL COMMENT '最低销售价格',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_currency_market (product_id, currency_id, market_id, deleted)
) COMMENT '产品价格表';
```

## 1.6 系统支持语言表 (erp_supported_locale)

```sql
CREATE TABLE erp_supported_locale (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  locale VARCHAR(10) NOT NULL COMMENT '语言代码(如en_US, zh_CN)',
  name VARCHAR(50) NOT NULL COMMENT '语言名称',
  native_name VARCHAR(50) NOT NULL COMMENT '本地语言名称',
  flag_icon VARCHAR(100) COMMENT '国旗图标路径',
  is_default BIT NOT NULL DEFAULT 0 COMMENT '是否为默认语言',
  status TINYINT NOT NULL COMMENT '状态(0-禁用, 1-启用)',
  sort INT NOT NULL DEFAULT 0 COMMENT '排序',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_locale (locale, deleted)
) COMMENT '系统支持语言表';
```