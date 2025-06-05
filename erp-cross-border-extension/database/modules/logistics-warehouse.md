# 国际物流与仓储支持数据库设计

## 2.1 国际仓库表 (erp_warehouse_international)

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

## 2.2 物流服务商表 (erp_logistics_provider)

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

## 2.3 物流方式表 (erp_logistics_method)

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

## 2.4 物流费用规则表 (erp_logistics_fee_rule)

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
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  KEY idx_logistics_method (logistics_method_id),
  KEY idx_countries (source_country, dest_country)
) COMMENT '物流费用规则表';
```

## 2.5 物流追踪表 (erp_logistics_tracking)

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

## 2.6 物流追踪历史表 (erp_logistics_tracking_history)

```sql
CREATE TABLE erp_logistics_tracking_history (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tracking_id BIGINT NOT NULL COMMENT '物流追踪ID',
  tracking_number VARCHAR(100) NOT NULL COMMENT '物流追踪号',
  status TINYINT NOT NULL COMMENT '物流状态',
  location VARCHAR(200) COMMENT '当前位置',
  description TEXT COMMENT '状态描述',
  track_time DATETIME NOT NULL COMMENT '追踪时间',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  KEY idx_tracking_id (tracking_id),
  KEY idx_tracking_number (tracking_number)
) COMMENT '物流追踪历史表';
```