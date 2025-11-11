-- 清空表数据（保留表结构）
-- 由于存在外键约束，需要先禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 按顺序删除数据（先删除子表，再删除父表）
DELETE FROM `agriculture_resource_usage`;
DELETE FROM `agriculture_resource_inventory`;
DELETE FROM `agriculture_resource`;

-- 重置自增ID
ALTER TABLE `agriculture_resource` AUTO_INCREMENT = 1;
ALTER TABLE `agriculture_resource_inventory` AUTO_INCREMENT = 1;
ALTER TABLE `agriculture_resource_usage` AUTO_INCREMENT = 1;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 农资资源表测试数据
-- 使用真实用户ID：9=物资管理部主管
INSERT INTO `agriculture_resource` (`resource_code`, `resource_name`, `resource_type`, `resource_image`, `measure_unit`, `remark`, `order_num`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES
('RES001', '复合肥料', '0', NULL, 'kg', '适用于多种作物的复合肥料', 1, '9', NOW(), '9', NOW(), '0'),
('RES002', '有机肥料', '0', NULL, 'kg', '有机质含量高的有机肥料', 2, '9', NOW(), '9', NOW(), '0'),
('RES003', '杀虫剂', '0', NULL, 'L', '高效低毒杀虫剂', 3, '9', NOW(), '9', NOW(), '0'),
('RES004', '除草剂', '0', NULL, 'L', '选择性除草剂', 4, '9', NOW(), '9', NOW(), '0'),
('RES005', '种子', '0', NULL, 'kg', '优质蔬菜种子', 5, '9', NOW(), '9', NOW(), '0'),
('RES006', '地膜', '0', NULL, 'm²', '黑色地膜，保温保湿', 6, '9', NOW(), '9', NOW(), '0'),
('RES007', '滴灌管', '0', NULL, 'm', 'PE材质滴灌管', 7, '9', NOW(), '9', NOW(), '0'),
('RES008', '育苗盘', '0', NULL, '个', '72孔育苗盘', 8, '9', NOW(), '9', NOW(), '0'),
('MACH001', '旋耕机', '1', NULL, '台', '小型旋耕机，适用于温室作业', 1, '9', NOW(), '9', NOW(), '0'),
('MACH002', '播种机', '1', NULL, '台', '精量播种机', 2, '9', NOW(), '9', NOW(), '0'),
('MACH003', '喷雾机', '1', NULL, '台', '电动喷雾机，用于喷洒农药', 3, '9', NOW(), '9', NOW(), '0'),
('MACH004', '收割机', '1', NULL, '台', '小型收割机', 4, '9', NOW(), '9', NOW(), '0'),
('MACH005', '拖拉机', '1', NULL, '台', '小型农用拖拉机', 5, '9', NOW(), '9', NOW(), '0');

-- 农资库存表测试数据
-- 使用真实用户ID：9=物资管理部主管
INSERT INTO `agriculture_resource_inventory` (`resource_id`, `current_stock`, `min_stock`, `max_stock`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES
(1, 5000.00, 500.00, 10000.00, '复合肥料库存', '9', NOW(), '9', NOW(), '0'),
(2, 3000.00, 300.00, 6000.00, '有机肥料库存', '9', NOW(), '9', NOW(), '0'),
(3, 200.00, 20.00, 500.00, '杀虫剂库存', '9', NOW(), '9', NOW(), '0'),
(4, 150.00, 15.00, 400.00, '除草剂库存', '9', NOW(), '9', NOW(), '0'),
(5, 1000.00, 100.00, 2000.00, '种子库存', '9', NOW(), '9', NOW(), '0'),
(6, 5000.00, 500.00, 10000.00, '地膜库存', '9', NOW(), '9', NOW(), '0'),
(7, 10000.00, 1000.00, 20000.00, '滴灌管库存', '9', NOW(), '9', NOW(), '0'),
(8, 500.00, 50.00, 1000.00, '育苗盘库存', '9', NOW(), '9', NOW(), '0'),
(9, 2.00, 1.00, 5.00, '旋耕机库存', '9', NOW(), '9', NOW(), '0'),
(10, 1.00, 1.00, 3.00, '播种机库存', '9', NOW(), '9', NOW(), '0'),
(11, 3.00, 1.00, 5.00, '喷雾机库存', '9', NOW(), '9', NOW(), '0'),
(12, 1.00, 1.00, 2.00, '收割机库存', '9', NOW(), '9', NOW(), '0'),
(13, 2.00, 1.00, 3.00, '拖拉机库存', '9', NOW(), '9', NOW(), '0');

-- 农资使用记录表测试数据（动态获取批次和任务ID）
-- 使用子查询获取第一个批次ID
SET @batch_id_1 = (SELECT batch_id FROM agriculture_crop_batch WHERE del_flag = '0' ORDER BY batch_id LIMIT 1);
SET @batch_id_2 = (SELECT batch_id FROM agriculture_crop_batch WHERE del_flag = '0' ORDER BY batch_id LIMIT 1 OFFSET 1);
SET @batch_id_3 = (SELECT batch_id FROM agriculture_crop_batch WHERE del_flag = '0' ORDER BY batch_id LIMIT 1 OFFSET 2);

-- 获取第一个批次的任务ID
SET @task_id_1 = (SELECT task_id FROM agriculture_batch_task WHERE batch_id = @batch_id_1 AND del_flag = '0' ORDER BY task_id LIMIT 1);
SET @task_id_2 = (SELECT task_id FROM agriculture_batch_task WHERE batch_id = @batch_id_1 AND del_flag = '0' ORDER BY task_id LIMIT 1 OFFSET 1);
SET @task_id_3 = (SELECT task_id FROM agriculture_batch_task WHERE batch_id = @batch_id_2 AND del_flag = '0' ORDER BY task_id LIMIT 1);
SET @task_id_4 = (SELECT task_id FROM agriculture_batch_task WHERE batch_id = @batch_id_2 AND del_flag = '0' ORDER BY task_id LIMIT 1 OFFSET 1);
SET @task_id_5 = (SELECT task_id FROM agriculture_batch_task WHERE batch_id = @batch_id_3 AND del_flag = '0' ORDER BY task_id LIMIT 1);

-- 插入使用记录（如果批次和任务存在）
INSERT INTO `agriculture_resource_usage` (`resource_id`, `batch_id`, `task_id`, `usage_quantity`, `measure_unit`, `usage_date`, `usage_type`, `operator`, `remark`, `status`, `order_num`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`)
SELECT * FROM (
    -- 批次1的使用记录
    SELECT 1 AS resource_id, @batch_id_1 AS batch_id, @task_id_1 AS task_id, 100.00 AS usage_quantity, 'kg' AS measure_unit, '2025-01-15 08:00:00' AS usage_date, '1' AS usage_type, '张三' AS operator, CONCAT('批次', @batch_id_1, '任务', @task_id_1, '施肥使用') AS remark, '0' AS status, 1 AS order_num, '1' AS create_by, NOW() AS create_time, '1' AS update_by, NOW() AS update_time, '0' AS del_flag
    WHERE @batch_id_1 IS NOT NULL AND @task_id_1 IS NOT NULL
    UNION ALL
    SELECT 2, @batch_id_1, @task_id_1, 50.00, 'kg', '2025-01-15 08:30:00', '1', '张三', CONCAT('批次', @batch_id_1, '任务', @task_id_1, '有机肥使用'), '0', 2, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_1 IS NOT NULL AND @task_id_1 IS NOT NULL
    UNION ALL
    SELECT 3, @batch_id_1, @task_id_2, 5.00, 'L', '2025-01-20 09:00:00', '1', '李四', CONCAT('批次', @batch_id_1, '任务', @task_id_2, '杀虫使用'), '0', 3, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_1 IS NOT NULL AND @task_id_2 IS NOT NULL
    UNION ALL
    SELECT 5, @batch_id_1, NULL, 20.00, 'kg', '2025-01-10 10:00:00', '0', '王五', CONCAT('批次', @batch_id_1, '种子领用'), '0', 4, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_1 IS NOT NULL
    UNION ALL
    -- 批次2的使用记录
    SELECT 1, @batch_id_2, @task_id_3, 150.00, 'kg', '2025-01-16 08:00:00', '1', '郑十', CONCAT('批次', @batch_id_2, '任务', @task_id_3, '复合肥料使用'), '0', 5, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_2 IS NOT NULL AND @task_id_3 IS NOT NULL
    UNION ALL
    SELECT 3, @batch_id_2, @task_id_4, 8.00, 'L', '2025-01-25 09:00:00', '1', '钱一', CONCAT('批次', @batch_id_2, '任务', @task_id_4, '杀虫剂使用'), '0', 6, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_2 IS NOT NULL AND @task_id_4 IS NOT NULL
    UNION ALL
    SELECT 6, @batch_id_2, @task_id_3, 200.00, 'm²', '2025-01-12 14:00:00', '1', '赵六', CONCAT('批次', @batch_id_2, '任务', @task_id_3, '地膜使用'), '0', 7, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_2 IS NOT NULL AND @task_id_3 IS NOT NULL
    UNION ALL
    -- 批次3的使用记录
    SELECT 4, @batch_id_3, @task_id_5, 3.00, 'L', '2025-01-14 15:00:00', '1', '赵六', CONCAT('批次', @batch_id_3, '任务', @task_id_5, '除草剂使用'), '0', 8, '1', NOW(), '1', NOW(), '0'
    WHERE @batch_id_3 IS NOT NULL AND @task_id_5 IS NOT NULL
    UNION ALL
    -- 入库记录（不关联批次和任务）
    SELECT 2, NULL, NULL, 30.00, 'kg', '2025-01-08 10:00:00', '2', '管理员', '仓库补货入库', '0', 9, '1', NOW(), '1', NOW(), '0'
    UNION ALL
    SELECT 5, NULL, NULL, 10.00, 'kg', '2025-01-09 11:00:00', '2', '管理员', '备用种子入库', '0', 10, '1', NOW(), '1', NOW(), '0'
    UNION ALL
    SELECT 1, NULL, NULL, 200.00, 'kg', '2025-01-07 09:00:00', '2', '管理员', '复合肥料采购入库', '0', 11, '1', NOW(), '1', NOW(), '0'
    UNION ALL
    SELECT 3, NULL, NULL, 50.00, 'L', '2025-01-06 14:00:00', '2', '管理员', '杀虫剂采购入库', '0', 12, '1', NOW(), '1', NOW(), '0'
) AS temp
WHERE resource_id IS NOT NULL;

