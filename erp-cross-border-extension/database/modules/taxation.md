# 税务管理数据库设计

## 4.1 税种表 (erp_tax_type)

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

## 4.2 国家税率表 (erp_country_tax_rate)

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
  KEY idx_country_tax (country_code, tax_type_id)
) COMMENT '国家税率表';
```

## 4.3 产品税分类表 (erp_product_tax_category)

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

## 4.4 产品税务信息表 (erp_product_tax)

```sql
CREATE TABLE erp_product_tax (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  tax_category_id BIGINT NOT NULL COMMENT '税分类ID',
  is_taxable BIT NOT NULL DEFAULT 1 COMMENT '是否应税',
  tax_rate DECIMAL(10,4) COMMENT '特殊税率(覆盖默认税率)',
  tax_code VARCHAR(50) COMMENT '税务代码',
  remark TEXT COMMENT '备注',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_country (product_id, country_code, deleted)
) COMMENT '产品税务信息表';
```

## 4.5 增值税注册表 (erp_vat_registration)

```sql
CREATE TABLE erp_vat_registration (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  vat_number VARCHAR(50) NOT NULL COMMENT '增值税注册号',
  registration_name VARCHAR(200) NOT NULL COMMENT '注册名称',
  registration_address TEXT NOT NULL COMMENT '注册地址',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE COMMENT '失效日期',
  filing_frequency TINYINT NOT NULL COMMENT '申报频率(1-月度,2-季度,3-年度)',
  filing_due_day INT NOT NULL COMMENT '申报截止日',
  status TINYINT NOT NULL COMMENT '状态(1-有效,2-即将到期,3-已过期)',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_country_vat (country_code, vat_number, deleted)
) COMMENT '增值税注册表';
```

## 4.6 税务申报记录表 (erp_tax_filing)

```sql
CREATE TABLE erp_tax_filing (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vat_registration_id BIGINT NOT NULL COMMENT '增值税注册ID',
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  period_start_date DATE NOT NULL COMMENT '申报期开始日期',
  period_end_date DATE NOT NULL COMMENT '申报期结束日期',
  filing_due_date DATE NOT NULL COMMENT '申报截止日期',
  filing_date DATE COMMENT '实际申报日期',
  total_sales DECIMAL(19,6) NOT NULL COMMENT '总销售额',
  total_tax DECIMAL(19,6) NOT NULL COMMENT '总税额',
  currency_id BIGINT NOT NULL COMMENT '货币ID',
  status TINYINT NOT NULL COMMENT '状态(1-待申报,2-已申报,3-已逾期)',
  filing_reference VARCHAR(100) COMMENT '申报参考号',
  filing_document VARCHAR(255) COMMENT '申报文件路径',