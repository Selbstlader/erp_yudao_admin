# 合规与认证管理API

## 3.1 认证类型管理API

```
# 创建认证类型
POST /api/v1/erp/certification/type/create
Request:
{
  "code": "CE",                // 认证代码
  "name": "CE Certification",  // 认证名称
  "description": "European Conformity certification", // 认证描述
  "issuingOrganization": "European Commission", // 发证机构
  "applicableCountries": ["DE", "FR", "IT", "ES"] // 适用国家
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新认证类型
PUT /api/v1/erp/certification/type/update
Request: 同创建，但需要增加id字段

# 删除认证类型
DELETE /api/v1/erp/certification/type/delete?id=1

# 获取认证类型详情
GET /api/v1/erp/certification/type/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "code": "CE",
    "name": "CE Certification",
    "description": "European Conformity certification",
    "issuingOrganization": "European Commission",
    "applicableCountries": ["DE", "FR", "IT", "ES"],
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取认证类型列表
GET /api/v1/erp/certification/type/list?pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "code": "CE",
        "name": "CE Certification",
        "description": "European Conformity certification",
        "issuingOrganization": "European Commission",
        "applicableCountries": ["DE", "FR", "IT", "ES"]
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 获取国家需要的认证类型
GET /api/v1/erp/certification/type/by-country?countryCode=DE
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "code": "CE",
      "name": "CE Certification",
      // 其他字段
    },
    // 更多记录
  ],
  "msg": "success"
}
```

## 3.2 产品认证管理API

```
# 创建产品认证
POST /api/v1/erp/product/certification/create
Request:
{
  "productId": 1001,           // 关联产品ID
  "certificationTypeId": 1,    // 认证类型ID
  "certificateNumber": "CE12345", // 证书编号
  "issueDate": "2023-01-15",   // 发证日期
  "expiryDate": "2025-01-14",  // 到期日期
  "certificateFile": "/uploads/certificates/CE12345.pdf", // 证书文件路径
  "status": 1,                 // 状态
  "remark": "Issued by TUV"    // 备注
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新产品认证
PUT /api/v1/erp/product/certification/update
Request: 同创建，但需要增加id字段

# 删除产品认证
DELETE /api/v1/erp/product/certification/delete?id=1

# 获取产品认证详情
GET /api/v1/erp/product/certification/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "productId": 1001,
    "productName": "Test Product",
    "certificationTypeId": 1,
    "certificationCode": "CE",
    "certificationName": "CE Certification",
    "certificateNumber": "CE12345",
    "issueDate": "2023-01-15",
    "expiryDate": "2025-01-14",
    "certificateFile": "/uploads/certificates/CE12345.pdf",
    "status": 1,
    "statusName": "有效",
    "remark": "Issued by TUV",
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取产品认证列表
GET /api/v1/erp/product/certification/list?productId=1001&status=1&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "productId": 1001,
        "productName": "Test Product",
        "certificationTypeId": 1,
        "certificationCode": "CE",
        "certificationName": "CE Certification",
        "certificateNumber": "CE12345",
        "issueDate": "2023-01-15",
        "expiryDate": "2025-01-14",
        "status": 1,
        "statusName": "有效"
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 上传认证文件
POST /api/v1/erp/product/certification/upload-file
Request: 使用multipart/form-data格式，上传文件
Response:
{
  "code": 0,
  "data": {
    "url": "/uploads/certificates/CE12345.pdf",
    "name": "CE12345.pdf",
    "size": 1024000
  },
  "msg": "success"
}

# 获取即将到期的认证
GET /api/v1/erp/product/certification/expiring?days=30&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "productId": 1001,
        "productName": "Test Product",
        "certificationTypeId": 1,
        "certificationName": "CE Certification",
        "expiryDate": "2023-08-10",
        "daysRemaining": 25
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 检查产品是否满足指定国家的认证要求
POST /api/v1/erp/product/certification/check-compliance
Request:
{
  "productId": 1001,
  "countryCode": "DE"
}
Response:
{
  "code": 0,
  "data": {
    "compliant": true,
    "missingCertifications": [],
    "expiredCertifications": [],
    "validCertifications": [
      {
        "id": 1,
        "code": "CE",
        "name": "CE Certification",
        "expiryDate": "2025-01-14"
      }
    ]
  },
  "msg": "success"
}
```

## 3.3 HS编码管理API

```
# 创建HS编码
POST /api/v1/erp/hs-code/create
Request:
{
  "code": "8471.30.00",        // HS编码
  "description": "Portable automatic data processing machines", // 描述
  "category": "Electronics"    // 类别
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新HS编码
PUT /api/v1/erp/hs-code/update
Request: 同创建，但需要增加id字段

# 删除HS编码
DELETE /api/v1/erp/hs-code/delete?id=1

# 获取HS编码详情
GET /api/v1/erp/hs-code/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "code": "8471.30.00",
    "description": "Portable automatic data processing machines",
    "category": "Electronics",
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取HS编码列表
GET /api/v1/erp/hs-code/list?category=Electronics&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "code": "8471.30.00",
        "description": "Portable automatic data processing machines",
        "category": "Electronics"
      },
      // 更多记录
    ],
    "total": 50,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 搜索HS编码
GET /api/v1/erp/hs-code/search?keyword=laptop&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "code": "8471.30.00",
        "description": "Portable automatic data processing machines",
        "category": "Electronics",
        "matchField": "description" // 匹配字段
      },
      // 更多记录
    ],
    "total": 15,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 批量导入HS编码
POST /api/v1/erp/hs-code/batch-import
Request: 使用multipart/form-data格式，上传Excel文件
Response:
{
  "code": 0,
  "data": {
    "success": 100,            // 成功导入数量
    "failed": 2,               // 失败数量
    "errors": [                // 错误详情
      {
        "row": 5,
        "message": "Invalid HS code format"
      },
      {
        "row": 10,
        "message": "Duplicate HS code"
      }
    ]
  },
  "msg": "success"
}
```

## 3.4 产品海关信息管理API

```
# 创建产品海关信息
POST /api/v1/erp/product/customs/create
Request:
{
  "productId": 1001,           // 关联产品ID
  "hsCodeId": 1,               // HS编码ID
  "countryCode": "US",         // 国家代码
  "declaredValue": 50.0,       // 申报价值
  "currencyId": 1,             // 申报货币ID
  "originCountry": "CN",       // 原产国
  "isRestricted": false,       // 是否限制商品
  "isProhibited": false,       // 是否禁止商品
  "customsDescription": "Laptop computer for personal use" // 海关申报描述
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新产品海关信息
PUT /api/v1/erp/product/customs/update
Request: 同创建，但需要增加id字段

# 删除产品海关信息
DELETE /api/v1/erp/product/customs/delete?id=1

# 获取产品海关信息详情
GET /api/v1/erp/product/customs/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "productId": 1001,
    "productName": "Test Laptop",
    "hsCodeId": 1,
    "hsCode": "8471.30.00",
    "hsCodeDescription": "Portable automatic data processing machines",
    "countryCode": "US",
    "countryName": "United States",
    "declaredValue": 50.0,
    "currencyId": 1,
    "currencyCode": "USD",
    "originCountry": "CN",
    "originCountryName": "China",
    "isRestricted": false,
    "isProhibited": false,
    "customsDescription": "Laptop computer for personal use",
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取产品海关信息列表
GET /api/v1/erp/product/customs/list?productId=1001&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "productId": 1001,
        "productName": "Test Laptop",
        "hsCodeId": 1,
        "hsCode": "8471.30.00",
        "countryCode": "US",
        "countryName": "United States",
        "declaredValue": 50.0,
        "currencyCode": "USD",
        "originCountry": "CN",
        "isRestricted": false,
        "isProhibited": false
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 批量保存产品海关信息
POST /api/v1/erp/product/customs/batch-save
Request:
{
  "productId": 1001,
  "items": [
    {
      "hsCodeId": 1,
      "countryCode": "US",
      "declaredValue": 50.0,
      "currencyId": 1,
      "originCountry": "CN",
      "isRestricted": false,
      "isProhibited": false,
      "customsDescription": "Laptop computer for personal use"
    },
    {
      "hsCodeId": 1,
      "countryCode": "CA",
      "declaredValue": 60.0,
      "currencyId": 2,
      "originCountry": "CN",
      "isRestricted": false,
      "isProhibited": false,
      "customsDescription": "Laptop for personal use"
    }
  ]
}
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}

# 检查产品在指定国家的进口限制
GET /api/v1/erp/product/customs/check-restrictions?productId=1001&countryCode=US
Response:
{
  "code": 0,
  "data": {
    "allowed": true,
    "restricted": false,
    "prohibited": false,
    "requiresPermit": false,
    "notes": "No restrictions for this product"
  },
  "msg": "success"
}

# 获取产品建议申报价值
GET /api/v1/erp/product/customs/suggested-value?productId=1001&countryCode=US
Response:
{
  "code": 0,
  "data": {
    "suggestedValue": 45.0,
    "currencyId": 1,
    "currencyCode": "USD",
    "minValue": 40.0,
    "maxValue": 55.0,
    "source": "HISTORICAL_AVERAGE" // 建议来源：历史平均值
  },
  "msg": "success"
}
```

## 3.5 合规检查API

```
# 产品合规性检查
POST /api/v1/erp/compliance/check-product
Request:
{
  "productId": 1001,
  "countryCode": "DE"
}
Response:
{
  "code": 0,
  "data": {
    "compliant": true,
    "issues": [],
    "checks": [
      {
        "type": "CERTIFICATION",
        "name": "CE认证",
        "passed": true,
        "details": "有效期至2025-01-14"
      },
      {
        "type": "CUSTOMS",
        "name": "海关申报信息",
        "passed": true,
        "details": "已配置完整"
      },
      {
        "type": "RESTRICTION",
        "name": "进口限制",
        "passed": true,
        "details": "无限制"
      },
      {
        "type": "TAX",
        "name": "税务信息",
        "passed": true,
        "details": "已配置"
      }
    ]
  },
  "msg": "success"
}

# 批量检查产品合规性
POST /api/v1/erp/compliance/batch-check
Request:
{
  "productIds": [1001, 1002, 1003],
  "countryCode": "DE"
}
Response:
{
  "code": 0,
  "data": {
    "compliant": 2,            // 合规产品数量
    "nonCompliant": 1,         // 不合规产品数量
   