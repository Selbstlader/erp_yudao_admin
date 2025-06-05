# 多语言/多币种支持API

## 1.1 多语言管理API

### 1.1.1 产品多语言信息管理

```
# 创建产品多语言信息
POST /api/v1/erp/product/i18n/create
Request:
{
  "productId": 1001,           // 产品ID
  "locale": "en_US",           // 语言代码
  "name": "Product Name",      // 本地化产品名称
  "description": "Description",// 本地化产品描述
  "keywords": "key1,key2",     // SEO关键词
  "metaTitle": "Meta Title",   // SEO标题
  "metaDescription": "Meta",   // SEO描述
  "standard": "Standard",      // 本地化产品规格
  "remark": "Remark"           // 本地化备注
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新产品多语言信息
PUT /api/v1/erp/product/i18n/update
Request: 
{
  "id": 1,                     // 记录ID
  "productId": 1001,           // 产品ID
  "locale": "en_US",           // 语言代码
  "name": "Updated Name",      // 本地化产品名称
  "description": "Updated",    // 本地化产品描述
  "keywords": "key1,key2,key3",// SEO关键词
  "metaTitle": "New Title",    // SEO标题
  "metaDescription": "New Meta",// SEO描述
  "standard": "New Standard",  // 本地化产品规格
  "remark": "New Remark"       // 本地化备注
}

# 获取指定产品的多语言信息
GET /api/v1/erp/product/i18n/get?productId=1001&locale=en_US
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "productId": 1001,
    "locale": "en_US",
    "name": "Product Name",
    "description": "Description",
    "keywords": "key1,key2",
    "metaTitle": "Meta Title",
    "metaDescription": "Meta",
    "standard": "Standard",
    "remark": "Remark",
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取产品的所有语言版本
GET /api/v1/erp/product/i18n/list?productId=1001
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "productId": 1001,
      "locale": "en_US",
      "name": "Product Name",
      // 其他字段
    },
    {
      "id": 2,
      "productId": 1001,
      "locale": "zh_CN",
      "name": "产品名称",
      // 其他字段
    }
  ],
  "msg": "success"
}

# 批量保存产品多语言信息
POST /api/v1/erp/product/i18n/batch-save
Request:
{
  "productId": 1001,
  "items": [
    {
      "locale": "en_US",
      "name": "Product Name",
      // 其他字段
    },
    {
      "locale": "zh_CN",
      "name": "产品名称",
      // 其他字段
    }
  ]
}
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}

# 删除产品多语言信息
DELETE /api/v1/erp/product/i18n/delete?id=1
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}
```

### 1.1.2 支持语言管理

```
# 创建支持语言
POST /api/v1/erp/system/locale/create
Request:
{
  "locale": "fr_FR",           // 语言代码
  "name": "French",            // 语言名称
  "nativeName": "Français",    // 本地语言名称
  "flagIcon": "/flags/fr.png", // 国旗图标路径
  "isDefault": false,          // 是否为默认语言
  "status": 1,                 // 状态
  "sort": 3                    // 排序
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新支持语言
PUT /api/v1/erp/system/locale/update
Request: 同创建，但需要增加id字段

# 删除支持语言
DELETE /api/v1/erp/system/locale/delete?id=1

# 获取支持语言详情
GET /api/v1/erp/system/locale/get?id=1

# 获取系统支持的语言列表
GET /api/v1/erp/system/locale/list?status=1

# 设置系统默认语言
PUT /api/v1/erp/system/locale/set-default
Request:
{
  "id": 1
}
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}
```

## 1.2 货币与汇率API

### 1.2.1 货币管理

```
# 创建货币
POST /api/v1/erp/currency/create
Request:
{
  "code": "USD",               // 货币代码
  "name": "US Dollar",         // 货币名称
  "symbol": "$",               // 货币符号
  "exchangeRate": 1.0,         // 兑换率
  "isBase": true,              // 是否为基准货币
  "decimalPlaces": 2,          // 小数位数
  "roundingMode": 0,           // 舍入模式
  "status": 1                  // 状态
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新货币
PUT /api/v1/erp/currency/update
Request: 同创建，但需要增加id字段

# 删除货币
DELETE /api/v1/erp/currency/delete?id=1

# 获取货币详情
GET /api/v1/erp/currency/get?id=1

# 获取货币列表
GET /api/v1/erp/currency/list?status=1

# 更新汇率
PUT /api/v1/erp/currency/update-exchange-rate
Request:
{
  "id": 1,
  "exchangeRate": 6.8
}
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}

# 同步所有汇率（从外部API）
POST /api/v1/erp/currency/sync-exchange-rates
Response:
{
  "code": 0,
  "data": {
    "success": 10,             // 成功更新数量
    "failed": 0                // 失败数量
  },
  "msg": "success"
}

# 设置基准货币
PUT /api/v1/erp/currency/set-base
Request:
{
  "id": 1
}
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}
```

### 1.2.2 汇率历史API

```
# 创建汇率历史记录
POST /api/v1/erp/exchange-rate/create
Request:
{
  "currencyId": 1,             // 货币ID
  "exchangeRate": 6.8,         // 兑换率
  "effectiveDate": "2023-07-01"// 生效日期
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 获取汇率历史
GET /api/v1/erp/exchange-rate/history?currencyId=1&startDate=2023-01-01&endDate=2023-07-01
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "currencyId": 1,
      "currencyCode": "CNY",
      "exchangeRate": 6.8,
      "effectiveDate": "2023-01-01"
    },
    // 更多记录
  ],
  "msg": "success"
}

# 获取汇率趋势分析
GET /api/v1/erp/exchange-rate/trend-analysis?currencyId=1&period=MONTH
Response:
{
  "code": 0,
  "data": {
    "currency": {
      "id": 1,
      "code": "CNY",
      "name": "Chinese Yuan"
    },
    "baseCurrency": {
      "id": 2,
      "code": "USD",
      "name": "US Dollar"
    },
    "period": "MONTH",
    "data": [
      {
        "date": "2023-01",
        "rate": 6.8,
        "change": 0
      },
      {
        "date": "2023-02",
        "rate": 6.85,
        "change": 0.05
      }
      // 更多数据点
    ]
  },
  "msg": "success"
}
```

## 1.3 市场管理API

```
# 创建市场
POST /api/v1/erp/market/create
Request:
{
  "code": "US",                // 市场代码
  "name": "United States",     // 市场名称
  "countries": ["US"],         // 覆盖国家
  "defaultCurrencyId": 1,      // 默认货币ID
  "defaultLocale": "en_US",    // 默认语言
  "status": 1                  // 状态
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新市场
PUT /api/v1/erp/market/update
Request: 同创建，但需要增加id字段

# 删除市场
DELETE /api/v1/erp/market/delete?id=1

# 获取市场详情
GET /api/v1/erp/market/get?id=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "code": "US",
    "name": "United States",
    "countries": ["US"],
    "defaultCurrency": {
      "id": 1,
      "code": "USD",
      "name": "US Dollar"
    },
    "defaultLocale": "en_US",
    "status": 1,
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取市场列表
GET /api/v1/erp/market/list?status=1
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "code": "US",
      "name": "United States",
      // 其他字段
    },
    // 更多记录
  ],
  "msg": "success"
}

# 获取国家所属的市场
GET /api/v1/erp/market/by-country?countryCode=US
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "code": "US",
      "name": "United States",
      // 其他字段
    },
    // 可能有多个市场覆盖同一国家
  ],
  "msg": "success"
}
```

## 1.4 产品价格管理API

```
# 创建产品价格
POST /api/v1/erp/product-price/create
Request:
{
  "productId": 1001,           // 产品ID
  "currencyId": 1,             // 货币ID
  "marketId": 1,               // 市场ID
  "salePrice": 99.99,          // 销售价格
  "minPrice": 89.99            // 最低销售价格
}
Response:
{
  "code": 0,
  "data": 1,                   // 新创建的记录ID
  "msg": "success"
}

# 更新产品价格
PUT /api/v1/erp/product-price/update
Request: 同创建，但需要增加id字段

# 删除产品价格
DELETE /api/v1/erp/product-price/delete?id=1

# 获取产品价格
GET /api/v1/erp/product-price/get?productId=1001&currencyId=1&marketId=1
Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "productId": 1001,
    "currencyId": 1,
    "currencyCode": "USD",
    "marketId": 1,
    "marketName": "United States",
    "salePrice": 99.99,
    "minPrice": 89.99,
    "createTime": "2023-07-10 10:00:00",
    "updateTime": "2023-07-10 10:00:00"
  },
  "msg": "success"
}

# 获取产品所有价格
GET /api/v1/erp/product-price/list?productId=1001
Response:
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "productId": 1001,
      "currencyId": 1,
      "currencyCode": "USD",
      "marketId": 1,
      "marketName": "United States",
      "salePrice": 99.99,
      "minPrice": 89.99
    },
    // 更多记录
  ],
  "msg": "success"
}

# 批量保存产品价格
POST /api/v1/erp/product-price/batch-save
Request:
{
  "productId": 1001,
  "items": [
    {
      "currencyId": 1,
      "marketId": 1,
      "salePrice": 99.99,
      "minPrice": 89.99
    },
    {
      "currencyId": 2,
      "marketId": 2,
      "salePrice": 89.99,
      "minPrice": 79.99
    }
  ]
}
Response:
{
  "code": 0,
  "data": true,
  "msg": "success"
}

#