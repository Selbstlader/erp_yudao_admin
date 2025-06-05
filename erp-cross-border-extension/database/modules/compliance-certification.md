# 合规与认证管理数据库设计

## 3.1 认证类型表 (erp_certification_type)

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

## 3.2 产品认证表 (erp_product_certification)

```sql
CREATE TABLE erp_product_certification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL COMMENT '关联产品ID',
  certification_type_id BIGINT NOT NULL COMMENT '认证类型ID',
  certificate_number VARCHAR(100) NOT NULL COMMENT '证书编号',
  issue_date DATE NOT NULL COMMENT '发证日期',
  expiry_date DATE COMMENT '到期日期',
  certificate_file VARCHAR(255) COMMENT '证书文件路径',
  status TINYINT NOT NULL COMMENT '状态(1-有效,2-即将到期,3-已过期)',
  remark TEXT COMMENT '备注',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  UNIQUE KEY uk_product_cert (product_id, certification_type_id, deleted)
) COMMENT '产品认证表';
```

## 3.3 HS编码表 (erp_hs_code)

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

## 3.4 产品海关信息表 (erp_product_customs)

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

## 3.5 国家进口限制表 (erp_country_import_restriction)

```sql
CREATE TABLE erp_country_import_restriction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country_code VARCHAR(10) NOT NULL COMMENT '国家代码',
  hs_code_id BIGINT COMMENT 'HS编码ID',
  product_category VARCHAR(100) COMMENT '产品类别',
  restriction_type TINYINT NOT NULL COMMENT '限制类型(1-禁止,2-限制,3-特殊要求)',
  description TEXT COMMENT '限制描述',
  required_documents TEXT COMMENT '所需文件(JSON格式)',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE COMMENT '失效日期',
  creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted BIT NOT NULL DEFAULT 0 COMMENT '是否删除',
  KEY idx_country_code (country_code),
  KEY idx_hs_code (hs_code_id)
) COMMENT '国家进口限制表';
```