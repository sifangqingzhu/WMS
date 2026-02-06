package org.example.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.SysPost;

import java.util.List;

/**
 * 岗位 Repository 接口
 */
public interface PostRepository extends IService<SysPost> {

    /**
     * 根据部门ID查询岗位列表
     */
    List<SysPost> findByDepartmentId(Long departmentId);

    /**
     * 根据用户ID查询岗位列表
     */
    List<SysPost> findByUserId(String userId);

    /**
     * 检查部门是否存在
     */
    boolean departmentExists(Long departmentId);

    /**
     * 根据公司ID查询岗位列表（通过部门关联）
     */
    List<SysPost> findByCompanyId(Long companyId);
}

