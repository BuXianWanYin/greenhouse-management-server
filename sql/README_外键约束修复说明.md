# 农资资源删除外键约束问题修复说明

## 问题描述

删除农资资源时出现外键约束错误：
```
Cannot delete or update a parent row: a foreign key constraint fails 
(`fish-dish-server`.`agriculture_resource_usage`, CONSTRAINT `fk_usage_resource` 
FOREIGN KEY (`resource_id`) REFERENCES `agriculture_resource` (`resource_id`) 
ON DELETE RESTRICT)
```

## 问题原因

`agriculture_resource_usage` 表的外键约束设置为 `ON DELETE RESTRICT`，导致当存在使用记录时，无法删除关联的农资资源。

## 解决方案

采用**双重保障**方案：

### 1. 数据库层面：修改外键约束为 CASCADE（推荐）

**执行SQL脚本：**
```sql
-- 执行 sql/alter_agriculture_resource_usage_foreign_key.sql
```

这将把外键约束从 `ON DELETE RESTRICT` 改为 `ON DELETE CASCADE`，删除资源时会自动级联删除使用记录。

**优点：**
- 数据库层面保证数据一致性
- 与库存表的外键约束保持一致（库存表已经是 CASCADE）
- 更符合业务逻辑：资源删除后，使用记录也应该删除

### 2. 代码层面：添加联动删除逻辑

在 `AgricultureResourceServiceImpl` 中：
- 重写了 `deleteAgricultureResourceById` 方法，删除资源前先删除使用记录
- 重写了 `removeByIds` 方法，支持批量删除时的关联数据清理
- 使用 `@Transactional` 保证事务一致性

**优点：**
- 即使数据库约束未修改，代码也能正常工作
- 提供了额外的安全保障
- 支持单个和批量删除

## 文件修改清单

### 1. SQL 脚本
- ✅ `sql/alter_agriculture_resource_usage_foreign_key.sql` - 修改外键约束脚本（新建）
- ✅ `sql/create_agriculture_resource_tables.sql` - 更新建表脚本中的外键约束

### 2. Java 代码
- ✅ `server-agriculture/src/main/java/com/server/service/impl/AgricultureResourceServiceImpl.java`
  - 添加了 `AgricultureResourceUsageService` 依赖
  - 修改了 `deleteAgricultureResourceById` 方法，添加联动删除
  - 重写了 `removeByIds` 方法，支持批量删除时的关联数据清理
  - 添加了 `@Transactional` 事务注解

## 使用说明

### 方式一：修改数据库约束（推荐）

1. **执行SQL脚本：**
   ```bash
   # 连接数据库后执行
   source sql/alter_agriculture_resource_usage_foreign_key.sql
   ```
   或直接在数据库客户端执行 `sql/alter_agriculture_resource_usage_foreign_key.sql` 文件中的SQL语句

2. **验证约束修改：**
   ```sql
   SELECT 
       CONSTRAINT_NAME,
       TABLE_NAME,
       DELETE_RULE
   FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
   WHERE TABLE_SCHEMA = DATABASE()
     AND TABLE_NAME = 'agriculture_resource_usage'
     AND CONSTRAINT_NAME = 'fk_usage_resource';
   ```
   应该看到 `DELETE_RULE` 为 `CASCADE`

### 方式二：仅使用代码层面方案

如果无法修改数据库约束，代码层面的联动删除逻辑已经可以解决问题。但建议还是修改数据库约束，以保持与库存表的一致性。

## 注意事项

1. **数据备份**：在执行SQL脚本前，建议备份数据库
2. **测试验证**：修改后请测试删除功能，确保：
   - 单个删除正常工作
   - 批量删除正常工作
   - 关联的使用记录被正确删除
   - 关联的库存记录被正确删除（库存表已经是CASCADE）
3. **事务一致性**：删除操作使用了事务，如果任何步骤失败，整个操作会回滚

## 相关表结构

- `agriculture_resource` - 农资资源表（主表）
- `agriculture_resource_usage` - 农资使用记录表（子表，外键：resource_id）
- `agriculture_resource_inventory` - 农资库存表（子表，外键：resource_id，已设置CASCADE）

## 总结

**推荐做法：**
1. 执行SQL脚本修改数据库外键约束为 CASCADE
2. 代码层面的联动删除逻辑作为额外保障

这样可以确保：
- 数据库层面和代码层面都有保护
- 数据一致性得到双重保障
- 符合业务逻辑：资源删除时，关联数据也应该删除


