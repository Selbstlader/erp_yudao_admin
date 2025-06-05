# 国际物流与仓储支持API

## 2.1 国际仓库管理API

### 2.1.1 国际仓库信息管理

```
# 创建国际仓库信息
POST /api/v1/erp/warehouse/international/create
Request:
{
  "warehouseId": 1001,         // 关联基础仓库ID
  "countryCode": "US",         // 国家代码
  "region": "California",      // 地区/州/省
  "city": "Los Angeles",       // 城市
  "postalCode": "90001",       // 邮政编码
  "address": "123 Main St",    // 详细地址
  "contactName": "John Doe",   // 联系人
  "contactPhone": "1234567890",// 联系电话
  "contactEmail": "john@example.com", // 联系邮箱
  "customsCode": "US12345",    // 海关编码
  "isBonded": true,            // 是否保税仓
  "vatNumber": "VAT123456"     // 增值税号
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新国际仓库信息
PUT /api/v1/erp/warehouse/international/update
Request: 同创建，但需要增加id字段

# 删除国际仓库信息
DELETE /api/v1/erp/warehouse/international/delete?id=1

# 获取国际仓库详情
GET /api/v1/erp/warehouse/international/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "warehouseId": 1001,
    "warehouseName": "US Main Warehouse",
    "countryCode": "US",
    "countryName": "United States",
    "region": "California",
    "city": "Los Angeles",
    "postalCode": "90001",
    "address": "123 Main St",
    "contactName": "John Doe",
    "contactPhone": "1234567890",
    "contactEmail": "john@example.com",
    "customsCode": "US12345",
    "isBonded": true,
    "vatNumber": "VAT123456",
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取国际仓库列表
GET /api/v1/erp/warehouse/international/list?countryCode=US&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "warehouseId": 1001,
        "warehouseName": "US Main Warehouse",
        "countryCode": "US",
        "countryName": "United States",
        "region": "California",
        "city": "Los Angeles",
        // 其他字段
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 获取指定国家的仓库列表
GET /api/v1/erp/warehouse/international/by-country?countryCode=US
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "warehouseId": 1001,
      "warehouseName": "US Main Warehouse",
      // 其他字段
    },
    // 更多记录
  ],
  "msg": "success"
}
```

## 2.2 物流服务商管理API

```
# 创建物流服务商
POST /api/v1/erp/logistics/provider/create
Request:
{
  "code": "DHL",               // 服务商代码
  "name": "DHL Express",       // 服务商名称
  "website": "https://www.dhl.com", // 官网地址
  "apiConfig": {               // API配置(JSON格式)
    "apiKey": "dhl_api_key",
    "apiSecret": "dhl_api_secret",
    "apiUrl": "https://api.dhl.com"
  },
  "status": 1                  // 状态
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新物流服务商
PUT /api/v1/erp/logistics/provider/update
Request: 同创建，但需要增加id字段

# 删除物流服务商
DELETE /api/v1/erp/logistics/provider/delete?id=1

# 获取物流服务商详情
GET /api/v1/erp/logistics/provider/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "code": "DHL",
    "name": "DHL Express",
    "website": "https://www.dhl.com",
    "apiConfig": {
      "apiKey": "dhl_api_key",
      "apiSecret": "******", // 敏感信息脱敏
      "apiUrl": "https://api.dhl.com"
    },
    "status": 1,
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取物流服务商列表
GET /api/v1/erp/logistics/provider/list?status=1&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "code": "DHL",
        "name": "DHL Express",
        "website": "https://www.dhl.com",
        "status": 1
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 测试物流服务商API连接
POST /api/v1/erp/logistics/provider/test-connection
Request:
{
  "id": 1
}
Response:
{
  "code": 0,
  "data": {
    "success": true,
    "message": "Connection successful"
  },
  "msg": "success"
}
```

## 2.3 物流方式管理API

```
# 创建物流方式
POST /api/v1/erp/logistics/method/create
Request:
{
  "code": "DHL_EXPRESS",       // 物流方式代码
  "name": "DHL Express",       // 物流方式名称
  "providerId": 1,             // 物流服务商ID
  "shippingType": 2,           // 运输类型(1-海运,2-空运,3-陆运,4-铁路,5-快递)
  "estimatedDays": 3,          // 预计送达天数
  "trackingUrl": "https://www.dhl.com/track?num={trackingNumber}", // 物流追踪URL模板
  "status": 1                  // 状态
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新物流方式
PUT /api/v1/erp/logistics/method/update
Request: 同创建，但需要增加id字段

# 删除物流方式
DELETE /api/v1/erp/logistics/method/delete?id=1

# 获取物流方式详情
GET /api/v1/erp/logistics/method/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "code": "DHL_EXPRESS",
    "name": "DHL Express",
    "providerId": 1,
    "providerName": "DHL",
    "shippingType": 2,
    "shippingTypeName": "空运",
    "estimatedDays": 3,
    "trackingUrl": "https://www.dhl.com/track?num={trackingNumber}",
    "status": 1,
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取物流方式列表
GET /api/v1/erp/logistics/method/list?providerId=1&status=1&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "code": "DHL_EXPRESS",
        "name": "DHL Express",
        "providerId": 1,
        "providerName": "DHL",
        "shippingType": 2,
        "shippingTypeName": "空运",
        "estimatedDays": 3,
        "status": 1
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 获取可用的物流方式
GET /api/v1/erp/logistics/method/available?sourceCountry=CN&destCountry=US&weight=2.5
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "code": "DHL_EXPRESS",
      "name": "DHL Express",
      "providerId": 1,
      "providerName": "DHL",
      "shippingType": 2,
      "estimatedDays": 3,
      "estimatedFee": 45.5,
      "currencyCode": "USD"
    },
    // 更多记录
  ],
  "msg": "success"
}
```

## 2.4 物流费用规则管理API

```
# 创建物流费用规则
POST /api/v1/erp/logistics/fee-rule/create
Request:
{
  "logisticsMethodId": 1,      // 物流方式ID
  "sourceCountry": "CN",       // 起始国家代码
  "destCountry": "US",         // 目的国家代码
  "weightStart": 0.0,          // 重量起始值(kg)
  "weightEnd": 5.0,            // 重量结束值(kg)
  "firstWeightFee": 20.0,      // 首重费用
  "additionalWeightFee": 5.0,  // 续重费用(每kg)
  "currencyId": 1,             // 货币ID
  "effectiveDate": "2023-07-01", // 生效日期
  "expiryDate": "2023-12-31"   // 失效日期
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新物流费用规则
PUT /api/v1/erp/logistics/fee-rule/update
Request: 同创建，但需要增加id字段

# 删除物流费用规则
DELETE /api/v1/erp/logistics/fee-rule/delete?id=1

# 获取物流费用规则详情
GET /api/v1/erp/logistics/fee-rule/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "logisticsMethodId": 1,
    "logisticsMethodName": "DHL Express",
    "sourceCountry": "CN",
    "sourceCountryName": "China",
    "destCountry": "US",
    "destCountryName": "United States",
    "weightStart": 0.0,
    "weightEnd": 5.0,
    "firstWeightFee": 20.0,
    "additionalWeightFee": 5.0,
    "currencyId": 1,
    "currencyCode": "USD",
    "effectiveDate": "2023-07-01",
    "expiryDate": "2023-12-31",
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取物流费用规则列表
GET /api/v1/erp/logistics/fee-rule/list?logisticsMethodId=1&sourceCountry=CN&destCountry=US&pageNo=1&pageSize=10
Response:
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "logisticsMethodId": 1,
        "logisticsMethodName": "DHL Express",
        "sourceCountry": "CN",
        "sourceCountryName": "China",
        "destCountry": "US",
        "destCountryName": "United States",
        "weightStart": 0.0,
        "weightEnd": 5.0,
        "firstWeightFee": 20.0,
        "additionalWeightFee": 5.0,
        "currencyId": 1,
        "currencyCode": "USD",
        "effectiveDate": "2023-07-01",
        "expiryDate": "2023-12-31"
      },
      // 更多记录
    ],
    "total": 5,
    "pageNo": 1,
    "pageSize": 10
  },
  "msg": "success"
}

# 计算物流费用
POST /api/v1/erp/logistics/fee-rule/calculate
Request:
{
  "logisticsMethodId": 1,
  "sourceCountry": "CN",
  "destCountry": "US",
  "weight": 2.5,
  "targetCurrencyId": 2        // 目标货币ID，可选，默认使用规则中的货币
}
Response:
{
  "code": 0,
  "data": {
    "fee": 32.5,               // 计算得出的费用
    "currencyId": 2,
    "currencyCode": "EUR",
    "details": {
      "firstWeightFee": 20.0,
      "additionalWeight": 2.5,
      "additionalWeightFee": 12.5,
      "originalCurrencyCode": "USD",
      "exchangeRate": 0.85
    }
  },
  "msg": "success"
}

# 批量导入物流费用规则
POST /api/v1/erp/logistics/fee-rule/batch-import
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
        "message": "Invalid weight range"
      },
      {
        "row": 10,
        "message": "Missing required field"
      }
    ]
  },
  "msg": "success"
}

# 导出物流费用规则
GET /api/v1/erp/logistics/fee-rule/export?logisticsMethodId=1
Response: 返回Excel文件下载
```

## 2.5 跨境物流追踪API

```
# 创建物流追踪记录
POST /api/v1/erp/logistics/tracking/create
Request:
{
  "orderId": 1001,             // 关联订单ID
  "logisticsMethodId": 1,      // 物流方式ID
  "trackingNumber": "DHL1234567890", // 物流追踪号
  "sourceCountry": "CN",       // 起始国家代码
  "destCountry": "US",         // 目的国家代码
  "status": 2,                 // 物流状态(1-待发货,2-已发货,3-运输中,4-清关中,5-已送达,6-异常)
  "estimatedDeliveryTime": "2023-07-15 14:00:00" // 预计送达时间
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新物流追踪记录
PUT /api/v1/erp/logistics/tracking/update
Request: 同创建，但需要增加id字段

# 删除物流追踪记录
DELETE /api/v1/erp/logistics/tracking/delete?id=1

# 获取物流追踪详情
GET /api/v1/erp/logistics/tracking/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "orderId": 1001,
    "orderNo": "ORD20230710001",
    "logisticsMethodId": 1,
    "logisticsMethodName": "DHL Express",
    "trackingNumber": "DHL1234567890",
    "sourceCountry": "CN",
    "sourceCountryName": "China",
    "destCountry": "US",
    "destCountryName": "United States",
    "status": 3,
    "statusName": "运输中",
    "lastTrackTime": "2023-07-12 10:30:00",
    "estimatedDeliveryTime": "2023-07-15 14:00:00",
    "trackingDetails": [
      {
        "time": "20