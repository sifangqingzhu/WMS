package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.SysCompany;
import org.example.mapper.CompanyMapper;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公司 Repository 实现类
 * 直接调用 Mapper 层
 */
@Repository
public class CompanyRepositoryImpl extends ServiceImpl<CompanyMapper, SysCompany> implements CompanyRepository {

    private final CompanyMapper companyMapper;

    public CompanyRepositoryImpl(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public List<SysCompany> findActiveCompanies() {
        return companyMapper.selectActiveCompanies();
    }

    @Override
    public SysCompany findByCompanyCode(String companyCode) {
        return companyMapper.selectByCompanyCode(companyCode);
    }

    @Override
    public boolean companyExists(Long companyId) {
        return companyMapper.countActiveById(companyId) > 0;
    }

    @Override
    public SysCompany findById(Long companyId) {
        return companyMapper.selectById(companyId);
    }

    @Override
    public boolean softDeleteById(Long companyId) {
        return companyMapper.softDeleteById(companyId) > 0;
    }
}
