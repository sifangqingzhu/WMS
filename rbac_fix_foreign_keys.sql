-- ========================================
-- 修正表结构和外键关系
-- ========================================

-- 1. 先删除现有的外键约束（如果存在）
ALTER TABLE sys_post DROP CONSTRAINT IF EXISTS fk_post_department;
ALTER TABLE sys_user DROP CONSTRAINT IF EXISTS fk_user_company;

-- 2. 修改 sys_user 表的 company_id 类型为 VARCHAR(200)
ALTER TABLE sys_user ALTER COLUMN company_id TYPE VARCHAR(200);

-- 3. 现在可以添加外键约束了
ALTER TABLE sys_post
ADD CONSTRAINT fk_post_department FOREIGN KEY (department_id) REFERENCES sys_department(department_id) ON DELETE SET NULL;

ALTER TABLE sys_user
ADD CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES sys_company(company_id) ON DELETE SET NULL;

-- 4. 更新用户表的公司ID为字符串
UPDATE sys_user SET company_id = 'C001' WHERE user_id = 'U001';
UPDATE sys_user SET company_id = 'C001' WHERE user_id = 'U002';
UPDATE sys_user SET company_id = 'C001' WHERE user_id = 'U003';

-- 验证外键关系
SELECT
    'sys_post -> sys_department' as relation,
    COUNT(*) as count
FROM sys_post p
LEFT JOIN sys_department d ON p.department_id = d.department_id

UNION ALL

SELECT
    'sys_department -> sys_company' as relation,
    COUNT(*) as count
FROM sys_department d
LEFT JOIN sys_company c ON d.company_id = c.company_id

UNION ALL

SELECT
    'sys_user -> sys_company' as relation,
    COUNT(*) as count
FROM sys_user u
LEFT JOIN sys_company c ON u.company_id = c.company_id;

