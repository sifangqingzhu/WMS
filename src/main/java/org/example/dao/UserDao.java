package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        wrapper.eq("username", username);
        return userMapper.selectOne(wrapper);
    }

    public SysUser selectById(Long id) {
        log.debug("DAO: selectById, id={}", id);
        return userMapper.selectById(id);
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
        wrapper.eq("username", username);
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
        log.debug("DAO: insert, username={}", user.getUsername());
        return userMapper.insert(user);
    }

    public int updateById(SysUser user) {
        log.debug("DAO: updateById, id={}", user.getId());
        return userMapper.updateById(user);
    }

    public int deleteById(Long id) {
        log.debug("DAO: deleteById, id={}", id);
        return userMapper.deleteById(id);
    }

    public IPage<SysUser> selectPage(int pageNum, int pageSize) {
        log.debug("DAO: selectPage, pageNum={}, pageSize={}", pageNum, pageSize);
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        return userMapper.selectPage(page, null);
    }

    public IPage<SysUser> selectPageWithCondition(int pageNum, int pageSize, Integer status, String keyword) {
        log.debug("DAO: selectPageWithCondition");
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like("username", keyword).or().like("real_name", keyword));
        }
        wrapper.orderByDesc("created_at");
        return userMapper.selectPage(page, wrapper);
    }
}
