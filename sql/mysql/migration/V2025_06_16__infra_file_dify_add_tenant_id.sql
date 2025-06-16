-- 修复 infra_file_dify 表缺少 tenant_id 列的问题
-- 在多租户模式下，tenant_id 列是必须的，需要添加并默认为0

-- 为 infra_file_dify 表添加 tenant_id 列
ALTER TABLE `infra_file_dify` ADD COLUMN `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号';

-- 添加索引，提高查询性能
ALTER TABLE `infra_file_dify` ADD INDEX `idx_tenant_id` (`tenant_id`); 