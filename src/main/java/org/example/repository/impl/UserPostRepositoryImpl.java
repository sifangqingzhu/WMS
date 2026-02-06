package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.SysUserPost;
import org.example.mapper.UserPostMapper;
import org.example.repository.UserPostRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户岗位关联 Repository 实现类
 * 直接调用 Mapper 层
 */
@Repository
public class UserPostRepositoryImpl extends ServiceImpl<UserPostMapper, SysUserPost> implements UserPostRepository {

    private final UserPostMapper userPostMapper;
    private final UserRepository userRepository;

    public UserPostRepositoryImpl(UserPostMapper userPostMapper, UserRepository userRepository) {
        this.userPostMapper = userPostMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<SysUserPost> findByUserId(String userId) {
        return userPostMapper.selectByUserId(userId);
    }

    @Override
    public List<SysUserPost> findByPostId(Long postId) {
        return userPostMapper.selectByPostId(postId);
    }

    @Override
    public int removeByUserId(String userId) {
        return userPostMapper.deleteByUserId(userId);
    }

    @Override
    public int countByPostId(Long postId) {
        return userPostMapper.countByPostId(postId);
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    @Transactional
    public void assignPostsToUser(String userId, List<Long> postIds) {
        // 先删除用户原有的岗位关联
        userPostMapper.deleteByUserId(userId);
        // 批量插入新的岗位关联
        for (Long postId : postIds) {
            SysUserPost userPost = new SysUserPost();
            userPost.setUserId(userId);
            userPost.setPostId(postId);
            userPostMapper.insert(userPost);
        }
    }
}
