package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.SysUserArchive;
import org.example.mapper.UserArchiveMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户归档表 DAO 层
 */
@Component
public class UserArchiveDao {

    private static final Logger log = LoggerFactory.getLogger(UserArchiveDao.class);

    private final UserArchiveMapper userArchiveMapper;

    public UserArchiveDao(UserArchiveMapper userArchiveMapper) {
        this.userArchiveMapper = userArchiveMapper;
    }

    /**
     * 插入归档记录
     */
    public int insert(SysUserArchive archive) {
        log.debug("DAO: insert archive, userId={}", archive.getUserId());
        return userArchiveMapper.insert(archive);
    }

    /**
     * 根据用户ID查询归档记录
     */
    public List<SysUserArchive> selectByUserId(String userId) {
        log.debug("DAO: selectByUserId, userId={}", userId);
        return userArchiveMapper.selectByUserId(userId);
    }

    /**
     * 查询所有归档记录
     */
    public List<SysUserArchive> selectAll() {
        log.debug("DAO: selectAll");
        return userArchiveMapper.selectList(null);
    }

    /**
     * 删除用户的归档记录
     */
    public int deleteByUserId(String userId) {
        log.debug("DAO: deleteByUserId, userId={}", userId);
        return userArchiveMapper.deleteByUserId(userId);
    }

    /**
     * 检查用户是否有归档记录
     */
    public boolean existsByUserId(String userId) {
        log.debug("DAO: existsByUserId, userId={}", userId);
        QueryWrapper<SysUserArchive> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return userArchiveMapper.selectCount(wrapper) > 0;
    }
}

