package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.SysDepartment;

import java.util.List;

/**
 * 部门 Mapper 接口
 */
@Mapper
@SuppressWarnings("unused")
public interface DepartmentMapper extends BaseMapper<SysDepartment> {

    /**
     * 根据公司ID查询所有部门
     */
    @Select("SELECT * FROM sys_department WHERE company_id = #{companyId} ORDER BY level, department_id")
    List<SysDepartment> selectByCompanyId(@Param("companyId") String companyId);

    /**
     * 根据父部门ID查询子部门
     */
    @Select("SELECT * FROM sys_department WHERE dep_tree LIKE CONCAT('%_', #{parentId}, '_%') OR dep_tree LIKE CONCAT('%_', #{parentId})")
    List<SysDepartment> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 更新部门的dep_tree字段
     */
    @Update("UPDATE sys_department SET dep_tree = #{depTree} WHERE department_id = #{departmentId}")
    int updateDepTree(@Param("departmentId") Long departmentId, @Param("depTree") String depTree);
}


