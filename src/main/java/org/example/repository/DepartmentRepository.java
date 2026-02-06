package org.example.repository;

import org.example.entity.SysDepartment;

import java.util.List;

/**
 * 部门 Repository 接口
 */
public interface DepartmentRepository {

    /**
     * 根据ID查询部门
     */
    SysDepartment findById(Long departmentId);

    /**
     * 查询所有部门
     */
    List<SysDepartment> findAll();

    /**
     * 根据公司ID查询部门列表
     */
    List<SysDepartment> findByCompanyId(Long companyId);

    /**
     * 根据父部门ID查询子部门
     */
    List<SysDepartment> findByParentId(Long parentId);

    /**
     * 插入部门
     */
    int save(SysDepartment department);

    /**
     * 更新部门
     */
    int update(SysDepartment department);


    /**
     * 删除部门
     */
    int deleteById(Long departmentId);

    /**
     * 检查部门是否存在
     */
    boolean existsById(Long departmentId);

    /**
     * 统计公司下的部门数量
     */
    long countByCompanyId(Long companyId);

    /**
     * 根据部门名称和公司ID查询
     */
    SysDepartment findByNameAndCompanyId(String departmentName, Long companyId);
}
