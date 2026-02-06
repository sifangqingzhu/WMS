-- ========================================
-- RBAC 权限系统 - 完整表结构
-- ========================================
-- 包含5张表：
-- 1. sys_company (公司表)
-- 2. sys_department (部门表) - 外键: company_id -> sys_company
-- 3. sys_user (用户表) - 外键: company_id -> sys_company
-- 4. sys_post (岗位表) - 外键: department_id -> sys_department
-- 5. sys_user_post (用户岗位关联表) - 外键: user_id, post_id
-- ========================================

-- 删除已存在的表（按依赖顺序）
DROP TABLE IF EXISTS sys_user_post CASCADE;
DROP TABLE IF EXISTS sys_post CASCADE;
DROP TABLE IF EXISTS sys_department CASCADE;
DROP TABLE IF EXISTS sys_user CASCADE;
DROP TABLE IF EXISTS sys_company CASCADE;

-- ========================================
-- 1. 创建 sys_company 表（公司表）
-- ========================================
CREATE TABLE sys_company (
    company_id VARCHAR(200) PRIMARY KEY,
    company_name VARCHAR(200) NOT NULL,
    is_delete SMALLINT NOT NULL DEFAULT 0,
    contry VARCHAR(200),
    province VARCHAR(200),
    city VARCHAR(200),
    address VARCHAR(200),
    link_man VARCHAR(200),
    phone VARCHAR(200),
    company_code VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_company_is_delete CHECK (is_delete IN (0, 1))
);

-- ========================================
-- 2. 创建 sys_department 表（部门表）
-- ========================================
CREATE TABLE sys_department (
    department_id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(200) NOT NULL,
    level SMALLINT,
    dep_tree VARCHAR(200),
    company_id VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_department_company FOREIGN KEY (company_id)
        REFERENCES sys_company(company_id) ON DELETE SET NULL
);

-- ========================================
-- 3. 创建 sys_user 表（用户表）
-- ========================================
CREATE TABLE sys_user (
    user_id VARCHAR(200) PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    name VARCHAR(100),
    company_id VARCHAR(200),
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,
    is_activated BOOLEAN NOT NULL DEFAULT TRUE,
    is_cloud BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL DEFAULT '在职',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_status CHECK (status IN ('在职', '离职')),
    CONSTRAINT fk_user_company FOREIGN KEY (company_id)
        REFERENCES sys_company(company_id) ON DELETE SET NULL
);

-- ========================================
-- 4. 创建 sys_post 表（岗位表）
-- ========================================
CREATE TABLE sys_post (
    post_id BIGSERIAL PRIMARY KEY,
    post_name VARCHAR(200) NOT NULL,
    department_id BIGINT,
    description VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_department FOREIGN KEY (department_id)
        REFERENCES sys_department(department_id) ON DELETE SET NULL
);

-- ========================================
-- 5. 创建 sys_user_post 关联表（用户岗位多对多）
-- ========================================
CREATE TABLE sys_user_post (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(200) NOT NULL,
    post_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_post_user FOREIGN KEY (user_id)
        REFERENCES sys_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_post_post FOREIGN KEY (post_id)
        REFERENCES sys_post(post_id) ON DELETE CASCADE,
    CONSTRAINT uk_user_post UNIQUE (user_id, post_id)
);

-- ========================================
-- 创建索引
-- ========================================

-- sys_company 索引
CREATE INDEX idx_sys_company_name ON sys_company(company_name);
CREATE INDEX idx_sys_company_code ON sys_company(company_code);
CREATE INDEX idx_sys_company_is_delete ON sys_company(is_delete);

-- sys_department 索引
CREATE INDEX idx_sys_department_name ON sys_department(department_name);
CREATE INDEX idx_sys_department_company ON sys_department(company_id);
CREATE INDEX idx_sys_department_level ON sys_department(level);

-- sys_user 索引
CREATE INDEX idx_sys_user_name ON sys_user(user_name);
CREATE INDEX idx_sys_user_company ON sys_user(company_id);
CREATE INDEX idx_sys_user_status ON sys_user(status);
CREATE INDEX idx_sys_user_is_delete ON sys_user(is_delete);
CREATE INDEX idx_sys_user_email ON sys_user(email);

-- sys_post 索引
CREATE INDEX idx_sys_post_name ON sys_post(post_name);
CREATE INDEX idx_sys_post_department ON sys_post(department_id);

-- sys_user_post 索引
CREATE INDEX idx_sys_user_post_user ON sys_user_post(user_id);
CREATE INDEX idx_sys_user_post_post ON sys_user_post(post_id);

-- ========================================
-- 创建更新时间触发器函数（如果不存在）
-- ========================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- ========================================
-- 为表添加更新时间触发器
-- ========================================

CREATE TRIGGER trigger_sys_company_updated_at
    BEFORE UPDATE ON sys_company
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_sys_department_updated_at
    BEFORE UPDATE ON sys_department
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_sys_user_updated_at
    BEFORE UPDATE ON sys_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trigger_sys_post_updated_at
    BEFORE UPDATE ON sys_post
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ========================================
-- 表和字段注释
-- ========================================

-- sys_company 注释
COMMENT ON TABLE sys_company IS '公司表';
COMMENT ON COLUMN sys_company.company_id IS '公司ID（主键）';
COMMENT ON COLUMN sys_company.company_name IS '公司名称';
COMMENT ON COLUMN sys_company.is_delete IS '软删除：0-未删除，1-已删除';
COMMENT ON COLUMN sys_company.contry IS '国家';
COMMENT ON COLUMN sys_company.province IS '省份';
COMMENT ON COLUMN sys_company.city IS '城市';
COMMENT ON COLUMN sys_company.address IS '地址';
COMMENT ON COLUMN sys_company.link_man IS '联系人';
COMMENT ON COLUMN sys_company.phone IS '电话';
COMMENT ON COLUMN sys_company.company_code IS '公司代码';

-- sys_department 注释
COMMENT ON TABLE sys_department IS '部门表';
COMMENT ON COLUMN sys_department.department_id IS '部门ID（主键）';
COMMENT ON COLUMN sys_department.department_name IS '部门名称';
COMMENT ON COLUMN sys_department.level IS '部门层级';
COMMENT ON COLUMN sys_department.dep_tree IS '部门树路径';
COMMENT ON COLUMN sys_department.company_id IS '所属公司ID（外键）';

-- sys_user 注释
COMMENT ON TABLE sys_user IS 'RBAC系统用户表';
COMMENT ON COLUMN sys_user.user_id IS '用户ID（主键）';
COMMENT ON COLUMN sys_user.user_name IS '用户名（唯一）';
COMMENT ON COLUMN sys_user.password IS '密码（BCrypt加密）';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.name IS '姓名';
COMMENT ON COLUMN sys_user.company_id IS '所属公司ID（外键）';
COMMENT ON COLUMN sys_user.is_delete IS '软删除标记';
COMMENT ON COLUMN sys_user.is_activated IS '是否激活';
COMMENT ON COLUMN sys_user.is_cloud IS '是否云端';
COMMENT ON COLUMN sys_user.status IS '状态：在职、离职';

-- sys_post 注释
COMMENT ON TABLE sys_post IS '岗位表';
COMMENT ON COLUMN sys_post.post_id IS '岗位ID（主键）';
COMMENT ON COLUMN sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN sys_post.department_id IS '所属部门ID（外键）';
COMMENT ON COLUMN sys_post.description IS '岗位描述';

-- sys_user_post 注释
COMMENT ON TABLE sys_user_post IS '用户岗位关联表（多对多）';
COMMENT ON COLUMN sys_user_post.id IS '主键ID';
COMMENT ON COLUMN sys_user_post.user_id IS '用户ID（外键）';
COMMENT ON COLUMN sys_user_post.post_id IS '岗位ID（外键）';

-- ========================================
-- 插入测试数据
-- ========================================

-- 插入公司
INSERT INTO sys_company (company_id, company_name, is_delete, contry, province, city, address, link_man, phone, company_code) VALUES
('C001', '总公司', 0, '中国', '广东省', '深圳市', '南山区科技园', '张总', '0755-12345678', 'HQ001'),
('C002', '北京分公司', 0, '中国', '北京市', '北京市', '朝阳区建国路', '李经理', '010-87654321', 'BJ001'),
('C003', '上海分公司', 0, '中国', '上海市', '上海市', '浦东新区陆家嘴', '王经理', '021-98765432', 'SH001');

-- 插入部门
INSERT INTO sys_department (department_name, level, dep_tree, company_id) VALUES
('管理部', 1, '1', 'C001'),
('仓储部', 1, '2', 'C001'),
('采购部', 1, '3', 'C001'),
('销售部', 1, '4', 'C001'),
('仓储一组', 2, '2.1', 'C001'),
('仓储二组', 2, '2.2', 'C001'),
('华北区销售', 2, '4.1', 'C002'),
('华东区销售', 2, '4.2', 'C003');

-- 插入岗位（密码：123456）
INSERT INTO sys_post (post_name, department_id, description) VALUES
('系统管理员', 1, '负责系统管理和维护'),
('仓库管理员', 2, '负责仓库日常管理'),
('采购员', 3, '负责采购业务'),
('销售员', 4, '负责销售业务');

-- 插入用户（密码都是：123456）
INSERT INTO sys_user (user_id, user_name, password, email, phone, name, company_id, status) VALUES
('U001', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@example.com', '13800138000', '系统管理员', 'C001', '在职'),
('U002', 'warehouse', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'warehouse@example.com', '13800138001', '张三', 'C001', '在职'),
('U003', 'buyer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'buyer@example.com', '13800138002', '李四', 'C001', '在职');

-- 插入用户岗位关联
INSERT INTO sys_user_post (user_id, post_id) VALUES
('U001', 1),
('U002', 2),
('U003', 3);

-- ========================================
-- 验证数据
-- ========================================
SELECT '=== 数据验证 ===' as info;

SELECT 'sys_company' as table_name, COUNT(*) as count FROM sys_company
UNION ALL SELECT 'sys_department', COUNT(*) FROM sys_department
UNION ALL SELECT 'sys_post', COUNT(*) FROM sys_post
UNION ALL SELECT 'sys_user', COUNT(*) FROM sys_user
UNION ALL SELECT 'sys_user_post', COUNT(*) FROM sys_user_post;

