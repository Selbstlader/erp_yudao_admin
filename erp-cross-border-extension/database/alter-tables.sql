-- Add missing website column to erp_logistics_provider table
ALTER TABLE erp_logistics_provider 
ADD COLUMN website VARCHAR(255) COMMENT '官网地址' AFTER name; 