-- ========================================
-- RBAC 权限系统表结构
-- ========================================

-- 1. 删除已存在的表（如果需要重建）
DROP TABLE IF EXISTS sys_user_post CASCADE;
DROP TABLE IF EXISTS sys_post CASCADE;
DROP TABLE IF EXISTS sys_user CASCADE;

-- 2. 创建 sys_user 表
CREATE TABLE sys_user (
    user_id VARCHAR(200) PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    name VARCHAR(100),
    company_id BIGINT,
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,
    is_activated BOOLEAN NOT NULL DEFAULT TRUE,
    is_cloud BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL DEFAULT '在职',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_status CHECK (status IN ('在职', '离职'))
);

-- 3. 创建 sys_post 表
CREATE TABLE sys_post (
    post_id BIGSERIAL PRIMARY KEY,
    post_name VARCHAR(200) NOT NULL,
    department_id BIGINT,
    description VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. 创建 sys_user_post 关联表
CREATE TABLE sys_user_post (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(200) NOT NULL,
    post_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_post_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_post_post FOREIGN KEY (post_id) REFERENCES sys_post(post_id) ON DELETE CASCADE,
    CONSTRAINT uk_user_post UNIQUE (user_id, post_id)
);

-- ========================================
-- 创建索引
-- ========================================

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
-- 创建更新时间触发器函数
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

-- sys_user 更新触发器
DROP TRIGGER IF EXISTS trigger_sys_user_updated_at ON sys_user;
CREATE TRIGGER trigger_sys_user_updated_at
    BEFORE UPDATE ON sys_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- sys_post 更新触发器
DROP TRIGGER IF EXISTS trigger_sys_post_updated_at ON sys_post;
CREATE TRIGGER trigger_sys_post_updated_at
    BEFORE UPDATE ON sys_post
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ========================================
-- 表和字段注释
-- ========================================

-- sys_user 表注释
COMMENT ON TABLE sys_user IS 'RBAC系统用户表';
COMMENT ON COLUMN sys_user.user_id IS '用户ID（主键）';
COMMENT ON COLUMN sys_user.user_name IS '用户名（唯一）';
COMMENT ON COLUMN sys_user.password IS '密码（BCrypt加密）';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.name IS '姓名';
COMMENT ON COLUMN sys_user.company_id IS '上级部门ID';
COMMENT ON COLUMN sys_user.is_delete IS '软删除标记';
COMMENT ON COLUMN sys_user.is_activated IS '是否激活';
COMMENT ON COLUMN sys_user.is_cloud IS '是否云端';
COMMENT ON COLUMN sys_user.status IS '状态：在职、离职';
COMMENT ON COLUMN sys_user.created_at IS '创建时间';
COMMENT ON COLUMN sys_user.updated_at IS '更新时间';

-- sys_post 表注释
COMMENT ON TABLE sys_post IS '岗位表';
COMMENT ON COLUMN sys_post.post_id IS '岗位ID（主键）';
COMMENT ON COLUMN sys_post.post_name IS '岗位名称';
COMMENT ON COLUMN sys_post.department_id IS '部门ID';
COMMENT ON COLUMN sys_post.description IS '岗位描述';
COMMENT ON COLUMN sys_post.created_at IS '创建时间';
COMMENT ON COLUMN sys_post.updated_at IS '更新时间';

-- sys_user_post 表注释
COMMENT ON TABLE sys_user_post IS '用户岗位关联表';
COMMENT ON COLUMN sys_user_post.id IS '主键ID';
COMMENT ON COLUMN sys_user_post.user_id IS '用户ID（外键）';
COMMENT ON COLUMN sys_user_post.post_id IS '岗位ID（外键）';
COMMENT ON COLUMN sys_user_post.created_at IS '创建时间';

-- ========================================
-- 插入测试数据
-- ========================================

-- 插入测试岗位
INSERT INTO sys_post (post_name, department_id, description) VALUES
('系统管理员', 1, '负责系统管理和维护'),
('仓库管理员', 2, '负责仓库日常管理'),
('采购员', 3, '负责采购业务'),
('销售员', 4, '负责销售业务');

-- 插入测试用户（密码为 123456 的BCrypt加密）
INSERT INTO sys_user (user_id, user_name, password, email, phone, name, company_id, status) VALUES
('U001', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@example.com', '13800138000', '系统管理员', 1, '在职'),
('U002', 'warehouse', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'warehouse@example.com', '13800138001', '张三', 2, '在职'),
('U003', 'buyer', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'buyer@example.com', '13800138002', '李四', 3, '在职');

-- 插入用户岗位关联
INSERT INTO sys_user_post (user_id, post_id) VALUES
('U001', 1),
('U002', 2),
('U003', 3);

