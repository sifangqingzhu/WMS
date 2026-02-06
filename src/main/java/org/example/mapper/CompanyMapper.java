package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysCompany;

import java.util.List;

@Mapper
public interface CompanyMapper extends BaseMapper<SysCompany> {

    default List<SysCompany> selectActiveCompanies() {
        return selectList(new LambdaQueryWrapper<SysCompany>()
                .eq(SysCompany::getIsDelete, 0));
    }

    default SysCompany selectByCompanyCode(String companyCode) {
        return selectOne(new LambdaQueryWrapper<SysCompany>()
                .eq(SysCompany::getCompanyCode, companyCode)
                .eq(SysCompany::getIsDelete, 0));
    }

    default SysCompany selectByCompanyName(String companyName) {
        return selectOne(new LambdaQueryWrapper<SysCompany>()
                .eq(SysCompany::getCompanyName, companyName)
                .eq(SysCompany::getIsDelete, 0));
    }

    default long countActiveById(Long companyId) {
        return selectCount(new LambdaQueryWrapper<SysCompany>()
                .eq(SysCompany::getCompanyId, companyId)
                .eq(SysCompany::getIsDelete, 0));
    }

    default int softDeleteById(Long companyId) {
        SysCompany company = new SysCompany();
        company.setIsDelete(1);
        return update(company, new LambdaUpdateWrapper<SysCompany>()
                .eq(SysCompany::getCompanyId, companyId));
    }
}
