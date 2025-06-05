# 跨境电商ERP系统功能扩展

## 项目概述

本项目旨在为现有ERP系统增加跨境电商所需的核心功能，包括多语言/多币种支持、国际物流与仓储支持、合规与认证管理以及税务管理四个核心功能模块。通过这些功能扩展，使ERP系统能够更好地支持跨境电商业务，提高运营效率，降低合规风险。

## 功能模块

### 1. 多语言/多币种支持

- 产品多语言信息管理
- 货币与汇率管理
- 市场管理
- 产品价格管理（多币种）

### 2. 国际物流与仓储支持

- 国际仓库管理
- 物流服务商管理
- 物流方式管理
- 物流费用规则管理
- 物流追踪管理

### 3. 合规与认证管理

- 认证类型管理
- 产品认证管理
- HS编码管理
- 产品海关信息管理
- 禁限商品检查

### 4. 税务管理

- 税种管理
- 国家税率管理
- 产品税分类管理
- 产品税务信息管理
- 增值税注册管理
- 税费计算
- 税务报表生成

## 文档结构

```
erp-cross-border-extension/
├── api-design/                 # API设计文档
│   └── api-design.md           # 详细API接口设计
├── database/                   # 数据库设计
│   └── database-design.md      # 数据库表结构设计
├── docs/                       # 项目文档
│   ├── design/                 # 设计文档
│   │   └── architecture-design.md  # 技术架构设计
│   ├── implementation/         # 实施文档
│   │   └── implementation-plan.md  # 实施计划
│   ├── requirements/           # 需求文档
│   │   └── requirements.md     # 详细需求说明
│   └── images/                 # 架构图等图片资源
├── src/                        # 源代码
│   └── ...                     # 待实现的源代码
└── README.md                   # 项目说明
```

## 技术栈

- **后端**：Spring Boot, Spring Cloud
- **前端**：Vue.js, Element UI
- **数据库**：MySQL
- **缓存**：Redis
- **消息队列**：RabbitMQ
- **文件存储**：MinIO
- **搜索引擎**：Elasticsearch
- **容器化**：Docker, Kubernetes
- **监控**：Prometheus, Grafana
- **日志**：ELK Stack

## 实施指南

1. 查阅 `docs/requirements/requirements.md` 了解详细需求
2. 查阅 `docs/design/architecture-design.md` 了解技术架构设计
3. 查阅 `database/database-design.md` 了解数据库设计
4. 查阅 `api-design/api-design.md` 了解API接口设计
5. 查阅 `docs/implementation/implementation-plan.md` 了解实施计划
6. 按照实施计划进行开发和部署

## 开发环境搭建

### 前提条件

- JDK 11+
- Maven 3.6+
- Node.js 14+
- Docker 20+
- Kubernetes 1.20+
- MySQL 8.0+
- Redis 6.0+

### 本地开发环境搭建步骤

1. 克隆代码仓库
   ```bash
   git clone [repository-url]
   cd erp-cross-border-extension
   ```

2. 后端服务启动
   ```bash
   cd src/backend
   mvn clean install
   mvn spring-boot:run
   ```

3. 前端服务启动
   ```bash
   cd src/frontend
   npm install
   npm run serve
   ```

4. 访问本地开发环境
   - 后端API: http://localhost:8080
   - 前端界面: http://localhost:8081

## 贡献指南

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件

## 联系方式

项目负责人 - [负责人邮箱]

项目链接: [项目仓库URL] 