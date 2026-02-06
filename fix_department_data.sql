-- =====================================================
-- 符合dep_tree规则的部门数据
-- 规则: company_id_predecessors_children
-- =====================================================

-- 清空现有数据
TRUNCATE TABLE sys_department CASCADE;

-- 插入符合规则的部门数据
-- 一级部门（根部门）
INSERT INTO sys_department (department_id, department_name, level, dep_tree, company_id, created_at, updated_at) VALUES
(1, '管理部', 1, 'C001_null_null', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749'),
(2, '仓储部', 1, 'C001_null_5,6', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749'),
(3, '采购部', 1, 'C001_null_null', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749'),
(4, '销售部', 1, 'C001_null_7,8', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749');

-- 二级部门（子部门）
INSERT INTO sys_department (department_id, department_name, level, dep_tree, company_id, created_at, updated_at) VALUES
(5, '仓储一组', 2, 'C001_2_null', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749'),
(6, '仓储二组', 2, 'C001_2_null', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749'),
(7, '华北区销售', 2, 'C001_4_null', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749'),
(8, '华东区销售', 2, 'C001_4_null', 'C001', '2026-02-06 02:48:45.861749', '2026-02-06 02:48:45.861749');

-- 重置序列
SELECT setval('sys_department_department_id_seq', (SELECT MAX(department_id) FROM sys_department));

-- 验证数据
SELECT
    department_id,
    department_name,
    level,
    dep_tree,
    company_id,
    CASE
        WHEN level = 1 THEN '根部门'
        WHEN level = 2 THEN '子部门'
        ELSE '多层部门'
    END as department_type
FROM sys_department
ORDER BY company_id, level, department_id;

-- 解释说明：
-- 1. 管理部 (ID=1): C001_null_null
--    - 属于公司C001
--    - 无父部门（根部门）
--    - 无子部门

-- 2. 仓储部 (ID=2): C001_null_5,6
--    - 属于公司C001
--    - 无父部门（根部门）
--    - 有子部门：5（仓储一组）、6（仓储二组）

-- 3. 采购部 (ID=3): C001_null_null
--    - 属于公司C001
--    - 无父部门（根部门）
--    - 无子部门

-- 4. 销售部 (ID=4): C001_null_7,8
--    - 属于公司C001
--    - 无父部门（根部门）
--    - 有子部门：7（华北区销售）、8（华东区销售）

-- 5. 仓储一组 (ID=5): C001_2_null
--    - 属于公司C001
--    - 父部门：2（仓储部）
--    - 无子部门

-- 6. 仓储二组 (ID=6): C001_2_null
--    - 属于公司C001
--    - 父部门：2（仓储部）
--    - 无子部门

-- 7. 华北区销售 (ID=7): C001_4_null
--    - 属于公司C001
--    - 父部门：4（销售部）
--    - 无子部门

-- 8. 华东区销售 (ID=8): C001_4_null
--    - 属于公司C001
--    - 父部门：4（销售部）
--    - 无子部门

