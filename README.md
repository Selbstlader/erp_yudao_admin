# ERP 项目架构指南

## 项目概述
本项目是一个基于 Spring Boot 3 和 Vue 3 + TypeScript 的 ERP 系统。后端使用 Spring Boot 3 开发，并通过 Docker 容器部署，前端使用 Vue 3 + TypeScript 开发。

## 项目结构

### 后端结构
- @pom.xml: 项目根 Maven 配置文件，定义了整个项目的依赖管理
- @yudao-dependencies/: 项目依赖版本管理模块
- @yudao-framework/: 框架核心模块，包含各种 Spring Boot Starter
- @yudao-server/: 应用服务器模块，包含主启动类和配置
- @yudao-module-system/: 系统管理模块
- @yudao-module-infra/: 基础设施模块
- @yudao-module-erp/: ERP 业务模块

### 前端结构
- @yudao-ui/yudao-ui-admin-vue3/: Vue 3 + TypeScript 前端项目
### 其他重要目录
- @sql/: 数据库脚本
- @script/: 部署和维护脚本

## 主要入口点
- 后端入口: @yudao-server/src/main/java/cn/iocoder/yudao/server/YudaoServerApplication.java
- 前端入口: @yudao-ui/yudao-ui-admin-vue3/src/main.ts

## Docker 部署
项目使用 Docker 容器化部署，主要配置文件为 @yudao-server/Dockerfile
