-- 测试数据：物流服务商、物流方式、国际仓库
-- 注意: 执行前先确认表已创建，且已执行cleanup.sql清理冲突数据

-- 1. 物流服务商测试数据（确保code是唯一的，已移除不存在的api_type字段）
INSERT INTO erp_logistics_provider 
(code, name, website, api_config, status, sort, remark, creator, create_time, updater, update_time, deleted) VALUES
('DHL001', 'DHL国际快递', 'https://www.dhl.com', '{"apiKey":"test_dhl_key","endpoint":"https://api.dhl.com/v1","apiType":"REST"}', 1, 10, 'DHL国际物流服务商', 'admin', NOW(), 'admin', NOW(), 0),
('UPS001', 'UPS国际快递', 'https://www.ups.com', '{"apiKey":"test_ups_key","endpoint":"https://api.ups.com/v1","apiType":"REST"}', 1, 20, 'UPS国际物流服务商', 'admin', NOW(), 'admin', NOW(), 0),
('FEDEX001', 'FedEx国际快递', 'https://www.fedex.com', '{"apiKey":"test_fedex_key","endpoint":"https://api.fedex.com/v1","apiType":"REST"}', 1, 30, 'FedEx国际物流服务商', 'admin', NOW(), 'admin', NOW(), 0),
('EMS001', 'EMS国际快递', 'https://www.ems.com.cn', '{"username":"test_ems_user","password":"test_ems_pass","endpoint":"https://api.ems.com/v1","apiType":"SOAP"}', 1, 40, 'EMS国际物流服务商', 'admin', NOW(), 'admin', NOW(), 0);

-- 2. 物流方式测试数据（使用子查询获取物流服务商ID）
INSERT INTO erp_logistics_method 
(code, name, provider_id, estimated_days, status, sort, remark, creator, create_time, updater, update_time, deleted) VALUES
('DHL-EXPRESS', 'DHL快递', (SELECT id FROM erp_logistics_provider WHERE code = 'DHL001' AND deleted = 0), 5, 1, 10, 'DHL快递服务', 'admin', NOW(), 'admin', NOW(), 0),
('DHL-ECON', 'DHL经济', (SELECT id FROM erp_logistics_provider WHERE code = 'DHL001' AND deleted = 0), 10, 1, 20, 'DHL经济服务', 'admin', NOW(), 'admin', NOW(), 0),
('UPS-EXPR', 'UPS特快', (SELECT id FROM erp_logistics_provider WHERE code = 'UPS001' AND deleted = 0), 4, 1, 10, 'UPS特快服务', 'admin', NOW(), 'admin', NOW(), 0),
('UPS-STAN', 'UPS标准', (SELECT id FROM erp_logistics_provider WHERE code = 'UPS001' AND deleted = 0), 8, 1, 20, 'UPS标准服务', 'admin', NOW(), 'admin', NOW(), 0),
('FEDEX-PRI', 'FedEx优先', (SELECT id FROM erp_logistics_provider WHERE code = 'FEDEX001' AND deleted = 0), 3, 1, 10, 'FedEx优先服务', 'admin', NOW(), 'admin', NOW(), 0),
('FEDEX-ECO', 'FedEx经济', (SELECT id FROM erp_logistics_provider WHERE code = 'FEDEX001' AND deleted = 0), 9, 1, 20, 'FedEx经济服务', 'admin', NOW(), 'admin', NOW(), 0),
('EMS-INT', 'EMS国际', (SELECT id FROM erp_logistics_provider WHERE code = 'EMS001' AND deleted = 0), 7, 1, 10, 'EMS国际服务', 'admin', NOW(), 'admin', NOW(), 0);

-- 3. 查询现有仓库ID（后续用于创建国际仓库记录）
-- 注意：执行以下SQL前，需要确保系统中已有基础仓库数据
-- SELECT id, name FROM erp_warehouse WHERE deleted = 0 LIMIT 10;

-- 4. 国际仓库测试数据（假设已有基础仓库，ID为1、2、3、4）
-- 根据实际字段修改，移除不存在的字段
INSERT INTO erp_warehouse_international
(warehouse_id, country_code, region, city, postal_code, address, contact_name, contact_phone, contact_email, customs_code, is_bonded, vat_number, status, remark, creator, create_time, updater, update_time, deleted) VALUES
(1, 'US', '加利福尼亚州', '洛杉矶', '90001', '123 Main St, Los Angeles, CA 90001', 'John Smith', '+1-213-555-1234', 'john.smith@example.com', 'US-LA-001', 0, 'US12345678', 1, '美国洛杉矶仓库', 'admin', NOW(), 'admin', NOW(), 0),
(2, 'UK', '大伦敦', '伦敦', 'E14 5AB', '10 Downing Street, London, E14 5AB', 'James Brown', '+44-20-7123-4567', 'james.brown@example.com', 'UK-LN-002', 1, 'GB123456789', 1, '英国伦敦仓库', 'admin', NOW(), 'admin', NOW(), 0),
(3, 'DE', '巴伐利亚', '慕尼黑', '80331', 'Marienplatz 1, 80331 Munich', 'Hans Mueller', '+49-89-1234-5678', 'hans.mueller@example.com', 'DE-MU-003', 0, 'DE987654321', 1, '德国慕尼黑仓库', 'admin', NOW(), 'admin', NOW(), 0),
(4, 'JP', '东京都', '东京', '100-0001', '100-0001 Tokyo Chiyoda-ku Chiyoda 1-1', 'Takeshi Sato', '+81-3-1234-5678', 'takeshi.sato@example.com', 'JP-TK-004', 1, 'JP12345678901', 1, '日本东京仓库', 'admin', NOW(), 'admin', NOW(), 0);

-- 注意：如果执行失败，请检查以下事项：
-- 1. 确保表已创建
-- 2. 确认字段名称与实际表结构一致
-- 3. 对于国际仓库测试数据，确保引用的warehouse_id实际存在 