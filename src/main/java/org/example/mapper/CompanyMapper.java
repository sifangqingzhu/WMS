package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.SysCompany;

import java.util.List;

/**
 * 公司 Mapper 接口
 */
@Mapper
public interface CompanyMapper extends BaseMapper<SysCompany> {

    /**
     * 查询未删除的公司列表
     */
    @Select("SELECT * FROM sys_company WHERE is_delete = 0")
    List<SysCompany> selectActiveCompanies();

    /**
     * 根据公司编码查询
     */
    @Select("SELECT * FROM sys_company WHERE company_code = #{companyCode} AND is_delete = 0")
    SysCompany selectByCompanyCode(@Param("companyCode") String companyCode);
}

