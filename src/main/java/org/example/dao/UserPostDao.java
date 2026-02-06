package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.SysUserPost;
import org.example.mapper.UserPostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户岗位关联 DAO 层
 */
@Component
public class UserPostDao {

    private static final Logger log = LoggerFactory.getLogger(UserPostDao.class);

    private final UserPostMapper userPostMapper;

    public UserPostDao(UserPostMapper userPostMapper) {
        this.userPostMapper = userPostMapper;
    }

    /**
     * 根据用户ID查询关联
     */
    public List<SysUserPost> selectByUserId(String userId) {
        log.debug("DAO: selectByUserId, userId={}", userId);
        return userPostMapper.selectByUserId(userId);
    }

    /**
     * 根据岗位ID查询关联
     */
    public List<SysUserPost> selectByPostId(Long postId) {
        log.debug("DAO: selectByPostId, postId={}", postId);
        return userPostMapper.selectByPostId(postId);
    }

    /**
     * 插入用户岗位关联
     */
    public int insert(SysUserPost userPost) {
        log.debug("DAO: insert, userId={}, postId={}", userPost.getUserId(), userPost.getPostId());
        return userPostMapper.insert(userPost);
    }

    /**
     * 批量插入用户岗位关联
     */
    public void batchInsert(String userId, List<Long> postIds) {
        log.debug("DAO: batchInsert, userId={}, postIds={}", userId, postIds);
        if (postIds != null && !postIds.isEmpty()) {
            for (Long postId : postIds) {
                SysUserPost userPost = new SysUserPost(userId, postId);
                userPostMapper.insert(userPost);
            }
        }
    }

    /**
     * 删除用户的所有岗位关联
     */
    public int deleteByUserId(String userId) {
        log.debug("DAO: deleteByUserId, userId={}", userId);
        return userPostMapper.deleteByUserId(userId);
    }

    /**
     * 删除岗位的所有用户关联
     */
    public int deleteByPostId(Long postId) {
        log.debug("DAO: deleteByPostId, postId={}", postId);
        return userPostMapper.deleteByPostId(postId);
    }

    /**
     * 删除指定用户的指定岗位关联
     */
    public int deleteByUserIdAndPostId(String userId, Long postId) {
        log.debug("DAO: deleteByUserIdAndPostId, userId={}, postId={}", userId, postId);
        QueryWrapper<SysUserPost> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("post_id", postId);
        return userPostMapper.delete(wrapper);
    }

    /**
     * 统计岗位关联的用户数量
     */
    public int countByPostId(Long postId) {
        log.debug("DAO: countByPostId, postId={}", postId);
        return userPostMapper.countByPostId(postId);
    }

    /**
     * 统计用户关联的岗位数量
     */
    public long countByUserId(String userId) {
        log.debug("DAO: countByUserId, userId={}", userId);
        QueryWrapper<SysUserPost> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return userPostMapper.selectCount(wrapper);
    }

    /**
     * 检查用户是否拥有某岗位
     */
    public boolean existsByUserIdAndPostId(String userId, Long postId) {
        log.debug("DAO: existsByUserIdAndPostId, userId={}, postId={}", userId, postId);
        QueryWrapper<SysUserPost> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
               .eq("post_id", postId);
        return userPostMapper.selectCount(wrapper) > 0;
    }
}

