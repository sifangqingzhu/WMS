package org.example.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.dao.UserDao;
import org.example.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户仓储层
 * 使用构造器注入，封装 DAO，完全解耦
 */
@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final UserDao userDao;

    // 构造器注入
    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public SysUser findByUsername(String username) {
        log.debug("Repository: findByUsername, username={}", username);
        try {
            return userDao.selectByUsername(username);
        } catch (Exception e) {
            log.error("Repository: findByUsername failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public SysUser findById(Long id) {
        log.debug("Repository: findById, id={}", id);
        try {
            return userDao.selectById(id);
        } catch (Exception e) {
            log.error("Repository: findById failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public boolean existsByUsername(String username) {
        log.debug("Repository: existsByUsername, username={}", username);
        try {
            return userDao.countByUsername(username) > 0;
        } catch (Exception e) {
            log.error("Repository: existsByUsername failed, error={}", e.getMessage(), e);
            throw new RuntimeException("检查用户名失败", e);
        }
    }

    public boolean save(SysUser user) {
        log.info("Repository: save, username={}", user.getUsername());
        try {
            int result = userDao.insert(user);
            return result > 0;
        } catch (Exception e) {
            log.error("Repository: save failed, error={}", e.getMessage(), e);
            throw new RuntimeException("保存用户失败", e);
        }
    }

    public boolean update(SysUser user) {
        log.info("Repository: update, userId={}", user.getId());
        try {
            int result = userDao.updateById(user);
            return result > 0;
        } catch (Exception e) {
            log.error("Repository: update failed, error={}", e.getMessage(), e);
            throw new RuntimeException("更新用户失败", e);
        }
    }

    public boolean delete(Long id) {
        log.info("Repository: delete, userId={}", id);
        try {
            int result = userDao.deleteById(id);
            return result > 0;
        } catch (Exception e) {
            log.error("Repository: delete failed, error={}", e.getMessage(), e);
            throw new RuntimeException("删除用户失败", e);
        }
    }

    public List<SysUser> findAll() {
        log.debug("Repository: findAll");
        try {
            return userDao.selectAll();
        } catch (Exception e) {
            log.error("Repository: findAll failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public List<SysUser> findByStatus(Integer status) {
        log.debug("Repository: findByStatus, status={}", status);
        try {
            return userDao.selectListByStatus(status);
        } catch (Exception e) {
            log.error("Repository: findByStatus failed, error={}", e.getMessage(), e);
            throw new RuntimeException("查找用户失败", e);
        }
    }

    public IPage<SysUser> findPage(int pageNum, int pageSize) {
        log.debug("Repository: findPage, pageNum={}, pageSize={}", pageNum, pageSize);
        try {
            return userDao.selectPage(pageNum, pageSize);
        } catch (Exception e) {
            log.error("Repository: findPage failed, error={}", e.getMessage(), e);
            throw new RuntimeException("分页查询失败", e);
        }
    }

    public IPage<SysUser> findPageByCondition(int pageNum, int pageSize, Integer status, String keyword) {
        log.debug("Repository: findPageByCondition");
        try {
            return userDao.selectPageWithCondition(pageNum, pageSize, status, keyword);
        } catch (Exception e) {
            log.error("Repository: findPageByCondition failed, error={}", e.getMessage(), e);
            throw new RuntimeException("分页查询失败", e);
        }
    }

    public Long count() {
        log.debug("Repository: count");
        try {
            return userDao.countAll();
        } catch (Exception e) {
            log.error("Repository: count failed, error={}", e.getMessage(), e);
            throw new RuntimeException("统计失败", e);
        }
    }
}
