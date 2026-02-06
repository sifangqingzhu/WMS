package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.SysPost;
import org.example.mapper.PostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 岗位 DAO 层
 */
@Component
public class PostDao {

    private static final Logger log = LoggerFactory.getLogger(PostDao.class);

    private final PostMapper postMapper;

    public PostDao(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    /**
     * 根据ID查询岗位
     */
    public SysPost selectById(Long postId) {
        log.debug("DAO: selectById, postId={}", postId);
        return postMapper.selectById(postId);
    }

    /**
     * 查询所有岗位
     */
    public List<SysPost> selectAll() {
        log.debug("DAO: selectAll");
        return postMapper.selectList(null);
    }

    /**
     * 根据部门ID查询岗位列表
     */
    public List<SysPost> selectByDepartmentId(Long departmentId) {
        log.debug("DAO: selectByDepartmentId, departmentId={}", departmentId);
        return postMapper.selectByDepartmentId(departmentId);
    }

    /**
     * 根据用户ID查询岗位列表
     */
    public List<SysPost> selectByUserId(String userId) {
        log.debug("DAO: selectByUserId, userId={}", userId);
        return postMapper.selectByUserId(userId);
    }

    /**
     * 根据岗位名称查询
     */
    public SysPost selectByPostName(String postName) {
        log.debug("DAO: selectByPostName, postName={}", postName);
        QueryWrapper<SysPost> wrapper = new QueryWrapper<>();
        wrapper.eq("post_name", postName);
        return postMapper.selectOne(wrapper);
    }

    /**
     * 根据部门ID和岗位名称查询
     */
    public SysPost selectByDepartmentIdAndPostName(Long departmentId, String postName) {
        log.debug("DAO: selectByDepartmentIdAndPostName, departmentId={}, postName={}", departmentId, postName);
        QueryWrapper<SysPost> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", departmentId)
               .eq("post_name", postName);
        return postMapper.selectOne(wrapper);
    }

    /**
     * 插入岗位
     */
    public int insert(SysPost post) {
        log.debug("DAO: insert, postName={}", post.getPostName());
        return postMapper.insert(post);
    }

    /**
     * 更新岗位
     */
    public int updateById(SysPost post) {
        log.debug("DAO: updateById, postId={}", post.getPostId());
        return postMapper.updateById(post);
    }

    /**
     * 删除岗位
     */
    public int deleteById(Long postId) {
        log.debug("DAO: deleteById, postId={}", postId);
        return postMapper.deleteById(postId);
    }

    /**
     * 统计部门下的岗位数量
     */
    public long countByDepartmentId(Long departmentId) {
        log.debug("DAO: countByDepartmentId, departmentId={}", departmentId);
        QueryWrapper<SysPost> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", departmentId);
        return postMapper.selectCount(wrapper);
    }
}

