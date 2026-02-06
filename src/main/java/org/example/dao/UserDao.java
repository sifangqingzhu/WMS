package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.annotation.RestoreUser;
import org.example.annotation.SoftDelete;
import org.example.entity.SysUser;
import org.example.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户 DAO 层
 * 使用构造器注入，完全解耦
 */
@Component
public class UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private final UserMapper userMapper;

    // 构造器注入
    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SysUser selectByUsername(String username) {
        log.debug("DAO: selectByUsername, username={}", username);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        return userMapper.selectOne(wrapper);
    }

    public SysUser selectById(String userId) {
        log.debug("DAO: selectById, userId={}", userId);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return userMapper.selectOne(wrapper);
    }

    public List<SysUser> selectAll() {
        log.debug("DAO: selectAll");
        return userMapper.selectList(null);
    }

    public List<SysUser> selectListByStatus(Integer status) {
        log.debug("DAO: selectListByStatus, status={}", status);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return userMapper.selectList(wrapper);
    }

    public Long countByUsername(String username) {
        log.debug("DAO: countByUsername, username={}", username);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        return userMapper.selectCount(wrapper);
    }

    public Long countAll() {
        log.debug("DAO: countAll");
        return userMapper.selectCount(null);
    }

    public Long countByStatus(Integer status) {
        log.debug("DAO: countByStatus, status={}", status);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return userMapper.selectCount(wrapper);
    }

    public int insert(SysUser user) {
        log.debug("DAO: insert, userName={}", user.getUserName());
        return userMapper.insert(user);
    }

    public int updateById(SysUser user) {
        log.debug("DAO: updateById, userId={}", user.getUserId());
        return userMapper.updateById(user);
    }

    /**
     * 删除用户（物理删除）
     */
    public int deleteById(String userId) {
        log.debug("DAO: deleteById, userId={}", userId);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return userMapper.delete(wrapper);
    }

    /**
     * 软删除用户（归档）
     * 使用@SoftDelete注解，切面自动处理归档逻辑
     */
    @SoftDelete
    public boolean softDeleteById(String userId) {
        log.debug("DAO: softDeleteById, userId={}", userId);
        return false; // 切面处理
    }

    /**
     * 恢复用户（从归档表恢复）
     * 使用@RestoreUser注解，切面自动处理恢复逻辑
     */
    @RestoreUser
    public boolean restoreById(String userId) {
        log.debug("DAO: restoreById, userId={}", userId);
        return false; // 切面处理
    }

    public IPage<SysUser> selectPage(int pageNum, int pageSize) {
        log.debug("DAO: selectPage, pageNum={}, pageSize={}", pageNum, pageSize);
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        return userMapper.selectPage(page, null);
    }

    public IPage<SysUser> selectPageWithCondition(int pageNum, int pageSize, String status, String keyword) {
        log.debug("DAO: selectPageWithCondition");
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like("user_name", keyword).or().like("name", keyword));
        }
        wrapper.eq("is_delete", 0);
        wrapper.orderByDesc("created_at");
        return userMapper.selectPage(page, wrapper);
    }

    /**
     * 检查用户是否存在（根据user_id）
     */
    public boolean existsById(String userId) {
        log.debug("DAO: existsById, userId={}", userId);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("is_delete", 0);
        return userMapper.selectCount(wrapper) > 0;
    }

    /**
     * 根据用户ID查询（String类型）
     */
    public SysUser selectByUserId(String userId) {
        log.debug("DAO: selectByUserId, userId={}", userId);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("is_delete", 0);
        return userMapper.selectOne(wrapper);
    }
}
