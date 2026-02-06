package org.example.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.entity.SysUser;
import org.example.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户仓储层
 * 直接调用 Mapper
 */
@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final UserMapper userMapper;

    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SysUser findByUsername(String username) {
        log.debug("Repository: findByUsername, username={}", username);
        try {
            return userMapper.selectByUsername(username);
        } catch (Exception e) {
            log.error("Repository: findByUsername failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public SysUser findById(String userId) {
        log.debug("Repository: findById, userId={}", userId);
        try {
            return userMapper.selectByUserId(userId);
        } catch (Exception e) {
            log.error("Repository: findById failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public boolean existsByUsername(String username) {
        log.debug("Repository: existsByUsername, username={}", username);
        try {
            return userMapper.countByUsername(username) > 0;
        } catch (Exception e) {
            log.error("Repository: existsByUsername failed, error={}", e.getMessage(), e);
            throw new RuntimeException("检查用户名失败", e);
        }
    }

    public boolean existsById(String userId) {
        log.debug("Repository: existsById, userId={}", userId);
        try {
            return userMapper.countActiveById(userId) > 0;
        } catch (Exception e) {
            log.error("Repository: existsById failed, error={}", e.getMessage(), e);
            throw new RuntimeException("检查用户ID失败", e);
        }
    }

    public boolean save(SysUser user) {
        log.info("Repository: save, userName={}", user.getUserName());
        try {
            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            log.error("Repository: save failed, error={}", e.getMessage(), e);
            throw new RuntimeException("保存用户失败", e);
        }
    }

    public boolean update(SysUser user) {
        log.info("Repository: update, userId={}", user.getUserId());
        try {
            int result = userMapper.updateById(user);
            return result > 0;
        } catch (Exception e) {
            log.error("Repository: update failed, error={}", e.getMessage(), e);
            throw new RuntimeException("更新用户失败", e);
        }
    }

    public boolean delete(String userId) {
        log.info("Repository: delete, userId={}", userId);
        try {
            int result = userMapper.deleteByUserId(userId);
            return result > 0;
        } catch (Exception e) {
            log.error("Repository: delete failed, error={}", e.getMessage(), e);
            throw new RuntimeException("删除用户失败", e);
        }
    }

    public List<SysUser> findAll() {
        log.debug("Repository: findAll");
        try {
            return userMapper.selectList(null);
        } catch (Exception e) {
            log.error("Repository: findAll failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public List<SysUser> findByStatus(String status) {
        log.debug("Repository: findByStatus, status={}", status);
        try {
            return userMapper.selectByStatus(status);
        } catch (Exception e) {
            log.error("Repository: findByStatus failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public IPage<SysUser> findPage(int pageNum, int pageSize) {
        log.debug("Repository: findPage, pageNum={}, pageSize={}", pageNum, pageSize);
        try {
            Page<SysUser> page = new Page<>(pageNum, pageSize);
            return userMapper.selectPage(page, null);
        } catch (Exception e) {
            log.error("Repository: findPage failed, error={}", e.getMessage(), e);
            throw new RuntimeException("分页查询失败", e);
        }
    }

    public IPage<SysUser> findPageByCondition(int pageNum, int pageSize, String status, String keyword) {
        log.debug("Repository: findPageByCondition");
        try {
            Page<SysUser> page = new Page<>(pageNum, pageSize);
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            if (status != null && !status.isEmpty()) {
                wrapper.eq("status", status);
            }
            if (keyword != null && !keyword.isEmpty()) {
                wrapper.and(w -> w.like("user_name", keyword).or().like("name", keyword));
            }
            return userMapper.selectPage(page, wrapper);
        } catch (Exception e) {
            log.error("Repository: findPageByCondition failed, error={}", e.getMessage(), e);
            throw new RuntimeException("分页查询失败", e);
        }
    }

    public Long count() {
        log.debug("Repository: count");
        try {
            return userMapper.selectCount(null);
        } catch (Exception e) {
            log.error("Repository: count failed, error={}", e.getMessage(), e);
            throw new RuntimeException("统计失败", e);
        }
    }
}
