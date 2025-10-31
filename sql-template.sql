/*
 Navicat Premium Dump SQL

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 30/10/2025 19:44:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (9, 'sys_config', '参数配置表', NULL, NULL, 'SysConfig', 'crud', 'element-plus', 'com.system', 'system', 'config', '参数配置', 'server', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26', NULL);
INSERT INTO `gen_table` VALUES (10, 'sys_dict_data', '字典数据表', NULL, NULL, 'DictData', 'crud', 'element-plus', 'com.system', 'system', 'data', '字典数据', 'server', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17', NULL);
INSERT INTO `gen_table` VALUES (11, 'sys_dict_type', '字典类型表', NULL, NULL, 'DictType', 'crud', 'element-plus', 'com.system', 'system', 'type', '字典类型', 'server', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23', NULL);
INSERT INTO `gen_table` VALUES (12, 'sys_notice', '通知公告表', NULL, NULL, 'Notice', 'crud', 'element-plus', 'com.system', 'system', 'notice', '通知公告', 'server', '0', '/', '{}', 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11', NULL);
INSERT INTO `gen_table` VALUES (13, 'sys_logininfor', '系统访问记录', NULL, NULL, 'Logininfor', 'crud', 'element-plus', 'com.monitor', 'monitor', 'logininfor', '系统访问记录', 'server', '0', '/', '{}', 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47', NULL);
INSERT INTO `gen_table` VALUES (14, 'sys_oper_log', '操作日志记录', NULL, NULL, 'OperLog', 'crud', 'element-plus', 'com.monitor', 'monitor', 'operlog', '操作日志记录', 'server', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30', NULL);
INSERT INTO `gen_table` VALUES (15, 'sys_role', '角色信息表', NULL, NULL, 'Role', 'crud', 'element-plus', 'com.system', 'system', 'role', '角色信息', 'server', '0', '/', '{}', 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51', NULL);
INSERT INTO `gen_table` VALUES (16, 'sys_user', '用户信息表', NULL, NULL, 'User', 'crud', 'element-plus', 'com.system', 'system', 'user', '用户信息', 'server', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18', NULL);
INSERT INTO `gen_table` VALUES (17, 'sys_dept', '部门表', NULL, NULL, 'Dept', 'crud', 'element-plus', 'com.system', 'system', 'dept', '部门', 'server', '0', '/', '{}', 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09', NULL);
INSERT INTO `gen_table` VALUES (18, 'sys_post', '岗位信息表', NULL, NULL, 'Post', 'crud', 'element-plus', 'com.system', 'system', 'post', '岗位信息', 'server', '0', '/', '{}', 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48', NULL);
INSERT INTO `gen_table` VALUES (19, 'sys_job', '定时任务调度表', NULL, NULL, 'Job', 'crud', 'element-plus', 'com.system', 'system', 'job', '定时任务调度', 'server', '0', '/', '{}', 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57', NULL);
INSERT INTO `gen_table` VALUES (20, 'sys_job_log', '定时任务调度日志表', NULL, NULL, 'JobLog', 'crud', 'element-plus', 'com.system', 'system', 'log', '定时任务调度日志', 'server', '0', '/', '{}', 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23', NULL);
INSERT INTO `gen_table` VALUES (21, 'sys_menu', '菜单权限表', NULL, NULL, 'SysMenu', 'crud', '', 'com.server', 'server', 'menu', '菜单权限', 'server', '0', '/', NULL, 'admin', '2025-04-23 14:49:37', '', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 677 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (61, 9, 'config_id', '参数主键', 'int', 'Long', 'configId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (62, 9, 'config_name', '参数名称', 'varchar(100)', 'String', 'configName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (63, 9, 'config_key', '参数键名', 'varchar(100)', 'String', 'configKey', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (64, 9, 'config_value', '参数键值', 'varchar(500)', 'String', 'configValue', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 4, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (65, 9, 'config_type', '系统内置（Y是 N否）', 'char(1)', 'String', 'configType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', 'sys_show_hide', 5, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (66, 9, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (67, 9, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (68, 9, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (69, 9, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (70, 9, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 10, 'admin', '2025-04-19 23:04:05', '', '2025-04-20 00:22:26');
INSERT INTO `gen_table_column` VALUES (71, 10, 'dict_code', '字典编码', 'bigint', 'Long', 'dictCode', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (72, 10, 'dict_sort', '字典排序', 'int', 'Long', 'dictSort', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (73, 10, 'dict_label', '字典标签', 'varchar(100)', 'String', 'dictLabel', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (74, 10, 'dict_value', '字典键值', 'varchar(100)', 'String', 'dictValue', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (75, 10, 'dict_type', '字典类型', 'varchar(100)', 'String', 'dictType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 5, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (76, 10, 'css_class', '样式属性（其他样式扩展）', 'varchar(100)', 'String', 'cssClass', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (77, 10, 'list_class', '表格回显样式', 'varchar(100)', 'String', 'listClass', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (78, 10, 'is_default', '是否默认（Y是 N否）', 'char(1)', 'String', 'isDefault', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (79, 10, 'status', '状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (80, 10, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (81, 10, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (82, 10, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (83, 10, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (84, 10, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 14, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:17');
INSERT INTO `gen_table_column` VALUES (85, 11, 'dict_id', '字典主键', 'bigint', 'Long', 'dictId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (86, 11, 'dict_name', '字典名称', 'varchar(100)', 'String', 'dictName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (87, 11, 'dict_type', '字典类型', 'varchar(100)', 'String', 'dictType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (88, 11, 'status', '状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 4, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (89, 11, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (90, 11, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (91, 11, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (92, 11, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (93, 11, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 9, 'admin', '2025-04-20 00:48:10', '', '2025-04-20 01:11:23');
INSERT INTO `gen_table_column` VALUES (94, 12, 'notice_id', '公告ID', 'int', 'Long', 'noticeId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (95, 12, 'notice_title', '公告标题', 'varchar(50)', 'String', 'noticeTitle', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (96, 12, 'notice_type', '公告类型（1通知 2公告）', 'char(1)', 'String', 'noticeType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (97, 12, 'notice_content', '公告内容', 'longblob', 'String', 'noticeContent', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'editor', '', 4, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (98, 12, 'status', '公告状态（0正常 1关闭）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 5, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (99, 12, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (100, 12, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (101, 12, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (102, 12, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (103, 12, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'input', '', 10, 'admin', '2025-04-20 20:36:19', '', '2025-04-20 20:46:11');
INSERT INTO `gen_table_column` VALUES (104, 13, 'info_id', '访问ID', 'bigint', 'Long', 'infoId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (105, 13, 'user_name', '用户账号', 'varchar(50)', 'String', 'userName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (106, 13, 'ipaddr', '登录IP地址', 'varchar(128)', 'String', 'ipaddr', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (107, 13, 'login_location', '登录地点', 'varchar(255)', 'String', 'loginLocation', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (108, 13, 'browser', '浏览器类型', 'varchar(50)', 'String', 'browser', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (109, 13, 'os', '操作系统', 'varchar(50)', 'String', 'os', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (110, 13, 'status', '登录状态（0成功 1失败）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 7, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (111, 13, 'msg', '提示消息', 'varchar(255)', 'String', 'msg', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (112, 13, 'login_time', '访问时间', 'datetime', 'Date', 'loginTime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 9, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:35:47');
INSERT INTO `gen_table_column` VALUES (113, 14, 'oper_id', '日志主键', 'bigint', 'Long', 'operId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (114, 14, 'title', '模块标题', 'varchar(50)', 'String', 'title', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (115, 14, 'business_type', '业务类型（0其它 1新增 2修改 3删除）', 'int', 'Long', 'businessType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (116, 14, 'method', '方法名称', 'varchar(200)', 'String', 'method', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (117, 14, 'request_method', '请求方式', 'varchar(10)', 'String', 'requestMethod', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (118, 14, 'operator_type', '操作类别（0其它 1后台用户 2手机端用户）', 'int', 'Long', 'operatorType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 6, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (119, 14, 'oper_name', '操作人员', 'varchar(50)', 'String', 'operName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 7, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (120, 14, 'dept_name', '部门名称', 'varchar(50)', 'String', 'deptName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 8, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (121, 14, 'oper_url', '请求URL', 'varchar(255)', 'String', 'operUrl', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (122, 14, 'oper_ip', '主机地址', 'varchar(128)', 'String', 'operIp', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (123, 14, 'oper_location', '操作地点', 'varchar(255)', 'String', 'operLocation', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (124, 14, 'oper_param', '请求参数', 'varchar(2000)', 'String', 'operParam', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 12, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (125, 14, 'json_result', '返回参数', 'varchar(2000)', 'String', 'jsonResult', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 13, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (126, 14, 'status', '操作状态（0正常 1异常）', 'int', 'Long', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 14, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (127, 14, 'error_msg', '错误消息', 'varchar(2000)', 'String', 'errorMsg', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 15, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (128, 14, 'oper_time', '操作时间', 'datetime', 'Date', 'operTime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 16, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (129, 14, 'cost_time', '消耗时间', 'bigint', 'Long', 'costTime', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 17, 'admin', '2025-04-20 21:34:08', '', '2025-04-20 21:36:30');
INSERT INTO `gen_table_column` VALUES (130, 15, 'role_id', '角色ID', 'bigint', 'Long', 'roleId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (131, 15, 'role_name', '角色名称', 'varchar(30)', 'String', 'roleName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (132, 15, 'role_key', '角色权限字符串', 'varchar(100)', 'String', 'roleKey', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (133, 15, 'role_sort', '显示顺序', 'int', 'Long', 'roleSort', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (134, 15, 'data_scope', '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）', 'char(1)', 'String', 'dataScope', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (135, 15, 'menu_check_strictly', '菜单树选择项是否关联显示', 'tinyint(1)', 'Integer', 'menuCheckStrictly', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (136, 15, 'dept_check_strictly', '部门树选择项是否关联显示', 'tinyint(1)', 'Integer', 'deptCheckStrictly', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (137, 15, 'status', '角色状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 8, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (138, 15, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (139, 15, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (140, 15, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (141, 15, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (142, 15, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (143, 15, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 14, 'admin', '2025-04-21 16:26:24', '', '2025-04-21 16:27:51');
INSERT INTO `gen_table_column` VALUES (144, 16, 'user_id', '用户ID', 'bigint', 'Long', 'userId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (145, 16, 'dept_id', '部门ID', 'bigint', 'Long', 'deptId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (146, 16, 'user_name', '用户账号', 'varchar(30)', 'String', 'userName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (147, 16, 'nick_name', '用户昵称', 'varchar(30)', 'String', 'nickName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (148, 16, 'user_type', '用户类型（00系统用户）', 'varchar(2)', 'String', 'userType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 5, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (149, 16, 'email', '用户邮箱', 'varchar(50)', 'String', 'email', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (150, 16, 'phonenumber', '手机号码', 'varchar(11)', 'String', 'phonenumber', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (151, 16, 'sex', '用户性别（0男 1女 2未知）', 'char(1)', 'String', 'sex', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 8, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (152, 16, 'avatar', '头像地址', 'varchar(100)', 'String', 'avatar', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (153, 16, 'password', '密码', 'varchar(100)', 'String', 'password', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (154, 16, 'status', '帐号状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 11, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (155, 16, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (156, 16, 'login_ip', '最后登录IP', 'varchar(128)', 'String', 'loginIp', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 13, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (157, 16, 'login_date', '最后登录时间', 'datetime', 'Date', 'loginDate', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'datetime', '', 14, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:18');
INSERT INTO `gen_table_column` VALUES (158, 16, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 15, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:19');
INSERT INTO `gen_table_column` VALUES (159, 16, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 16, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:19');
INSERT INTO `gen_table_column` VALUES (160, 16, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 17, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:19');
INSERT INTO `gen_table_column` VALUES (161, 16, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 18, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:19');
INSERT INTO `gen_table_column` VALUES (162, 16, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 19, 'admin', '2025-04-21 16:26:24', '', '2025-06-04 21:23:19');
INSERT INTO `gen_table_column` VALUES (163, 17, 'dept_id', '部门id', 'bigint', 'Long', 'deptId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (164, 17, 'parent_id', '父部门id', 'bigint', 'Long', 'parentId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (165, 17, 'ancestors', '祖级列表', 'varchar(50)', 'String', 'ancestors', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (166, 17, 'dept_name', '部门名称', 'varchar(30)', 'String', 'deptName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 4, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (167, 17, 'order_num', '显示顺序', 'int', 'Long', 'orderNum', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (168, 17, 'leader', '负责人', 'varchar(20)', 'String', 'leader', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (169, 17, 'phone', '联系电话', 'varchar(11)', 'String', 'phone', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (170, 17, 'email', '邮箱', 'varchar(50)', 'String', 'email', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (171, 17, 'status', '部门状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (172, 17, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (173, 17, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (174, 17, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (175, 17, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (176, 17, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 14, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 01:34:09');
INSERT INTO `gen_table_column` VALUES (177, 18, 'post_id', '岗位ID', 'bigint', 'Long', 'postId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (178, 18, 'post_code', '岗位编码', 'varchar(64)', 'String', 'postCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (179, 18, 'post_name', '岗位名称', 'varchar(50)', 'String', 'postName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (180, 18, 'post_sort', '显示顺序', 'int', 'Long', 'postSort', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (181, 18, 'status', '状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 5, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (182, 18, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (183, 18, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (184, 18, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (185, 18, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (186, 18, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 10, 'admin', '2025-04-21 16:26:38', '', '2025-04-22 03:10:48');
INSERT INTO `gen_table_column` VALUES (187, 19, 'job_id', '任务ID', 'bigint', 'Long', 'jobId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (188, 19, 'job_name', '任务名称', 'varchar(64)', 'String', 'jobName', '1', '0', '0', '1', NULL, NULL, NULL, 'LIKE', 'input', '', 2, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (189, 19, 'job_group', '任务组名', 'varchar(64)', 'String', 'jobGroup', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 3, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (190, 19, 'invoke_target', '调用目标字符串', 'varchar(500)', 'String', 'invokeTarget', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 4, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (191, 19, 'cron_expression', 'cron执行表达式', 'varchar(255)', 'String', 'cronExpression', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (192, 19, 'misfire_policy', '计划执行错误策略（1立即执行 2执行一次 3放弃执行）', 'varchar(20)', 'String', 'misfirePolicy', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (193, 19, 'concurrent', '是否并发执行（0允许 1禁止）', 'char(1)', 'String', 'concurrent', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (194, 19, 'status', '状态（0正常 1暂停）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 8, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (195, 19, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (196, 19, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (197, 19, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (198, 19, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (199, 19, 'remark', '备注信息', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 13, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:49:57');
INSERT INTO `gen_table_column` VALUES (200, 20, 'job_log_id', '任务日志ID', 'bigint', 'Long', 'jobLogId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (201, 20, 'job_name', '任务名称', 'varchar(64)', 'String', 'jobName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (202, 20, 'job_group', '任务组名', 'varchar(64)', 'String', 'jobGroup', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (203, 20, 'invoke_target', '调用目标字符串', 'varchar(500)', 'String', 'invokeTarget', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 4, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (204, 20, 'job_message', '日志信息', 'varchar(500)', 'String', 'jobMessage', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 5, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (205, 20, 'status', '执行状态（0正常 1失败）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 6, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (206, 20, 'exception_info', '异常信息', 'varchar(2000)', 'String', 'exceptionInfo', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'textarea', '', 7, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (207, 20, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-04-23 14:49:37', '', '2025-04-23 14:50:23');
INSERT INTO `gen_table_column` VALUES (208, 21, 'menu_id', '菜单ID', 'bigint', 'Long', 'menuId', '1', '1', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (209, 21, 'menu_name', '菜单名称', 'varchar(50)', 'String', 'menuName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (210, 21, 'parent_id', '父菜单ID', 'bigint', 'Long', 'parentId', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (211, 21, 'order_num', '显示顺序', 'int', 'Long', 'orderNum', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (212, 21, 'path', '路由地址', 'varchar(200)', 'String', 'path', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (213, 21, 'component', '组件路径', 'varchar(255)', 'String', 'component', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (214, 21, 'query', '路由参数', 'varchar(255)', 'String', 'query', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (215, 21, 'route_name', '路由名称', 'varchar(50)', 'String', 'routeName', '0', '0', '0', '1', '1', '1', '1', 'LIKE', 'input', '', 8, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (216, 21, 'is_frame', '是否为外链（0是 1否）', 'int', 'Long', 'isFrame', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (217, 21, 'is_cache', '是否缓存（0缓存 1不缓存）', 'int', 'Long', 'isCache', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (218, 21, 'menu_type', '菜单类型（M目录 C菜单 F按钮）', 'char(1)', 'String', 'menuType', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'select', '', 11, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (219, 21, 'visible', '菜单状态（0显示 1隐藏）', 'char(1)', 'String', 'visible', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 12, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (220, 21, 'status', '菜单状态（0正常 1停用）', 'char(1)', 'String', 'status', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'radio', '', 13, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (221, 21, 'perms', '权限标识', 'varchar(100)', 'String', 'perms', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 14, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (222, 21, 'icon', '菜单图标', 'varchar(100)', 'String', 'icon', '0', '0', '0', '1', '1', '1', '1', 'EQ', 'input', '', 15, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (223, 21, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 16, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (224, 21, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 17, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (225, 21, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'input', '', 18, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (226, 21, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', '0', '1', '1', NULL, NULL, 'EQ', 'datetime', '', 19, 'admin', '2025-04-23 14:49:37', '', NULL);
INSERT INTO `gen_table_column` VALUES (227, 21, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', '0', '1', '1', '1', NULL, 'EQ', 'textarea', '', 20, 'admin', '2025-04-23 14:49:37', '', NULL);

-- ----------------------------
-- Table structure for param_type_dict
-- ----------------------------
DROP TABLE IF EXISTS `param_type_dict`;
CREATE TABLE `param_type_dict`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `param_type_en` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数英文名',
  `param_type_cn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数中文名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '传感器参数类型中英文对照表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of param_type_dict
-- ----------------------------
INSERT INTO `param_type_dict` VALUES (1, 'temperature', '空气温度', '气象-温度');
INSERT INTO `param_type_dict` VALUES (2, 'humidity', '空气湿度', '气象-湿度');
INSERT INTO `param_type_dict` VALUES (3, 'wind_speed', '风速', '气象-风速');
INSERT INTO `param_type_dict` VALUES (4, 'light_intensity', '光照强度', '气象-光照');
INSERT INTO `param_type_dict` VALUES (6, 'ph_value', 'PH值', '水质-PH');
INSERT INTO `param_type_dict` VALUES (7, 'dissolved_oxygen', '溶解氧', '水质-溶解氧');
INSERT INTO `param_type_dict` VALUES (8, 'ammonia_nitrogen', '氨氮', '水质-氨氮');
INSERT INTO `param_type_dict` VALUES (9, 'water_temperature', '水温', '水质-水温');
INSERT INTO `param_type_dict` VALUES (10, 'wind_direction', '风向', '气象-风向');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-03-30 12:47:29', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'false', 'Y', 'admin', '2025-03-30 12:47:29', 'admin', '2025-05-13 16:18:29', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2025-03-30 12:47:29', 'admin', '2025-05-13 16:18:37', '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-03-30 12:47:29', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (100, '账号自助-滑块开关', 'sys.account.sliderEnabled', 'false', 'Y', 'admin', '2025-04-01 17:12:59', 'admin', '2025-05-13 16:17:57', '是否开启滑块功能（true开启，false关闭）\n');
INSERT INTO `sys_config` VALUES (101, '账号自助-忘记密码开关', 'sys.account.forgetPasswordEnabled', 'true', 'Y', 'admin', '2025-04-01 20:54:23', 'admin', '2025-04-01 21:05:41', '是否开启忘记密码功能（true开启，false关闭）\n');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-03-30 12:47:29', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-03-30 12:47:28', 'admin', '2025-04-21 16:02:14', '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-03-30 12:47:28', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2025-03-30 12:47:29', '', '2025-04-23 15:14:53', '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2025-03-30 12:47:29', '', '2025-04-23 15:25:15', '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2025-03-30 12:47:29', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '系统默认（有参） 总共耗时：3毫秒', '0', '', '2025-04-23 16:36:41');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-10-03 16:26:07');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2025-10-10 19:21:38');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2051 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (2000, '系统管理', 0, 16, 'system', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '&#xe6d0;', 'admin', '2025-04-12 16:15:20', 'admin', '2025-07-30 10:00:39', '');
INSERT INTO `sys_menu` VALUES (2001, '菜单权限', 2000, 2, 'menu', 'system/menu/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe8a4;', 'admin', '2025-04-12 16:16:36', 'admin', '2025-07-28 17:27:49', '');
INSERT INTO `sys_menu` VALUES (2002, '角色管理', 0, 12, 'role', '/system/role/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe724;', 'admin', '2025-04-12 19:36:03', 'admin', '2025-07-28 17:35:56', '');
INSERT INTO `sys_menu` VALUES (2004, '系统工具', 0, 9, 'tool', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '&#xe884;', 'admin', '2025-04-13 14:18:09', 'admin', '2025-10-30 19:23:28', '');
INSERT INTO `sys_menu` VALUES (2005, '代码生成', 2004, 0, 'gen', 'tool/gen/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe615;', 'admin', '2025-04-13 14:23:05', 'admin', '2025-04-24 23:21:29', '');
INSERT INTO `sys_menu` VALUES (2006, '生成修改', 2005, 0, '', NULL, NULL, '', 1, 0, 'F', '0', '0', NULL, '#', 'admin', '2025-04-15 10:59:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2007, '参数设置', 2000, 6, 'config', 'system/config/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe6bf;', 'admin', '2025-04-19 22:40:36', 'admin', '2025-04-24 23:13:40', '');
INSERT INTO `sys_menu` VALUES (2008, '字典管理', 2000, 5, 'dict', 'system/dict/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe897;', 'admin', '2025-04-20 01:22:58', 'admin', '2025-04-24 23:18:39', '');
INSERT INTO `sys_menu` VALUES (2009, '系统接口', 2004, 0, 'http://localhost:8080/swagger-ui/index.html', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '&#xe674;', 'admin', '2025-04-20 01:37:50', 'admin', '2025-04-20 01:38:13', '');
INSERT INTO `sys_menu` VALUES (2010, '通知公告', 0, 15, 'notice', '/system/notice/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe6e9;', 'admin', '2025-04-20 20:51:20', 'admin', '2025-07-30 10:01:01', '');
INSERT INTO `sys_menu` VALUES (2011, '日志管理', 0, 17, 'log', NULL, NULL, '', 1, 0, 'M', '0', '0', '', '&#xe88c;', 'admin', '2025-04-20 22:23:35', 'admin', '2025-07-30 10:01:27', '');
INSERT INTO `sys_menu` VALUES (2012, '登陆日志', 2011, 0, 'logininfor', 'monitor/logininfor/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe88b;', 'admin', '2025-04-20 22:26:09', 'admin', '2025-04-20 22:27:35', '');
INSERT INTO `sys_menu` VALUES (2013, '操作日志', 2011, 0, 'operlog', 'monitor/operlog/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe832;', 'admin', '2025-04-20 22:27:15', 'admin', '2025-04-20 22:28:42', '');
INSERT INTO `sys_menu` VALUES (2014, '用户管理', 0, 11, 'users', '/system/user/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe734;', 'admin', '2025-04-21 16:44:31', 'admin', '2025-07-30 10:07:53', '');
INSERT INTO `sys_menu` VALUES (2015, '部门管理', 0, 13, 'dept', '/system/dept/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe7b7;', 'admin', '2025-04-22 01:30:25', 'admin', '2025-07-28 17:36:01', '');
INSERT INTO `sys_menu` VALUES (2016, '岗位管理', 0, 14, 'post', '/system/post/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe86e;', 'admin', '2025-04-22 01:32:44', 'admin', '2025-07-28 17:36:39', '');
INSERT INTO `sys_menu` VALUES (2017, '系统监控', 0, 7, 'monitor', NULL, NULL, '', 1, 0, 'M', '1', '0', '', '&#xe6b2;', 'admin', '2025-04-22 13:43:10', 'admin', '2025-07-28 16:05:59', '');
INSERT INTO `sys_menu` VALUES (2018, '在线用户', 0, 10, 'online', '/monitor/online/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe7c3;', 'admin', '2025-04-22 14:04:13', 'admin', '2025-07-28 17:34:37', '');
INSERT INTO `sys_menu` VALUES (2019, '服务监控', 2017, 3, 'server', 'monitor/server/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe7ee;', 'admin', '2025-04-22 14:47:38', 'admin', '2025-04-22 16:09:08', '');
INSERT INTO `sys_menu` VALUES (2020, '缓存监控', 2017, 4, 'cache', 'monitor/cache/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe743;', 'admin', '2025-04-22 16:04:27', 'admin', '2025-04-22 16:08:59', '');
INSERT INTO `sys_menu` VALUES (2021, '缓存列表', 2017, 5, 'cacheList', 'monitor/cache/list', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe742;', 'admin', '2025-04-22 16:08:09', 'admin', '2025-04-22 17:47:29', '');
INSERT INTO `sys_menu` VALUES (2022, '数据监控', 2017, 2, 'http://localhost:8080/druid/login.html', '', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe899;', 'admin', '2025-04-23 13:49:59', 'admin', '2025-04-24 12:56:19', '');
INSERT INTO `sys_menu` VALUES (2023, '定时任务', 2017, 1, 'job', 'monitor/job/index', NULL, '', 1, 0, 'C', '0', '0', '', '&#xe833;', 'admin', '2025-04-23 13:51:27', 'admin', '2025-04-23 13:51:36', '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (100, '代码生成', 6, 'com.ruoyi.generator.controller.GenController.importTableSave()', 'POST', 1, 'admin', NULL, '/tool/gen/importTable', '127.0.0.1', '内网IP', '{\"tables\":\"sys_config\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-10-10 19:21:50', 82);
INSERT INTO `sys_oper_log` VALUES (101, '代码生成', 8, 'com.ruoyi.generator.controller.GenController.batchGenCode()', 'GET', 1, 'admin', NULL, '/tool/gen/batchGenCode', '127.0.0.1', '内网IP', '{\"tables\":\"sys_config\"}', NULL, 0, NULL, '2025-10-10 19:21:52', 144);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-03-30 12:47:25', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-03-30 12:47:26', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-03-30 12:47:26', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-03-30 12:47:26', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2025-10-03 16:25:06', '', NULL, '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime NULL DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-10-10 19:21:38', '2025-10-03 16:25:06', 'admin', '2025-10-03 16:25:06', '', '2025-10-10 19:21:38', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-10-03 16:25:06', '2025-10-03 16:25:06', 'admin', '2025-10-03 16:25:06', '', NULL, '测试员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

SET FOREIGN_KEY_CHECKS = 1;
