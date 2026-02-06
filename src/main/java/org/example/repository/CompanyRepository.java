package org.example.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.SysCompany;

import java.util.List;

/**
 * 公司 Repository 接口
 */
public interface CompanyRepository extends IService<SysCompany> {

    /**
     * 查询所有未删除的公司
     */
    List<SysCompany> findActiveCompanies();

    /**
     * 根据公司编码查询
     */
    SysCompany findByCompanyCode(String companyCode);

    /**
     * 检查公司是否存在
     */
    boolean companyExists(Long companyId);
}

