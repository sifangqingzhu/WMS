-- =====================================================
-- 邻接表优化 - 删除 dep_tree 和 menu_tree 字段
-- 使用 parent_id 作为唯一的层级关系表达
-- =====================================================

-- 1. 修改 sys_department 表（删除 dep_tree）
ALTER TABLE sys_department DROP COLUMN IF EXISTS dep_tree;

-- 2. 修改 sys_menu 表（删除 menu_tree）
ALTER TABLE sys_menu DROP COLUMN IF EXISTS menu_tree;

-- 3. 确保 parent_id 索引存在
CREATE INDEX IF NOT EXISTS idx_sys_department_parent_id ON sys_department(parent_id);
CREATE INDEX IF NOT EXISTS idx_sys_menu_parent_id ON sys_menu(parent_id);

-- =====================================================
-- 常用查询示例（PostgreSQL）
-- =====================================================

-- 查询某节点的父节点
-- SELECT * FROM sys_menu WHERE menu_id = (SELECT parent_id FROM sys_menu WHERE menu_id = ?);

-- 查询某节点的直接子节点
-- SELECT * FROM sys_menu WHERE parent_id = ? ORDER BY sort_order;

-- 查询某节点的兄弟节点
-- SELECT * FROM sys_menu WHERE parent_id = (SELECT parent_id FROM sys_menu WHERE menu_id = ?) AND menu_id != ?;

-- 查询某节点的所有祖先（递归）
-- WITH RECURSIVE ancestors AS (
--     SELECT * FROM sys_menu WHERE menu_id = (SELECT parent_id FROM sys_menu WHERE menu_id = ?)
--     UNION ALL
--     SELECT m.* FROM sys_menu m JOIN ancestors a ON m.menu_id = a.parent_id
-- )
-- SELECT * FROM ancestors;

-- 查询某节点的所有子孙（递归）
-- WITH RECURSIVE descendants AS (
--     SELECT * FROM sys_menu WHERE parent_id = ?
--     UNION ALL
--     SELECT m.* FROM sys_menu m JOIN descendants d ON m.parent_id = d.menu_id
-- )
-- SELECT * FROM descendants;

-- =====================================================
-- 如果是全新建表，使用以下语句
-- =====================================================

-- 创建 sys_department 表（无 dep_tree）
-- CREATE TABLE IF NOT EXISTS sys_department (
--     department_id BIGSERIAL PRIMARY KEY,
--     department_name VARCHAR(200) NOT NULL,
--     company_id BIGINT NOT NULL,
--     parent_id BIGINT,
--     level INTEGER DEFAULT 1,
--     description VARCHAR(500),
--     is_delete BOOLEAN DEFAULT false,
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_department_company FOREIGN KEY (company_id) REFERENCES sys_company(company_id) ON DELETE CASCADE,
--     CONSTRAINT fk_department_parent FOREIGN KEY (parent_id) REFERENCES sys_department(department_id) ON DELETE SET NULL
-- );

-- 创建 sys_menu 表（无 menu_tree）
-- CREATE TABLE IF NOT EXISTS sys_menu (
--     menu_id BIGSERIAL PRIMARY KEY,
--     menu_name VARCHAR(200) NOT NULL,
--     menu_type SMALLINT NOT NULL DEFAULT 1,
--     menu_code VARCHAR(200) NOT NULL UNIQUE,
--     menu_path VARCHAR(200),
--     menu_component VARCHAR(200),
--     menu_icon VARCHAR(100),
--     parent_id BIGINT,
--     sort_order INTEGER DEFAULT 0,
--     is_visible BOOLEAN DEFAULT true,
--     description VARCHAR(500),
--     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_menu_parent FOREIGN KEY (parent_id) REFERENCES sys_menu(menu_id) ON DELETE SET NULL
-- );
