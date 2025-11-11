-- 修改 agriculture_resource_usage 表的外键约束
-- 将 ON DELETE RESTRICT 改为 ON DELETE CASCADE，实现级联删除

-- 1. 删除旧的外键约束
ALTER TABLE `agriculture_resource_usage` 
DROP FOREIGN KEY `fk_usage_resource`;

-- 2. 重新添加外键约束，使用 CASCADE 删除策略
ALTER TABLE `agriculture_resource_usage` 
ADD CONSTRAINT `fk_usage_resource` 
FOREIGN KEY (`resource_id`) 
REFERENCES `agriculture_resource` (`resource_id`) 
ON DELETE CASCADE 
ON UPDATE RESTRICT;

-- 验证：查询外键约束信息
-- SELECT 
--     CONSTRAINT_NAME,
--     TABLE_NAME,
--     COLUMN_NAME,
--     REFERENCED_TABLE_NAME,
--     REFERENCED_COLUMN_NAME,
--     DELETE_RULE,
--     UPDATE_RULE
-- FROM
--     INFORMATION_SCHEMA.KEY_COLUMN_USAGE
-- WHERE
--     TABLE_SCHEMA = DATABASE()
--     AND TABLE_NAME = 'agriculture_resource_usage'
--     AND CONSTRAINT_NAME = 'fk_usage_resource';

