# 跨境电商ERP系统API设计

## 文档信息
- **文档版本**：1.0.0
- **创建日期**：2023-07-10
- **最后更新**：2023-07-15
- **作者**：API设计团队

## 目录

1. [多语言/多币种支持API](modules/multilingual-currency-api.md)
2. [国际物流与仓储支持API](modules/logistics-warehouse-api.md)
3. [合规与认证管理API](modules/compliance-certification-api.md)
4. [税务管理API](modules/taxation-api.md)
5. [通用API](modules/common-api.md)

## API设计规范

### 请求格式

所有API请求应遵循以下格式规范：

- **GET请求**：参数通过URL查询字符串传递
- **POST/PUT请求**：参数通过JSON格式的请求体传递
- **DELETE请求**：参数通过URL查询字符串传递

### 响应格式

所有API响应均采用统一的JSON格式：

```json
{
  "code": 0,           // 响应码，0表示成功，非0表示错误
  "data": {},          // 响应数据，可能是对象、数组或基本类型
  "msg": "success"     // 响应消息，成功为"success"，失败时为错误描述
}
```

### 错误码

| 错误码 | 描述 |
|-------|------|
| 0 | 成功 |
| 1000 | 系统错误 |
| 1001 | 参数错误 |
| 1002 | 权限不足 |
| 1003 | 资源不存在 |
| 1004 | 资源已存在 |
| 1005 | 操作失败 |

### 分页参数

分页查询统一使用以下参数：

- **pageNo**：页码，从1开始
- **pageSize**：每页记录数

分页响应格式：

```json
{
  "code": 0,
  "data": {
    "list": [],        // 数据列表
    "total": 100,      // 总记录数
    "pageNo": 1,       // 当前页码
    "pageSize": 10     // 每页记录数
  },
  "msg": "success"
}
```

### 认证与授权

所有API请求需要在HTTP头部包含以下认证信息：

- **Authorization**：Bearer {token}

### API版本控制

API版本通过URL路径进行控制，格式为：`/api/v{version}/...`

当前文档描述的是v1版本的API，完整路径前缀为：`/api/v1/erp/`
