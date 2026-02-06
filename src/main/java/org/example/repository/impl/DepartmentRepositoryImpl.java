package org.example.repository.impl;

import org.example.entity.SysDepartment;
import org.example.mapper.DepartmentMapper;
import org.example.repository.DepartmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门 Repository 实现类
 * 直接调用 Mapper 层
 */
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentMapper departmentMapper;

    public DepartmentRepositoryImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public SysDepartment findById(Long departmentId) {
        return departmentMapper.selectById(departmentId);
    }

    @Override
    public List<SysDepartment> findAll() {
        return departmentMapper.selectList(null);
    }

    @Override
    public List<SysDepartment> findByCompanyId(Long companyId) {
        return departmentMapper.selectByCompanyId(companyId);
    }

    @Override
    public List<SysDepartment> findByParentId(Long parentId) {
        return departmentMapper.selectByParentId(parentId);
    }

    @Override
    public int save(SysDepartment department) {
        return departmentMapper.insert(department);
    }

    @Override
    public int update(SysDepartment department) {
        return departmentMapper.updateById(department);
    }

    @Override
    public int deleteById(Long departmentId) {
        return departmentMapper.deleteById(departmentId);
    }

    @Override
    public boolean existsById(Long departmentId) {
        return departmentMapper.countById(departmentId) > 0;
    }

    @Override
    public long countByCompanyId(Long companyId) {
        return departmentMapper.countByCompanyId(companyId);
    }

    @Override
    public SysDepartment findByNameAndCompanyId(String departmentName, Long companyId) {
        return departmentMapper.selectByNameAndCompanyId(departmentName, companyId);
    }
}
