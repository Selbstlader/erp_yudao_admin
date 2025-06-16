-- 创建文件与Dify知识库关联表
CREATE TABLE IF NOT EXISTS `infra_file_dify` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `file_id` bigint NOT NULL COMMENT '文件ID',
  `dify_document_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'Dify文档ID',
  `sync_status` int NOT NULL DEFAULT '0' COMMENT '同步状态（0-未同步，1-同步中，2-已同步，3-同步失败）',
  `error_message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误信息',
  `error_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '错误码',
  `dataset_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '知识库ID',
  `batch_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '批处理ID',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `next_retry_time` datetime DEFAULT NULL COMMENT '下次重试时间',
  `sync_time` datetime DEFAULT NULL COMMENT '上次同步时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_file_id` (`file_id`),
  KEY `idx_dify_document_id` (`dify_document_id`),
  KEY `idx_sync_status` (`sync_status`),
  KEY `idx_dataset_id` (`dataset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件与Dify知识库关联表'; 