package org.example.repository.impl;

import org.example.dao.DepartmentDao;
import org.example.entity.SysDepartment;
import org.example.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门 Repository 实现类
 * Repository层负责业务逻辑封装，调用DAO层
 */
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private static final Logger log = LoggerFactory.getLogger(DepartmentRepositoryImpl.class);

    private final DepartmentDao departmentDao;

    public DepartmentRepositoryImpl(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public SysDepartment findById(Long departmentId) {
        log.debug("Repository: findById, departmentId={}", departmentId);
        return departmentDao.selectById(departmentId);
    }

    @Override
    public List<SysDepartment> findAll() {
        log.debug("Repository: findAll");
        return departmentDao.selectAll();
    }

    @Override
    public List<SysDepartment> findByCompanyId(Long companyId) {
        log.debug("Repository: findByCompanyId, companyId={}", companyId);
        return departmentDao.selectByCompanyId(companyId);
    }

    @Override
    public List<SysDepartment> findByParentId(Long parentId) {
        log.debug("Repository: findByParentId, parentId={}", parentId);
        return departmentDao.selectByParentId(parentId);
    }

    @Override
    public int save(SysDepartment department) {
        log.debug("Repository: save, departmentName={}", department.getDepartmentName());
        return departmentDao.insert(department);
    }

    @Override
    public int update(SysDepartment department) {
        log.debug("Repository: update, departmentId={}", department.getDepartmentId());
        return departmentDao.updateById(department);
    }

    @Override
    public int updateDepTree(Long departmentId, String depTree) {
        log.debug("Repository: updateDepTree, departmentId={}, depTree={}", departmentId, depTree);
        return departmentDao.updateDepTree(departmentId, depTree);
    }

    @Override
    public int deleteById(Long departmentId) {
        log.debug("Repository: deleteById, departmentId={}", departmentId);
        return departmentDao.deleteById(departmentId);
    }

    @Override
    public boolean existsById(Long departmentId) {
        log.debug("Repository: existsById, departmentId={}", departmentId);
        return departmentDao.existsById(departmentId);
    }

    @Override
    public long countByCompanyId(Long companyId) {
        log.debug("Repository: countByCompanyId, companyId={}", companyId);
        return departmentDao.countByCompanyId(companyId);
    }

    @Override
    public SysDepartment findByNameAndCompanyId(String departmentName, Long companyId) {
        log.debug("Repository: findByNameAndCompanyId, departmentName={}, companyId={}", departmentName, companyId);
        return departmentDao.selectByNameAndCompanyId(departmentName, companyId);
    }
}
