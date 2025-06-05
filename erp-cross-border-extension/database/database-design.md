# 跨境电商ERP系统数据库设计

## 文档信息
- **文档版本**：1.0.0
- **创建日期**：2023-07-10
- **最后更新**：2023-07-10
- **作者**：数据库设计团队

## 1. 多语言/多币种支持

### 1.1 产品多语言表 (erp_product_i18n)

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

### 1.2 货币表 (erp_currency)

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

### 1.3 汇率历史表 (erp_exchange_rate_history)

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

### 1.4 市场表 (erp_market)

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

### 1.5 产品价格表 (erp_product_price)

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

## 2. 国际物流与仓储支持

### 2.1 国际仓库表 (erp_warehouse_international)

```sql
CREATE TABLE erp_warehouse_international (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  warehouse_id BIGINT NOT NULL COMMENT '关联基础仓库ID',
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  region VARCHAR(100) COMMENT '地区/州/省',
  city VARCHAR(100) COMMENT '城市',
  postal_code VARCHAR(20) COMMENT '邮政编码',
  address TEXT COMMENT '详细地址',
  contact_name VARCHAR(100) COMMENT '联系人',
  contact_phone VARCHAR(20) COMMENT '联系电话',
  contact_email VARCHAR(100) COMMENT '联系邮箱',
  customs_code VARCHAR(50) COMMENT '海关编码',
  is_bonded BIT DEFAULT 0 COMMENT '是否保税仓',
  vat_number VARCHAR(50) COMMENT '增值税号',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除'
) COMMENT '国际仓库信息表';
```

### 2.2 物流服务商表 (erp_logistics_provider)

```sql
CREATE TABLE erp_logistics_provider (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '服务商代码',
  name VARCHAR(100) NOT NULL COMMENT '服务商名称',
  website VARCHAR(255) COMMENT '官网地址',
  api_config TEXT COMMENT 'API配置(JSON格式)',
  status TINYINT NOT NULL COMMENT '状态(0-禁用, 1-启用)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '物流服务商表';
```

### 2.3 物流方式表 (erp_logistics_method)

```sql
CREATE TABLE erp_logistics_method (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '物流方式代码',
  name VARCHAR(100) NOT NULL COMMENT '物流方式名称',
  provider_id BIGINT COMMENT '物流服务商ID',
  shipping_type TINYINT NOT NULL COMMENT '运输类型(1-海运,2-空运,3-陆运,4-铁路,5-快递)',
  estimated_days INT NOT NULL COMMENT '预计送达天数',
  tracking_url VARCHAR(255) COMMENT '物流追踪URL模板',
  status TINYINT NOT NULL COMMENT '状态(0-禁用, 1-启用)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '物流方式表';
```

### 2.4 物流费用规则表 (erp_logistics_fee_rule)

```sql
CREATE TABLE erp_logistics_fee_rule (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  logistics_method_id BIGINT NOT NULL COMMENT '物流方式ID',
  source_country VARCHAR(10) NOT NULL COMMENT '起始国家代码',
  dest_country VARCHAR(10) NOT NULL COMMENT '目的国家代码',
  weight_start DECIMAL(10,2) NOT NULL COMMENT '重量起始值(kg)',
  weight_end DECIMAL(10,2) NOT NULL COMMENT '重量结束值(kg)',
  first_weight_fee DECIMAL(10,2) NOT NULL COMMENT '首重费用',
  additional_weight_fee DECIMAL(10,2) NOT NULL COMMENT '续重费用(每kg)',
  currency_id BIGINT NOT NULL COMMENT '货币ID',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE COMMENT '失效日期',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除'
) COMMENT '物流费用规则表';
```

### 2.5 跨境物流追踪表 (erp_logistics_tracking)

```sql
CREATE TABLE erp_logistics_tracking (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT COMMENT '关联订单ID',
  logistics_method_id BIGINT NOT NULL COMMENT '物流方式ID',
  tracking_number VARCHAR(100) NOT NULL COMMENT '物流追踪号',
  source_country VARCHAR(10) NOT NULL COMMENT '起始国家代码',
  dest_country VARCHAR(10) NOT NULL COMMENT '目的国家代码',
  status TINYINT NOT NULL COMMENT '物流状态(1-待发货,2-已发货,3-运输中,4-清关中,5-已送达,6-异常)',
  last_track_time DATETIME COMMENT '最后追踪时间',
  estimated_delivery_time DATETIME COMMENT '预计送达时间',
  tracking_details TEXT COMMENT '追踪详情(JSON格式)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  KEY idx_tracking_number (tracking_number),
  KEY idx_order_id (order_id)
) COMMENT '跨境物流追踪表';
```

## 3. 合规与认证管理

### 3.1 认证类型表 (erp_certification_type)

```sql
CREATE TABLE erp_certification_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '认证代码',
  name VARCHAR(100) NOT NULL COMMENT '认证名称',
  description TEXT COMMENT '认证描述',
  issuing_organization VARCHAR(200) COMMENT '发证机构',
  applicable_countries TEXT COMMENT '适用国家(JSON格式)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '认证类型表';
```

### 3.2 产品认证表 (erp_product_certification)

```sql
CREATE TABLE erp_product_certification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  certification_type_id BIGINT NOT NULL COMMENT '认证类型ID',
  certificate_number VARCHAR(100) COMMENT '证书编号',
  issue_date DATE COMMENT '发证日期',
  expiry_date DATE COMMENT '到期日期',
  certificate_file VARCHAR(255) COMMENT '证书文件路径',
  status TINYINT NOT NULL COMMENT '状态(1-有效,2-即将到期,3-已过期,4-审核中)',
  remark TEXT COMMENT '备注',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_cert (product_id, certification_type_id, deleted)
) COMMENT '产品认证表';
```

### 3.3 HS编码表 (erp_hs_code)

```sql
CREATE TABLE erp_hs_code (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) NOT NULL COMMENT 'HS编码',
  description TEXT COMMENT '描述',
  category VARCHAR(100) COMMENT '类别',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT 'HS编码表';
```

### 3.4 产品海关信息表 (erp_product_customs)

```sql
CREATE TABLE erp_product_customs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  hs_code_id BIGINT NOT NULL COMMENT 'HS编码ID',
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  declared_value DECIMAL(19,6) COMMENT '申报价值',
  currency_id BIGINT COMMENT '申报货币ID',
  origin_country VARCHAR(10) COMMENT '原产国',
  is_restricted BIT DEFAULT 0 COMMENT '是否限制商品',
  is_prohibited BIT DEFAULT 0 COMMENT '是否禁止商品',
  customs_description VARCHAR(500) COMMENT '海关申报描述',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_country (product_id, country_code, deleted)
) COMMENT '产品海关信息表';
```

## 4. 税务管理

### 4.1 税种表 (erp_tax_type)

```sql
CREATE TABLE erp_tax_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '税种代码',
  name VARCHAR(100) NOT NULL COMMENT '税种名称',
  description TEXT COMMENT '税种描述',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '税种表';
```

### 4.2 国家税率表 (erp_country_tax_rate)

```sql
CREATE TABLE erp_country_tax_rate (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  tax_type_id BIGINT NOT NULL COMMENT '税种ID',
  rate DECIMAL(10,4) NOT NULL COMMENT '税率',
  threshold_value DECIMAL(19,6) COMMENT '起征点金额',
  threshold_currency_id BIGINT COMMENT '起征点货币ID',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE COMMENT '失效日期',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_country_tax_date (country_code, tax_type_id, effective_date, deleted)
) COMMENT '国家税率表';
```

### 4.3 产品税分类表 (erp_product_tax_category)

```sql
CREATE TABLE erp_product_tax_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL COMMENT '分类代码',
  name VARCHAR(100) NOT NULL COMMENT '分类名称',
  description TEXT COMMENT '分类描述',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_code (code, deleted)
) COMMENT '产品税分类表';
```

### 4.4 产品税务信息表 (erp_product_tax)

```sql
CREATE TABLE erp_product_tax (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  tax_category_id BIGINT COMMENT '税分类ID',
  is_taxable BIT NOT NULL DEFAULT 1 COMMENT '是否应税',
  special_rate DECIMAL(10,4) COMMENT '特殊税率',
  tax_code VARCHAR(50) COMMENT '税务代码',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_country (product_id, country_code, deleted)
) COMMENT '产品税务信息表';
```

### 4.5 增值税注册表 (erp_vat_registration)

```sql
CREATE TABLE erp_vat_registration (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  vat_number VARCHAR(50) NOT NULL COMMENT '增值税注册号',
  registration_name VARCHAR(200) NOT NULL COMMENT '注册名称',
  registration_address TEXT COMMENT '注册地址',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE COMMENT '失效日期',
  filing_frequency TINYINT COMMENT '申报频率(1-月度,2-季度,3-半年,4-年度)',
  filing_due_day INT COMMENT '申报截止日',
  status TINYINT NOT NULL COMMENT '状态(1-有效,2-即将到期,3-已过期)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_country_vat (country_code, vat_number, deleted)
) COMMENT '增值税注册表';
``` 