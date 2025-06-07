-- 清理冲突数据

-- 1. 查看物流服务商中的重复code数据
-- SELECT code, COUNT(*) FROM erp_logistics_provider WHERE deleted = 0 GROUP BY code HAVING COUNT(*) > 1;

-- 2. 删除物流服务商重复数据
DELETE FROM erp_logistics_provider WHERE deleted = 0;

-- 3. 删除物流方式数据
DELETE FROM erp_logistics_method WHERE deleted = 0;

-- 4. 删除国际仓库数据
DELETE FROM erp_warehouse_international WHERE deleted = 0; 