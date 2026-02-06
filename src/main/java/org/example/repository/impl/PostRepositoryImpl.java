package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.DepartmentDao;
import org.example.dao.PostDao;
import org.example.entity.SysPost;
import org.example.mapper.PostMapper;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 岗位 Repository 实现类
 * Repository层负责业务逻辑封装，调用DAO层
 */
@Repository
public class PostRepositoryImpl extends ServiceImpl<PostMapper, SysPost> implements PostRepository {

    private final PostDao postDao;
    private final DepartmentDao departmentDao;

    public PostRepositoryImpl(PostDao postDao, DepartmentDao departmentDao) {
        this.postDao = postDao;
        this.departmentDao = departmentDao;
    }

    @Override
    public List<SysPost> findByDepartmentId(Long departmentId) {
        return postDao.selectByDepartmentId(departmentId);
    }

    @Override
    public List<SysPost> findByUserId(String userId) {
        return postDao.selectByUserId(userId);
    }

    @Override
    public boolean departmentExists(Long departmentId) {
        return departmentDao.existsById(departmentId);
    }
}
