-- ========================================
-- RBAC 权限系统 - 部门和公司表
-- ========================================

-- 1. 创建 sys_company 表（公司表）
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

-- 2. 创建 sys_department 表（部门表）
CREATE TABLE sys_department (
    department_id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(200) NOT NULL,
    level SMALLINT,
    dep_tree VARCHAR(200),
    company_id VARCHAR(200),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_department_company FOREIGN KEY (company_id) REFERENCES sys_company(company_id) ON DELETE SET NULL
);

-- 3. 为 sys_post 表添加外键（多对一关系：post -> department）
ALTER TABLE sys_post
ADD CONSTRAINT fk_post_department FOREIGN KEY (department_id) REFERENCES sys_department(department_id) ON DELETE SET NULL;

-- 4. 为 sys_user 表添加外键（多对一关系：user -> company）
ALTER TABLE sys_user
ADD CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES sys_company(company_id) ON DELETE SET NULL;

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

-- ========================================
-- 为表添加更新时间触发器
-- ========================================

-- sys_company 更新触发器
DROP TRIGGER IF EXISTS trigger_sys_company_updated_at ON sys_company;
CREATE TRIGGER trigger_sys_company_updated_at
    BEFORE UPDATE ON sys_company
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- sys_department 更新触发器
DROP TRIGGER IF EXISTS trigger_sys_department_updated_at ON sys_department;
CREATE TRIGGER trigger_sys_department_updated_at
    BEFORE UPDATE ON sys_department
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ========================================
-- 表和字段注释
-- ========================================

-- sys_company 表注释
COMMENT ON TABLE sys_company IS '公司表';
COMMENT ON COLUMN sys_company.company_id IS '公司ID（主键）';
COMMENT ON COLUMN sys_company.company_name IS '公司名称';
COMMENT ON COLUMN sys_company.is_delete IS '软删除标记：0-未删除，1-已删除';
COMMENT ON COLUMN sys_company.contry IS '国家';
COMMENT ON COLUMN sys_company.province IS '省份';
COMMENT ON COLUMN sys_company.city IS '城市';
COMMENT ON COLUMN sys_company.address IS '地址';
COMMENT ON COLUMN sys_company.link_man IS '联系人';
COMMENT ON COLUMN sys_company.phone IS '电话';
COMMENT ON COLUMN sys_company.company_code IS '公司代码';
COMMENT ON COLUMN sys_company.created_at IS '创建时间';
COMMENT ON COLUMN sys_company.updated_at IS '更新时间';

-- sys_department 表注释
COMMENT ON TABLE sys_department IS '部门表';
COMMENT ON COLUMN sys_department.department_id IS '部门ID（主键）';
COMMENT ON COLUMN sys_department.department_name IS '部门名称';
COMMENT ON COLUMN sys_department.level IS '部门层级';
COMMENT ON COLUMN sys_department.dep_tree IS '部门树路径';
COMMENT ON COLUMN sys_department.company_id IS '公司ID（外键）';
COMMENT ON COLUMN sys_department.created_at IS '创建时间';
COMMENT ON COLUMN sys_department.updated_at IS '更新时间';

-- ========================================
-- 插入测试数据
-- ========================================

-- 插入测试公司
INSERT INTO sys_company (company_id, company_name, is_delete, contry, province, city, address, link_man, phone, company_code) VALUES
('C001', '总公司', 0, '中国', '广东省', '深圳市', '南山区科技园', '张总', '0755-12345678', 'HQ001'),
('C002', '北京分公司', 0, '中国', '北京市', '北京市', '朝阳区建国路', '李经理', '010-87654321', 'BJ001'),
('C003', '上海分公司', 0, '中国', '上海市', '上海市', '浦东新区陆家嘴', '王经理', '021-98765432', 'SH001');

-- 插入测试部门
INSERT INTO sys_department (department_name, level, dep_tree, company_id) VALUES
('管理部', 1, '1', 'C001'),
('仓储部', 1, '2', 'C001'),
('采购部', 1, '3', 'C001'),
('销售部', 1, '4', 'C001'),
('仓储一组', 2, '2.1', 'C001'),
('仓储二组', 2, '2.2', 'C001'),
('华北区销售', 2, '4.1', 'C002'),
('华东区销售', 2, '4.2', 'C003');

-- 更新岗位表的部门ID
UPDATE sys_post SET department_id = 1 WHERE post_name = '系统管理员';
UPDATE sys_post SET department_id = 2 WHERE post_name = '仓库管理员';
UPDATE sys_post SET department_id = 3 WHERE post_name = '采购员';
UPDATE sys_post SET department_id = 4 WHERE post_name = '销售员';

-- 更新用户表的公司ID
UPDATE sys_user SET company_id = 1 WHERE user_id = 'U001';
UPDATE sys_user SET company_id = 2 WHERE user_id = 'U002';
UPDATE sys_user SET company_id = 3 WHERE user_id = 'U003';

