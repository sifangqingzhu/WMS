package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.SysDepartment;
import org.example.entity.SysPost;
import org.example.entity.SysUserPost;
import org.example.mapper.DepartmentMapper;
import org.example.mapper.PostMapper;
import org.example.mapper.UserPostMapper;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位 Repository 实现类
 * 组装多表查询逻辑
 */
@Repository
public class PostRepositoryImpl extends ServiceImpl<PostMapper, SysPost> implements PostRepository {

    private final PostMapper postMapper;
    private final DepartmentMapper departmentMapper;
    private final UserPostMapper userPostMapper;

    public PostRepositoryImpl(PostMapper postMapper, DepartmentMapper departmentMapper, UserPostMapper userPostMapper) {
        this.postMapper = postMapper;
        this.departmentMapper = departmentMapper;
        this.userPostMapper = userPostMapper;
    }

    @Override
    public List<SysPost> findByDepartmentId(Long departmentId) {
        return postMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<SysPost> findByUserId(String userId) {
        // 1. 查 user_post 表获取 postIds
        List<SysUserPost> userPosts = userPostMapper.selectByUserId(userId);
        List<Long> postIds = userPosts.stream()
                .map(SysUserPost::getPostId)
                .collect(Collectors.toList());
        // 2. 根据 postIds 查岗位
        return postMapper.selectByPostIds(postIds);
    }

    @Override
    public boolean departmentExists(Long departmentId) {
        return departmentMapper.countById(departmentId) > 0;
    }

    @Override
    public List<SysPost> findByCompanyId(Long companyId) {
        // 1. 查该公司的所有部门
        List<SysDepartment> departments = departmentMapper.selectByCompanyId(companyId);
        List<Long> departmentIds = departments.stream()
                .map(SysDepartment::getDepartmentId)
                .toList();
        // 2. 查这些部门下的所有岗位
        if (departmentIds.isEmpty()) {
            return List.of();
        }
        return departmentIds.stream()
                .flatMap(deptId -> postMapper.selectByDepartmentId(deptId).stream())
                .collect(Collectors.toList());
    }
}
