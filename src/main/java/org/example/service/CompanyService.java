package org.example.service;

import org.example.dto.CompanyResponse;
import org.example.dto.CreateCompanyRequest;
import org.example.entity.SysCompany;
import org.example.entity.SysUser;
import org.example.repository.CompanyRepository;
import org.example.repository.UserRepository;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        log.info("创建公司: name={}, code={}", request.getCompanyName(), request.getCompanyCode());

        if (request.getCompanyCode() != null && companyRepository.findByCompanyCode(request.getCompanyCode()) != null) {
            throw new RuntimeException("公司编码已存在: " + request.getCompanyCode());
        }

        SysCompany company = new SysCompany();
        company.setCompanyName(request.getCompanyName());
        company.setCompanyCode(request.getCompanyCode());
        company.setCountry(request.getCountry());
        company.setProvince(request.getProvince());
        company.setCity(request.getCity());
        company.setAddress(request.getAddress());
        company.setLinkMan(request.getLinkMan());
        company.setPhone(request.getPhone());
        company.setIsDelete(0);

        companyRepository.save(company);
        log.info("公司创建成功: companyId={}", company.getCompanyId());

        // 自动创建公司管理员
        createCompanyAdmin(company);

        return convertToResponse(company);
    }

    /**
     * 为新创建的公司自动生成公司管理员
     */
    private void createCompanyAdmin(SysCompany company) {
        // 生成管理员用户名：admin_公司编码 或 admin_公司ID
        String adminUsername = "admin_" + (company.getCompanyCode() != null ?
                company.getCompanyCode() : company.getCompanyId());

        // 检查用户名是否已存在
        if (userRepository.existsByUsername(adminUsername)) {
            log.warn("公司管理员用户名已存在: username={}", adminUsername);
            throw new RuntimeException("公司管理员用户名已存在: " + adminUsername);
        }

        // 创建管理员用户
        SysUser adminUser = new SysUser();
        adminUser.setUserId(UUID.randomUUID().toString().replace("-", ""));
        adminUser.setUserName(adminUsername);
        // 默认密码：admin123
        adminUser.setPassword(PasswordUtil.hashPassword("admin123"));
        adminUser.setName(company.getCompanyName() + "-管理员");
        adminUser.setCompanyId(company.getCompanyId());
        adminUser.setEmail(adminUsername + "@" + (company.getCompanyCode() != null ?
                company.getCompanyCode() : company.getCompanyId()) + ".com");
        adminUser.setPhone(company.getPhone());
        adminUser.setStatus("在职");
        adminUser.setIsDelete(false);
        adminUser.setIsActivated(true);
        adminUser.setIsCloud(false);

        // 保存管理员用户
        boolean success = userRepository.save(adminUser);

        if (!success) {
            log.error("公司管理员创建失败: companyId={}", company.getCompanyId());
            throw new RuntimeException("公司管理员创建失败");
        }

        log.info("公司管理员创建成功: companyId={}, userId={}, username={}",
                company.getCompanyId(), adminUser.getUserId(), adminUser.getUserName());
    }

    public CompanyResponse getCompanyById(Long companyId) {
        SysCompany company = companyRepository.findById(companyId);
        if (company == null || company.getIsDelete() == 1) {
            return null;
        }
        return convertToResponse(company);
    }

    public List<CompanyResponse> getAllCompanies() {
        List<SysCompany> companies = companyRepository.findActiveCompanies();
        return companies.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public CompanyResponse getCompanyByCode(String companyCode) {
        SysCompany company = companyRepository.findByCompanyCode(companyCode);
        if (company == null || company.getIsDelete() == 1) {
            return null;
        }
        return convertToResponse(company);
    }

    @Transactional
    public boolean deleteCompany(Long companyId) {
        log.info("软删除公司: companyId={}", companyId);
        return companyRepository.softDeleteById(companyId);
    }

    @Transactional
    public CompanyResponse updateCompany(Long companyId, CreateCompanyRequest request) {
        log.info("更新公司: companyId={}", companyId);

        SysCompany company = companyRepository.findById(companyId);
        if (company == null || company.getIsDelete() == 1) {
            throw new RuntimeException("公司不存在: " + companyId);
        }

        if (request.getCompanyName() != null) company.setCompanyName(request.getCompanyName());
        if (request.getCompanyCode() != null) company.setCompanyCode(request.getCompanyCode());
        if (request.getCountry() != null) company.setCountry(request.getCountry());
        if (request.getProvince() != null) company.setProvince(request.getProvince());
        if (request.getCity() != null) company.setCity(request.getCity());
        if (request.getAddress() != null) company.setAddress(request.getAddress());
        if (request.getLinkMan() != null) company.setLinkMan(request.getLinkMan());
        if (request.getPhone() != null) company.setPhone(request.getPhone());

        companyRepository.updateById(company);
        return convertToResponse(company);
    }

    private CompanyResponse convertToResponse(SysCompany company) {
        CompanyResponse response = new CompanyResponse();
        response.setCompanyId(company.getCompanyId());
        response.setCompanyName(company.getCompanyName());
        response.setCompanyCode(company.getCompanyCode());
        response.setCountry(company.getCountry());
        response.setProvince(company.getProvince());
        response.setCity(company.getCity());
        response.setAddress(company.getAddress());
        response.setLinkMan(company.getLinkMan());
        response.setPhone(company.getPhone());
        response.setCreatedAt(company.getCreatedAt());
        response.setUpdatedAt(company.getUpdatedAt());
        return response;
    }
}
