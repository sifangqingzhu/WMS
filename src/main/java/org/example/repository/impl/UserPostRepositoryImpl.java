package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.UserPostDao;
import org.example.entity.SysUserPost;
import org.example.mapper.UserPostMapper;
import org.example.repository.UserPostRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户岗位关联 Repository 实现类
 * Repository层负责业务逻辑封装，调用DAO层
 */
@Repository
public class UserPostRepositoryImpl extends ServiceImpl<UserPostMapper, SysUserPost> implements UserPostRepository {

    private final UserPostDao userPostDao;
    private final UserRepository userRepository;

    public UserPostRepositoryImpl(UserPostDao userPostDao, UserRepository userRepository) {
        this.userPostDao = userPostDao;
        this.userRepository = userRepository;
    }

    @Override
    public List<SysUserPost> findByUserId(String userId) {
        return userPostDao.selectByUserId(userId);
    }

    @Override
    public List<SysUserPost> findByPostId(Long postId) {
        return userPostDao.selectByPostId(postId);
    }

    @Override
    public int removeByUserId(String userId) {
        return userPostDao.deleteByUserId(userId);
    }

    @Override
    public int countByPostId(Long postId) {
        return userPostDao.countByPostId(postId);
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    @Transactional
    public void assignPostsToUser(String userId, List<Long> postIds) {
        // 先删除用户原有的岗位关联
        userPostDao.deleteByUserId(userId);
        // 批量插入新的岗位关联
        userPostDao.batchInsert(userId, postIds);
    }
}
