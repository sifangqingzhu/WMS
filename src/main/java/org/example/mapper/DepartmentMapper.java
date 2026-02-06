package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysDepartment;

import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<SysDepartment> {

    default List<SysDepartment> selectByCompanyId(Long companyId) {
        return selectList(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getCompanyId, companyId)
                .orderByAsc(SysDepartment::getLevel)
                .orderByAsc(SysDepartment::getDepartmentId));
    }

    default List<SysDepartment> selectByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getParentId, parentId)
                .orderByAsc(SysDepartment::getDepartmentId));
    }

    default List<SysDepartment> selectRootDepartments(Long companyId) {
        return selectList(new LambdaQueryWrapper<SysDepartment>()
                .isNull(SysDepartment::getParentId)
                .eq(SysDepartment::getCompanyId, companyId)
                .orderByAsc(SysDepartment::getDepartmentId));
    }

    default SysDepartment selectByNameAndCompanyId(String departmentName, Long companyId) {
        return selectOne(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getDepartmentName, departmentName)
                .eq(SysDepartment::getCompanyId, companyId));
    }

    default long countByCompanyId(Long companyId) {
        return selectCount(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getCompanyId, companyId));
    }

    default long countByParentId(Long parentId) {
        return selectCount(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getParentId, parentId));
    }

    default long countById(Long departmentId) {
        return selectCount(new LambdaQueryWrapper<SysDepartment>()
                .eq(SysDepartment::getDepartmentId, departmentId));
    }
}
