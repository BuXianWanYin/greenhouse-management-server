-- ============================================
-- 重构采摘信息表 (agriculture_partition_food)
-- MySQL 8 兼容版本
-- 说明：
-- 1. 删除溯源相关字段（id、barcode、first_trace_time）
-- 2. 删除鱼相关字段（fish_weight、fish_status）
-- 3. 删除食品类型字段（food_type）
-- 4. 删除质量相关字段（status、cuisine_status）
-- 5. 删除冗余字段（name、description）
-- 6. 添加种质ID关联字段（class_id）
-- 7. 重构重量字段（cuisine_weight -> weight）
-- 8. 添加BaseEntityPlus基础字段
-- ============================================
-- 
-- 重要提示：
-- 1. 执行前请先备份数据表
-- 2. 如果某个列不存在，存储过程会忽略错误继续执行
-- 3. 如果遇到错误，请检查表结构是否与预期一致
-- ============================================

-- 备份表（强烈建议先执行）
-- CREATE TABLE agriculture_partition_food_backup AS SELECT * FROM agriculture_partition_food;

-- 开始事务
START TRANSACTION;

-- ============================================
-- 创建存储过程：安全删除列（如果存在）
-- ============================================
DELIMITER $$

DROP PROCEDURE IF EXISTS `drop_column_if_exists`$$

CREATE PROCEDURE `drop_column_if_exists`(
    IN p_table_name VARCHAR(128),
    IN p_column_name VARCHAR(128)
)
BEGIN
    DECLARE v_column_exists INT DEFAULT 0;
    DECLARE v_sql TEXT;
    DECLARE v_error INT DEFAULT 0;
    
    -- 声明错误处理器：捕获删除列时的错误（如列不存在）
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
    BEGIN
        SET v_error = 1;
        -- 忽略错误，继续执行
    END;
    
    -- 检查列是否存在
    SELECT COUNT(*) INTO v_column_exists
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND COLUMN_NAME = p_column_name;
    
    -- 如果列存在，则尝试删除
    IF v_column_exists > 0 THEN
        SET v_error = 0;
        SET v_sql = CONCAT('ALTER TABLE `', p_table_name, '` DROP COLUMN `', p_column_name, '`');
        SET @sql = v_sql;
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        -- 如果执行出错，v_error 会被设置为 1，但会被忽略
    END IF;
END$$

DELIMITER ;

-- ============================================
-- 创建存储过程：安全添加列（如果不存在）
-- ============================================
DELIMITER $$

DROP PROCEDURE IF EXISTS `add_column_if_not_exists`$$

CREATE PROCEDURE `add_column_if_not_exists`(
    IN table_name VARCHAR(128),
    IN column_definition TEXT
)
BEGIN
    DECLARE column_exists INT DEFAULT 0;
    DECLARE column_name VARCHAR(128);
    
    -- 从 column_definition 中提取列名（假设格式为 `column_name` TYPE ...）
    SET column_name = SUBSTRING_INDEX(SUBSTRING_INDEX(column_definition, '`', 2), '`', -1);
    
    SELECT COUNT(*) INTO column_exists
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = table_name
      AND COLUMN_NAME = column_name;
    
    IF column_exists = 0 THEN
        SET @sql = CONCAT('ALTER TABLE `', table_name, '` ADD COLUMN ', column_definition);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

-- ============================================
-- 创建存储过程：安全添加索引（如果不存在）
-- ============================================
DELIMITER $$

DROP PROCEDURE IF EXISTS `add_index_if_not_exists`$$

CREATE PROCEDURE `add_index_if_not_exists`(
    IN table_name VARCHAR(128),
    IN index_name VARCHAR(128),
    IN index_definition TEXT
)
BEGIN
    DECLARE index_exists INT DEFAULT 0;
    
    SELECT COUNT(*) INTO index_exists
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = table_name
      AND INDEX_NAME = index_name;
    
    IF index_exists = 0 THEN
        SET @sql = CONCAT('ALTER TABLE `', table_name, '` ADD INDEX `', index_name, '` ', index_definition);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$

DELIMITER ;

-- ============================================
-- 1. 删除不需要的字段（使用存储过程安全删除）
-- ============================================
-- 注意：如果某个列不存在，存储过程会忽略错误继续执行
CALL drop_column_if_exists('agriculture_partition_food', 'status');
CALL drop_column_if_exists('agriculture_partition_food', 'cuisine_status');
CALL drop_column_if_exists('agriculture_partition_food', 'fish_weight');
CALL drop_column_if_exists('agriculture_partition_food', 'fish_status');
CALL drop_column_if_exists('agriculture_partition_food', 'food_type');
CALL drop_column_if_exists('agriculture_partition_food', 'barcode');
CALL drop_column_if_exists('agriculture_partition_food', 'first_trace_time');
CALL drop_column_if_exists('agriculture_partition_food', 'name');
CALL drop_column_if_exists('agriculture_partition_food', 'description');

-- ============================================
-- 2. 处理主键和id字段
-- ============================================
-- 检查是否存在主键
SET @pk_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS 
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'agriculture_partition_food'
      AND CONSTRAINT_TYPE = 'PRIMARY KEY'
);

-- 如果存在主键，先删除
SET @sql = IF(@pk_exists > 0, 
    'ALTER TABLE `agriculture_partition_food` DROP PRIMARY KEY', 
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 删除旧的id字段（如果存在）
CALL drop_column_if_exists('agriculture_partition_food', 'id');

-- ============================================
-- 3. 添加新的自增主键id
-- ============================================
ALTER TABLE `agriculture_partition_food`
    ADD COLUMN `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '采摘ID' FIRST,
    ADD PRIMARY KEY (`id`);

-- ============================================
-- 4. 添加种质ID字段（关联种质信息表）
-- ============================================
CALL add_column_if_not_exists('agriculture_partition_food', 
    '`class_id` BIGINT(20) NOT NULL COMMENT ''种质ID'' AFTER `ia_partition_id`');

CALL add_index_if_not_exists('agriculture_partition_food', 'idx_class_id', '(`class_id`)');

-- ============================================
-- 5. 添加外键约束（可选，根据实际需求决定是否添加）
-- ============================================
-- ALTER TABLE `agriculture_partition_food`
--     ADD CONSTRAINT `fk_partition_food_class` 
--     FOREIGN KEY (`class_id`) 
--     REFERENCES `agriculture_class` (`class_id`) 
--     ON DELETE RESTRICT ON UPDATE CASCADE;

-- ============================================
-- 6. 重命名重量字段：cuisine_weight -> weight
-- ============================================
-- 检查 cuisine_weight 列是否存在
SET @col_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'agriculture_partition_food'
      AND COLUMN_NAME = 'cuisine_weight'
);

-- 如果存在 cuisine_weight，则重命名；如果不存在但存在 weight，则跳过
SET @sql = IF(@col_exists > 0,
    'ALTER TABLE `agriculture_partition_food` CHANGE COLUMN `cuisine_weight` `weight` DOUBLE(10,2) DEFAULT NULL COMMENT ''采摘重量(kg)''',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ============================================
-- 7. 添加BaseEntityPlus基础字段
-- ============================================
CALL add_column_if_not_exists('agriculture_partition_food', 
    '`create_by` VARCHAR(64) DEFAULT '''' COMMENT ''创建者ID''');
CALL add_column_if_not_exists('agriculture_partition_food', 
    '`create_time` DATETIME DEFAULT NULL COMMENT ''创建时间''');
CALL add_column_if_not_exists('agriculture_partition_food', 
    '`update_by` VARCHAR(64) DEFAULT '''' COMMENT ''修改人ID''');
CALL add_column_if_not_exists('agriculture_partition_food', 
    '`update_time` DATETIME DEFAULT NULL COMMENT ''修改时间''');
CALL add_column_if_not_exists('agriculture_partition_food', 
    '`remark` VARCHAR(500) DEFAULT NULL COMMENT ''备注''');

-- ============================================
-- 8. 为日期字段添加索引
-- ============================================
CALL add_index_if_not_exists('agriculture_partition_food', 'idx_date', '(`date`)');
CALL add_index_if_not_exists('agriculture_partition_food', 'idx_ia_partition_id', '(`ia_partition_id`)');

-- ============================================
-- 清理存储过程
-- ============================================
DROP PROCEDURE IF EXISTS `drop_column_if_exists`;
DROP PROCEDURE IF EXISTS `add_column_if_not_exists`;
DROP PROCEDURE IF EXISTS `add_index_if_not_exists`;

-- 提交事务
COMMIT;

-- ============================================
-- 验证表结构
-- ============================================
-- DESCRIBE agriculture_partition_food;
-- SHOW CREATE TABLE agriculture_partition_food;

-- ============================================
-- 如果存储过程执行失败，可以使用以下方法手动检查并删除列
-- ============================================
-- 检查列是否存在：
-- SELECT COLUMN_NAME 
-- FROM INFORMATION_SCHEMA.COLUMNS 
-- WHERE TABLE_SCHEMA = DATABASE()
--   AND TABLE_NAME = 'agriculture_partition_food'
--   AND COLUMN_NAME IN ('status', 'cuisine_status', 'fish_weight', 'fish_status', 
--                       'food_type', 'barcode', 'first_trace_time', 'name', 'description');
--
-- 如果某个列存在但存储过程删除失败，可以手动删除：
-- ALTER TABLE `agriculture_partition_food` DROP COLUMN `barcode`;

-- ============================================
-- 数据迁移说明（如果需要保留现有数据）
-- ============================================
-- 如果表中有现有数据，需要执行以下步骤：
-- 
-- 1. 如果原id是字符串类型，需要先处理数据：
--    UPDATE agriculture_partition_food SET class_id = (SELECT class_id FROM agriculture_class WHERE class_name = name LIMIT 1);
--    注意：需要根据实际情况匹配种质名称
-- 
-- 2. 如果原表没有class_id对应的数据，需要先补充种质信息
-- 
-- 3. 迁移重量数据（如果字段名不同）：
--    UPDATE agriculture_partition_food SET weight = cuisine_weight WHERE cuisine_weight IS NOT NULL;

