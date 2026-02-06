-- =====================================================
-- WMS 仓库管理系统 - 完整建表SQL
-- 执行顺序：按照表依赖关系排序
-- =====================================================

-- 创建更新时间触发器函数（全局使用）
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- =====================================================
-- 1. 创建 sys_company 表（公司表）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_company (
    company_id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(200) NOT NULL,
    company_code VARCHAR(100),
    parent_id BIGINT,
    company_tree VARCHAR(500),
    level INTEGER DEFAULT 1,
    description VARCHAR(500),
    is_delete BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_company_parent FOREIGN KEY (parent_id) REFERENCES sys_company(company_id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_sys_company_parent_id ON sys_company(parent_id);
CREATE INDEX IF NOT EXISTS idx_sys_company_code ON sys_company(company_code);

DROP TRIGGER IF EXISTS update_sys_company_updated_at ON sys_company;
CREATE TRIGGER update_sys_company_updated_at
    BEFORE UPDATE ON sys_company
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sys_company IS '公司表';
COMMENT ON COLUMN sys_company.company_id IS '公司ID';
COMMENT ON COLUMN sys_company.company_name IS '公司名称';
COMMENT ON COLUMN sys_company.company_code IS '公司编码';
COMMENT ON COLUMN sys_company.parent_id IS '父公司ID';
COMMENT ON COLUMN sys_company.company_tree IS '公司层级树';
COMMENT ON COLUMN sys_company.level IS '层级';
COMMENT ON COLUMN sys_company.is_delete IS '软删除标记';

-- =====================================================
-- 2. 创建 sys_department 表（部门表）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_department (
    department_id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(200) NOT NULL,
    company_id BIGINT NOT NULL,
    parent_id BIGINT,
    dep_tree VARCHAR(500),
    level INTEGER DEFAULT 1,
    description VARCHAR(500),
    is_delete BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_department_company FOREIGN KEY (company_id) REFERENCES sys_company(company_id) ON DELETE CASCADE,
    CONSTRAINT fk_department_parent FOREIGN KEY (parent_id) REFERENCES sys_department(department_id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_sys_department_company_id ON sys_department(company_id);
CREATE INDEX IF NOT EXISTS idx_sys_department_parent_id ON sys_department(parent_id);

DROP TRIGGER IF EXISTS update_sys_department_updated_at ON sys_department;
CREATE TRIGGER update_sys_department_updated_at
    BEFORE UPDATE ON sys_department
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sys_department IS '部门表';
COMMENT ON COLUMN sys_department.department_id IS '部门ID';
COMMENT ON COLUMN sys_department.department_name IS '部门名称';
COMMENT ON COLUMN sys_department.company_id IS '所属公司ID';
COMMENT ON COLUMN sys_department.parent_id IS '父部门ID';
COMMENT ON COLUMN sys_department.dep_tree IS '部门层级树：predecessors_children';
COMMENT ON COLUMN sys_department.level IS '层级';
COMMENT ON COLUMN sys_department.is_delete IS '软删除标记';

-- =====================================================
-- 3. 创建 sys_post 表（岗位表）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_post (
    post_id BIGSERIAL PRIMARY KEY,
    post_name VARCHAR(200) NOT NULL,
    department_id BIGINT NOT NULL,
    post_code VARCHAR(100),
    description VARCHAR(500),
    is_delete BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_department FOREIGN KEY (department_id) REFERENCES sys_department(department_id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sys_post_department_id ON sys_post(department_id);
CREATE INDEX IF NOT EXISTS idx_sys_post_code ON sys_post(post_code);

DROP TRIGGER IF EXISTS update_sys_post_updated_at ON sys_post;
CREATE TRIGGER update_sys_post_updated_at
    BEFORE UPDATE ON sys_post
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sys_post IS '岗位表';
COMMENT ON COLUMN sys_post.post_id IS '岗位ID';
COMMENT ON COLUMN sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN sys_post.department_id IS '所属部门ID';
COMMENT ON COLUMN sys_post.post_code IS '岗位编码';
COMMENT ON COLUMN sys_post.description IS '描述';
COMMENT ON COLUMN sys_post.is_delete IS '软删除标记';

-- =====================================================
-- 4. 创建 sys_user 表（用户表）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user (
    user_id VARCHAR(200) PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    company_id BIGINT,
    status VARCHAR(20) DEFAULT '在职',
    is_delete BOOLEAN DEFAULT false,
    is_activated BOOLEAN DEFAULT false,
    is_cloud BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES sys_company(company_id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_sys_user_username ON sys_user(user_name);
CREATE INDEX IF NOT EXISTS idx_sys_user_company_id ON sys_user(company_id);

DROP TRIGGER IF EXISTS update_sys_user_updated_at ON sys_user;
CREATE TRIGGER update_sys_user_updated_at
    BEFORE UPDATE ON sys_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sys_user IS '系统用户表';
COMMENT ON COLUMN sys_user.user_id IS '用户ID（UUID）';
COMMENT ON COLUMN sys_user.user_name IS '用户名（登录名）';
COMMENT ON COLUMN sys_user.password IS '密码（BCrypt加密）';
COMMENT ON COLUMN sys_user.name IS '姓名';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.company_id IS '所属公司ID';
COMMENT ON COLUMN sys_user.status IS '状态：在职、离职';
COMMENT ON COLUMN sys_user.is_delete IS '软删除标记';
COMMENT ON COLUMN sys_user.is_activated IS '是否激活';
COMMENT ON COLUMN sys_user.is_cloud IS '是否云端用户';

-- =====================================================
-- 5. 创建 sys_user_archive 表（用户归档表/离职记录）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user_archive (
    archive_id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(200) NOT NULL,
    user_name VARCHAR(50),
    name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    company_id BIGINT,
    archive_reason VARCHAR(500),
    archived_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    archived_by VARCHAR(200)
);

CREATE INDEX IF NOT EXISTS idx_sys_user_archive_user_id ON sys_user_archive(user_id);

COMMENT ON TABLE sys_user_archive IS '用户归档表';
COMMENT ON COLUMN sys_user_archive.archive_id IS '归档ID';
COMMENT ON COLUMN sys_user_archive.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_archive.archive_reason IS '归档原因';
COMMENT ON COLUMN sys_user_archive.archived_at IS '归档时间';
COMMENT ON COLUMN sys_user_archive.archived_by IS '操作人';

-- =====================================================
-- 6. 创建 sys_user_post 表（用户-岗位关联表）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user_post (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(200) NOT NULL,
    post_id BIGINT NOT NULL,
    is_primary BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_post_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_post_post FOREIGN KEY (post_id) REFERENCES sys_post(post_id) ON DELETE CASCADE,
    CONSTRAINT uk_user_post UNIQUE (user_id, post_id)
);

CREATE INDEX IF NOT EXISTS idx_sys_user_post_user_id ON sys_user_post(user_id);
CREATE INDEX IF NOT EXISTS idx_sys_user_post_post_id ON sys_user_post(post_id);

COMMENT ON TABLE sys_user_post IS '用户-岗位关联表';
COMMENT ON COLUMN sys_user_post.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_post.post_id IS '岗位ID';
COMMENT ON COLUMN sys_user_post.is_primary IS '是否主岗位';

-- =====================================================
-- 7. 创建 sys_menu 表（菜单/权限表）
-- menu_type: 1-菜单 2-页面 3-按钮 4-字段
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_menu (
    menu_id BIGSERIAL PRIMARY KEY,
    menu_name VARCHAR(200) NOT NULL,
    menu_type SMALLINT NOT NULL DEFAULT 1,
    menu_code VARCHAR(200) NOT NULL UNIQUE,
    menu_path VARCHAR(200),
    menu_component VARCHAR(200),
    menu_icon VARCHAR(100),
    menu_tree VARCHAR(500),
    parent_id BIGINT,
    sort_order INTEGER DEFAULT 0,
    is_visible BOOLEAN DEFAULT true,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_menu_parent FOREIGN KEY (parent_id) REFERENCES sys_menu(menu_id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_sys_menu_parent_id ON sys_menu(parent_id);
CREATE INDEX IF NOT EXISTS idx_sys_menu_code ON sys_menu(menu_code);
CREATE INDEX IF NOT EXISTS idx_sys_menu_type ON sys_menu(menu_type);

DROP TRIGGER IF EXISTS update_sys_menu_updated_at ON sys_menu;
CREATE TRIGGER update_sys_menu_updated_at
    BEFORE UPDATE ON sys_menu
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE sys_menu IS '系统菜单/权限表';
COMMENT ON COLUMN sys_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.menu_type IS '类型：1-菜单 2-页面 3-按钮 4-字段';
COMMENT ON COLUMN sys_menu.menu_code IS '权限代码，如 system:user:delete';
COMMENT ON COLUMN sys_menu.menu_path IS '前端路由路径，如 /system/user';
COMMENT ON COLUMN sys_menu.menu_component IS '前端组件路径，如 system/user/index';
COMMENT ON COLUMN sys_menu.menu_icon IS '菜单图标';
COMMENT ON COLUMN sys_menu.menu_tree IS '菜单层级树，格式：predecessors_children';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单ID';
COMMENT ON COLUMN sys_menu.sort_order IS '排序号';
COMMENT ON COLUMN sys_menu.is_visible IS '是否可见';

-- =====================================================
-- 8. 创建 sys_post_menu 表（岗位-菜单权限关联表）
-- perm_type: 1-只读 2-读写 3-完全控制
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_post_menu (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    perm_type SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_menu_post FOREIGN KEY (post_id) REFERENCES sys_post(post_id) ON DELETE CASCADE,
    CONSTRAINT fk_post_menu_menu FOREIGN KEY (menu_id) REFERENCES sys_menu(menu_id) ON DELETE CASCADE,
    CONSTRAINT uk_post_menu UNIQUE (post_id, menu_id)
);

CREATE INDEX IF NOT EXISTS idx_sys_post_menu_post_id ON sys_post_menu(post_id);
CREATE INDEX IF NOT EXISTS idx_sys_post_menu_menu_id ON sys_post_menu(menu_id);

COMMENT ON TABLE sys_post_menu IS '岗位-菜单权限关联表';
COMMENT ON COLUMN sys_post_menu.post_id IS '岗位ID';
COMMENT ON COLUMN sys_post_menu.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_post_menu.perm_type IS '权限类型：1-只读 2-读写 3-完全控制';

-- =====================================================
-- 初始化基础菜单数据
-- =====================================================
INSERT INTO sys_menu (menu_name, menu_type, menu_code, menu_path, menu_component, menu_icon, parent_id, sort_order, is_visible, description, menu_tree) VALUES
-- 系统管理菜单（ID=1）
('系统管理', 1, 'system', '/system', 'Layout', 'setting', NULL, 1, true, '系统管理目录', 'null_'),
-- 系统管理子页面（parent_id=1）
('用户管理', 2, 'system:user', '/system/user', 'system/user/index', 'user', 1, 1, true, '用户管理页面', '1_'),
('角色管理', 2, 'system:role', '/system/role', 'system/role/index', 'peoples', 1, 2, true, '角色管理页面', '1_'),
('菜单管理', 2, 'system:menu', '/system/menu', 'system/menu/index', 'tree-table', 1, 3, true, '菜单管理页面', '1_'),
('部门管理', 2, 'system:dept', '/system/dept', 'system/dept/index', 'tree', 1, 4, true, '部门管理页面', '1_'),
('岗位管理', 2, 'system:post', '/system/post', 'system/post/index', 'post', 1, 5, true, '岗位管理页面', '1_'),

-- 用户管理按钮权限（parent_id=2）
('用户查询', 3, 'system:user:query', NULL, NULL, NULL, 2, 1, true, '用户查询按钮', '1,2_'),
('用户新增', 3, 'system:user:add', NULL, NULL, NULL, 2, 2, true, '用户新增按钮', '1,2_'),
('用户修改', 3, 'system:user:edit', NULL, NULL, NULL, 2, 3, true, '用户修改按钮', '1,2_'),
('用户删除', 3, 'system:user:delete', NULL, NULL, NULL, 2, 4, true, '用户删除按钮', '1,2_'),
('用户导出', 3, 'system:user:export', NULL, NULL, NULL, 2, 5, true, '用户导出按钮', '1,2_'),

-- 用户字段权限（parent_id=2）
('手机号查看', 4, 'system:user:phone:view', NULL, NULL, NULL, 2, 10, true, '查看用户手机号', '1,2_'),
('手机号编辑', 4, 'system:user:phone:edit', NULL, NULL, NULL, 2, 11, true, '编辑用户手机号', '1,2_'),
('邮箱查看', 4, 'system:user:email:view', NULL, NULL, NULL, 2, 12, true, '查看用户邮箱', '1,2_'),
('邮箱编辑', 4, 'system:user:email:edit', NULL, NULL, NULL, 2, 13, true, '编辑用户邮箱', '1,2_'),

-- 仓库管理菜单（ID会根据实际插入顺序确定）
('仓库管理', 1, 'warehouse', '/warehouse', 'Layout', 'component', NULL, 2, true, '仓库管理目录', 'null_')
ON CONFLICT (menu_code) DO NOTHING;

-- 仓库管理子菜单（需要在主菜单插入后单独处理）
-- 获取仓库管理的menu_id后插入
DO $$
DECLARE
    warehouse_id BIGINT;
BEGIN
    SELECT menu_id INTO warehouse_id FROM sys_menu WHERE menu_code = 'warehouse';
    IF warehouse_id IS NOT NULL THEN
        INSERT INTO sys_menu (menu_name, menu_type, menu_code, menu_path, menu_component, menu_icon, parent_id, sort_order, is_visible, description, menu_tree) VALUES
        ('仓库列表', 2, 'warehouse:list', '/warehouse/list', 'warehouse/list/index', 'list', warehouse_id, 1, true, '仓库列表页面', warehouse_id || '_'),
        ('库存管理', 2, 'warehouse:stock', '/warehouse/stock', 'warehouse/stock/index', 'shopping', warehouse_id, 2, true, '库存管理页面', warehouse_id || '_'),
        ('出入库记录', 2, 'warehouse:record', '/warehouse/record', 'warehouse/record/index', 'documentation', warehouse_id, 3, true, '出入库记录页面', warehouse_id || '_')
        ON CONFLICT (menu_code) DO NOTHING;
    END IF;
END $$;

