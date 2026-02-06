package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.SysDepartment;
import org.example.mapper.DepartmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 部门 DAO 层
 */
@Component
public class DepartmentDao {

    private static final Logger log = LoggerFactory.getLogger(DepartmentDao.class);

    private final DepartmentMapper departmentMapper;

    public DepartmentDao(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /**
     * 根据ID查询部门
     */
    public SysDepartment selectById(Long departmentId) {
        log.debug("DAO: selectById, departmentId={}", departmentId);
        return departmentMapper.selectById(departmentId);
    }

    /**
     * 查询所有部门
     */
    public List<SysDepartment> selectAll() {
        log.debug("DAO: selectAll");
        return departmentMapper.selectList(null);
    }

    /**
     * 根据公司ID查询部门列表
     */
    public List<SysDepartment> selectByCompanyId(Long companyId) {
        log.debug("DAO: selectByCompanyId, companyId={}", companyId);
        return departmentMapper.selectByCompanyId(companyId);
    }

    /**
     * 根据父部门ID查询子部门
     */
    public List<SysDepartment> selectByParentId(Long parentId) {
        log.debug("DAO: selectByParentId, parentId={}", parentId);
        return departmentMapper.selectByParentId(parentId);
    }

    /**
     * 根据部门名称和公司ID查询
     */
    public SysDepartment selectByNameAndCompanyId(String departmentName, Long companyId) {
        log.debug("DAO: selectByNameAndCompanyId, departmentName={}, companyId={}", departmentName, companyId);
        QueryWrapper<SysDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("department_name", departmentName)
               .eq("company_id", companyId);
        return departmentMapper.selectOne(wrapper);
    }

    /**
     * 插入部门
     */
    public int insert(SysDepartment department) {
        log.debug("DAO: insert, departmentName={}", department.getDepartmentName());
        return departmentMapper.insert(department);
    }

    /**
     * 更新部门
     */
    public int updateById(SysDepartment department) {
        log.debug("DAO: updateById, departmentId={}", department.getDepartmentId());
        return departmentMapper.updateById(department);
    }

    /**
     * 更新部门树路径
     */
    public int updateDepTree(Long departmentId, String depTree) {
        log.debug("DAO: updateDepTree, departmentId={}, depTree={}", departmentId, depTree);
        return departmentMapper.updateDepTree(departmentId, depTree);
    }

    /**
     * 删除部门
     */
    public int deleteById(Long departmentId) {
        log.debug("DAO: deleteById, departmentId={}", departmentId);
        return departmentMapper.deleteById(departmentId);
    }

    /**
     * 检查部门是否存在
     */
    public boolean existsById(Long departmentId) {
        log.debug("DAO: existsById, departmentId={}", departmentId);
        return departmentMapper.selectById(departmentId) != null;
    }

    /**
     * 统计公司下的部门数量
     */
    public long countByCompanyId(Long companyId) {
        log.debug("DAO: countByCompanyId, companyId={}", companyId);
        QueryWrapper<SysDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id", companyId);
        return departmentMapper.selectCount(wrapper);
    }

    /**
     * 根据层级查询部门
     */
    public List<SysDepartment> selectByLevel(Integer level) {
        log.debug("DAO: selectByLevel, level={}", level);
        QueryWrapper<SysDepartment> wrapper = new QueryWrapper<>();
        wrapper.eq("level", level);
        return departmentMapper.selectList(wrapper);
    }
}

