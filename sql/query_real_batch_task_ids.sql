-- 查询真实的批次ID和任务ID（用于参考）
-- 此脚本用于查询数据库中实际存在的批次和任务ID

-- 查询所有批次ID
SELECT 
    batch_id AS '批次ID',
    batch_name AS '批次名称',
    status AS '状态',
    create_time AS '创建时间'
FROM agriculture_crop_batch
WHERE del_flag = '0'
ORDER BY batch_id;

-- 查询所有任务ID及对应的批次ID
SELECT 
    task_id AS '任务ID',
    batch_id AS '批次ID',
    task_name AS '任务名称',
    responsible_person_name AS '负责人姓名',
    status AS '状态',
    create_time AS '创建时间'
FROM agriculture_batch_task
WHERE del_flag = '0'
ORDER BY batch_id, task_id;

-- 查询批次和任务的关联关系
SELECT 
    b.batch_id AS '批次ID',
    b.batch_name AS '批次名称',
    t.task_id AS '任务ID',
    t.task_name AS '任务名称',
    t.responsible_person_name AS '任务负责人',
    t.status AS '任务状态'
FROM agriculture_crop_batch b
LEFT JOIN agriculture_batch_task t ON b.batch_id = t.batch_id AND t.del_flag = '0'
WHERE b.del_flag = '0'
ORDER BY b.batch_id, t.task_id;

-- 查询所有用户ID和昵称（用于参考）
SELECT 
    user_id AS '用户ID',
    user_name AS '用户账号',
    nick_name AS '用户昵称',
    dept_id AS '部门ID'
FROM sys_user
WHERE del_flag = '0'
ORDER BY user_id;

-- 查询物资管理部和种植管理部的用户
SELECT 
    user_id AS '用户ID',
    user_name AS '用户账号',
    nick_name AS '用户昵称',
    dept_id AS '部门ID'
FROM sys_user
WHERE del_flag = '0' 
  AND (dept_id = 103 OR dept_id = 101)  -- 103=物资管理部, 101=种植管理部
ORDER BY dept_id, user_id;
