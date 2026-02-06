package org.example.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.dao.CompanyDao;
import org.example.entity.SysCompany;
import org.example.mapper.CompanyMapper;
import org.example.repository.CompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 公司 Repository 实现类
 * Repository层负责业务逻辑封装，调用DAO层
 */
@Repository
public class CompanyRepositoryImpl extends ServiceImpl<CompanyMapper, SysCompany> implements CompanyRepository {

    private final CompanyDao companyDao;

    public CompanyRepositoryImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public List<SysCompany> findActiveCompanies() {
        return companyDao.selectActiveCompanies();
    }

    @Override
    public SysCompany findByCompanyCode(String companyCode) {
        return companyDao.selectByCompanyCode(companyCode);
    }

    @Override
    public boolean companyExists(String companyId) {
        return companyDao.existsById(companyId);
    }
}
