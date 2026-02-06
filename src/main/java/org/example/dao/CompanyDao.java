package org.example.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.SysCompany;
import org.example.mapper.CompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 公司 DAO 层
 */
@Component
public class CompanyDao {

    private static final Logger log = LoggerFactory.getLogger(CompanyDao.class);

    private final CompanyMapper companyMapper;

    public CompanyDao(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    /**
     * 根据ID查询公司
     */
    public SysCompany selectById(String companyId) {
        log.debug("DAO: selectById, companyId={}", companyId);
        return companyMapper.selectById(companyId);
    }

    /**
     * 查询所有公司
     */
    public List<SysCompany> selectAll() {
        log.debug("DAO: selectAll");
        return companyMapper.selectList(null);
    }

    /**
     * 查询所有未删除的公司
     */
    public List<SysCompany> selectActiveCompanies() {
        log.debug("DAO: selectActiveCompanies");
        return companyMapper.selectActiveCompanies();
    }

    /**
     * 根据公司编码查询
     */
    public SysCompany selectByCompanyCode(String companyCode) {
        log.debug("DAO: selectByCompanyCode, companyCode={}", companyCode);
        return companyMapper.selectByCompanyCode(companyCode);
    }

    /**
     * 根据公司名称查询
     */
    public SysCompany selectByCompanyName(String companyName) {
        log.debug("DAO: selectByCompanyName, companyName={}", companyName);
        QueryWrapper<SysCompany> wrapper = new QueryWrapper<>();
        wrapper.eq("company_name", companyName)
               .eq("is_delete", 0);
        return companyMapper.selectOne(wrapper);
    }

    /**
     * 插入公司
     */
    public int insert(SysCompany company) {
        log.debug("DAO: insert, companyName={}", company.getCompanyName());
        return companyMapper.insert(company);
    }

    /**
     * 更新公司
     */
    public int updateById(SysCompany company) {
        log.debug("DAO: updateById, companyId={}", company.getCompanyId());
        return companyMapper.updateById(company);
    }

    /**
     * 软删除公司
     */
    public int softDeleteById(String companyId) {
        log.debug("DAO: softDeleteById, companyId={}", companyId);
        SysCompany company = new SysCompany();
        company.setCompanyId(companyId);
        company.setIsDelete(1);
        return companyMapper.updateById(company);
    }

    /**
     * 检查公司是否存在
     */
    public boolean existsById(String companyId) {
        log.debug("DAO: existsById, companyId={}", companyId);
        return companyMapper.selectById(companyId) != null;
    }

    /**
     * 检查公司编码是否存在
     */
    public boolean existsByCompanyCode(String companyCode) {
        log.debug("DAO: existsByCompanyCode, companyCode={}", companyCode);
        QueryWrapper<SysCompany> wrapper = new QueryWrapper<>();
        wrapper.eq("company_code", companyCode)
               .eq("is_delete", 0);
        return companyMapper.selectCount(wrapper) > 0;
    }
}

