-- ============================================
-- 种植计划管理模块完善方案
-- ============================================

-- ============================================
-- 一、修改现有表 agriculture_crop_batch
-- ============================================

-- 1. 删除不需要的字段（MySQL 8.0.19+支持 IF EXISTS）
ALTER TABLE `agriculture_crop_batch` 
DROP COLUMN IF EXISTS `germplasm_id`,
DROP COLUMN IF EXISTS `vegetable_id`,
DROP COLUMN IF EXISTS `fish_area`,
DROP COLUMN IF EXISTS `contract_addr`;

-- 2. 新增统一种质ID字段（替代germplasm_id和vegetable_id）
-- 注意：如果字段已存在会报错，可以忽略继续执行
ALTER TABLE `agriculture_crop_batch` 
ADD COLUMN `class_id` BIGINT(20) NULL COMMENT '种质ID（关联agriculture_class表）' AFTER `batch_name`;

-- 3. 新增种植计划管理相关字段
-- 注意：如果字段已存在会报错，可以忽略继续执行
ALTER TABLE `agriculture_crop_batch` 
ADD COLUMN `plan_year` INT(4) NULL COMMENT '计划年份' AFTER `class_id`,
ADD COLUMN `season_type` VARCHAR(50) NULL COMMENT '季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季）' AFTER `plan_year`,
ADD COLUMN `rotation_plan_id` BIGINT(20) NULL COMMENT '轮作计划ID（关联agriculture_rotation_plan表）' AFTER `season_type`,
ADD COLUMN `planting_density` DECIMAL(10,2) NULL COMMENT '种植密度（株/亩）' AFTER `rotation_plan_id`,
ADD COLUMN `expected_harvest_time` DATETIME NULL COMMENT '预期收获时间' AFTER `planting_density`,
ADD COLUMN `current_growth_stage` VARCHAR(50) NULL COMMENT '当前生长阶段（seedling=幼苗期,growth=生长期,flowering=开花期,fruiting=结果期,mature=成熟期）' AFTER `expected_harvest_time`,
ADD COLUMN `growth_stage_start_time` DATETIME NULL COMMENT '当前生长阶段开始时间' AFTER `current_growth_stage`,
ADD COLUMN `total_growth_days` INT(11) NULL COMMENT '总生长天数' AFTER `growth_stage_start_time`,
ADD COLUMN `actual_harvest_time` DATETIME NULL COMMENT '实际收获时间' AFTER `total_growth_days`;

-- ============================================
-- 二、新建表：年度种植规划表
-- ============================================
CREATE TABLE IF NOT EXISTS `agriculture_annual_plan` (
  `plan_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '计划ID',
  `plan_year` INT(4) NOT NULL COMMENT '计划年份',
  `plan_name` VARCHAR(100) NOT NULL COMMENT '计划名称',
  `pasture_id` BIGINT(20) NULL COMMENT '温室ID（关联agriculture_pasture表，NULL表示全温室）',
  `plan_type` VARCHAR(50) NULL COMMENT '计划类型（annual=年度计划,seasonal=季节性计划）',
  `plan_status` VARCHAR(20) NULL DEFAULT '0' COMMENT '计划状态（0=草稿,1=已发布,2=执行中,3=已完成,4=已取消）',
  `start_date` DATE NULL COMMENT '计划开始日期',
  `end_date` DATE NULL COMMENT '计划结束日期',
  `total_area` DECIMAL(10,2) NULL COMMENT '计划总面积（亩）',
  `plan_description` TEXT NULL COMMENT '计划描述',
  `create_by` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  `del_flag` CHAR(1) NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`plan_id`),
  KEY `idx_plan_year` (`plan_year`),
  KEY `idx_pasture_id` (`pasture_id`),
  KEY `idx_plan_status` (`plan_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年度种植规划表';

-- ============================================
-- 三、新建表：轮作计划表
-- ============================================
CREATE TABLE IF NOT EXISTS `agriculture_rotation_plan` (
  `rotation_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '轮作计划ID',
  `rotation_name` VARCHAR(100) NOT NULL COMMENT '轮作计划名称',
  `plan_year` INT(4) NULL COMMENT '计划年份',
  `pasture_id` BIGINT(20) NULL COMMENT '温室ID（关联agriculture_pasture表）',
  `rotation_cycle` INT(11) NULL COMMENT '轮作周期（年）',
  `rotation_description` TEXT NULL COMMENT '轮作描述',
  `rotation_status` VARCHAR(20) NULL DEFAULT '0' COMMENT '状态（0=草稿,1=执行中,2=已完成）',
  `create_by` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  `del_flag` CHAR(1) NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`rotation_id`),
  KEY `idx_plan_year` (`plan_year`),
  KEY `idx_pasture_id` (`pasture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮作计划表';

-- ============================================
-- 四、新建表：轮作计划明细表（记录轮作顺序）
-- ============================================
CREATE TABLE IF NOT EXISTS `agriculture_rotation_detail` (
  `detail_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `rotation_id` BIGINT(20) NOT NULL COMMENT '轮作计划ID（关联agriculture_rotation_plan表）',
  `class_id` BIGINT(20) NOT NULL COMMENT '种质ID（关联agriculture_class表）',
  `rotation_order` INT(11) NOT NULL COMMENT '轮作顺序（1,2,3...）',
  `season_type` VARCHAR(50) NULL COMMENT '季节类型（spring=春季,summer=夏季,autumn=秋季,winter=冬季）',
  `planting_area` DECIMAL(10,2) NULL COMMENT '种植面积（亩）',
  `planting_density` DECIMAL(10,2) NULL COMMENT '种植密度（株/亩）',
  `expected_start_date` DATE NULL COMMENT '预期开始日期',
  `expected_end_date` DATE NULL COMMENT '预期结束日期',
  `create_by` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detail_id`),
  KEY `idx_rotation_id` (`rotation_id`),
  KEY `idx_class_id` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮作计划明细表';

-- ============================================
-- 五、新建表：生长阶段表
-- ============================================
CREATE TABLE IF NOT EXISTS `agriculture_growth_stage` (
  `stage_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '生长阶段ID',
  `batch_id` BIGINT(20) NOT NULL COMMENT '批次ID（关联agriculture_crop_batch表）',
  `stage_type` VARCHAR(50) NOT NULL COMMENT '生长阶段类型（seedling=幼苗期,growth=生长期,flowering=开花期,fruiting=结果期,mature=成熟期）',
  `stage_name` VARCHAR(100) NULL COMMENT '阶段名称',
  `stage_order` INT(11) NULL COMMENT '阶段顺序（1,2,3...）',
  `expected_start_date` DATE NULL COMMENT '预期开始日期',
  `expected_end_date` DATE NULL COMMENT '预期结束日期',
  `actual_start_date` DATE NULL COMMENT '实际开始日期',
  `actual_end_date` DATE NULL COMMENT '实际结束日期',
  `stage_status` VARCHAR(20) NULL DEFAULT '0' COMMENT '阶段状态（0=未开始,1=进行中,2=已完成）',
  `stage_description` TEXT NULL COMMENT '阶段描述',
  `create_by` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`stage_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_stage_type` (`stage_type`),
  KEY `idx_stage_status` (`stage_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生长阶段表';

-- ============================================
-- 六、新建表：生长关键节点表（播种、移栽、开花、结果、收获）
-- ============================================
CREATE TABLE IF NOT EXISTS `agriculture_growth_node` (
  `node_id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '节点ID',
  `batch_id` BIGINT(20) NOT NULL COMMENT '批次ID（关联agriculture_crop_batch表）',
  `node_type` VARCHAR(50) NOT NULL COMMENT '节点类型（sowing=播种,transplanting=移栽,flowering=开花,fruiting=结果,harvest=收获）',
  `node_name` VARCHAR(100) NULL COMMENT '节点名称',
  `expected_date` DATE NULL COMMENT '预期日期',
  `actual_date` DATE NULL COMMENT '实际日期',
  `remind_days` INT(11) NULL DEFAULT 7 COMMENT '提前提醒天数',
  `remind_status` VARCHAR(20) NULL DEFAULT '0' COMMENT '提醒状态（0=未提醒,1=已提醒,2=已完成）',
  `node_status` VARCHAR(20) NULL DEFAULT '0' COMMENT '节点状态（0=未开始,1=进行中,2=已完成）',
  `node_description` TEXT NULL COMMENT '节点描述',
  `node_images` TEXT NULL COMMENT '节点图片（多个图片URL，逗号分隔）',
  `node_videos` TEXT NULL COMMENT '节点视频（多个视频URL，逗号分隔）',
  `create_by` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`node_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_node_type` (`node_type`),
  KEY `idx_expected_date` (`expected_date`),
  KEY `idx_remind_status` (`remind_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生长关键节点表';

-- ============================================
-- 七、新建表：年度计划批次关联表
-- ============================================
CREATE TABLE IF NOT EXISTS `agriculture_plan_batch` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plan_id` BIGINT(20) NOT NULL COMMENT '年度计划ID（关联agriculture_annual_plan表）',
  `batch_id` BIGINT(20) NOT NULL COMMENT '批次ID（关联agriculture_crop_batch表）',
  `create_by` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plan_batch` (`plan_id`, `batch_id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年度计划批次关联表';

-- ============================================
-- 八、数据迁移建议（可选）
-- ============================================
-- 如果现有数据中有germplasm_id或vegetable_id，可以迁移到新的class_id字段
-- UPDATE agriculture_crop_batch SET class_id = COALESCE(vegetable_id, germplasm_id) WHERE class_id IS NULL;

