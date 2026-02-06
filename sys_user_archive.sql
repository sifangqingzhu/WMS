-- =====================================================
-- 用户归档表
-- =====================================================

CREATE TABLE IF NOT EXISTS sys_user_archive (
    user_id VARCHAR(200) NOT NULL,
    user_name VARCHAR(200),
    password VARCHAR(200),
    email VARCHAR(200),
    phone VARCHAR(50),
    name VARCHAR(200),
    company_id VARCHAR(200),
    is_delete SMALLINT DEFAULT 1,
    is_activated SMALLINT DEFAULT 0,
    is_cloud SMALLINT DEFAULT 0,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    archived_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, archived_at)
);

CREATE INDEX idx_user_archive_archived_at ON sys_user_archive(archived_at);

