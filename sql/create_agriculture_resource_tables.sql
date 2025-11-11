-- 删除旧表
DROP TABLE IF EXISTS `agriculture_machine`;
DROP TABLE IF EXISTS `agriculture_material`;

-- 创建统一的农资资源表
CREATE TABLE `agriculture_resource` (
  `resource_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '农资ID',
  `resource_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '农资编码',
  `resource_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '农资名称',
  `resource_type` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '农资类型(0是物料,1是机械)',
  `resource_image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '农资图片',
  `measure_unit` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '计量单位',
  `remark` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `order_num` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '创建者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '修改人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '农资资源表' ROW_FORMAT = DYNAMIC;

-- 创建农资库存表
CREATE TABLE `agriculture_resource_inventory` (
  `inventory_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `resource_id` bigint UNSIGNED NOT NULL COMMENT '农资ID',
  `current_stock` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '当前库存',
  `min_stock` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '最小库存',
  `max_stock` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '最大库存',
  `remark` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '创建者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '修改人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`inventory_id`) USING BTREE,
  UNIQUE KEY `uk_resource_id` (`resource_id`) USING BTREE,
  CONSTRAINT `fk_inventory_resource` FOREIGN KEY (`resource_id`) REFERENCES `agriculture_resource` (`resource_id`) ON DELETE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '农资库存表' ROW_FORMAT = DYNAMIC;

-- 创建农资使用记录表
CREATE TABLE `agriculture_resource_usage` (
  `usage_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '使用记录ID',
  `resource_id` bigint UNSIGNED NOT NULL COMMENT '农资ID',
  `batch_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '种植批次ID',
  `task_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '批次任务ID',
  `usage_quantity` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '使用数量',
  `measure_unit` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '计量单位',
  `usage_date` datetime NOT NULL COMMENT '使用日期',
  `usage_type` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '使用类型(0是领用,1是消耗,2是入库)',
  `operator` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `remark` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '状态(0正常,1已撤销)',
  `order_num` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '创建者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '修改人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`usage_id`) USING BTREE,
  KEY `idx_resource_id` (`resource_id`) USING BTREE,  
  KEY `idx_batch_id` (`batch_id`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE,
  CONSTRAINT `fk_usage_resource` FOREIGN KEY (`resource_id`) REFERENCES `agriculture_resource` (`resource_id`) ON DELETE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '农资使用记录表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

