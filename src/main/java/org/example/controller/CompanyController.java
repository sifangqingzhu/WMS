package org.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ApiResponse;
import org.example.dto.CompanyResponse;
import org.example.dto.CreateCompanyRequest;
import org.example.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")
@Tag(name = "公司管理")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(@RequestBody CreateCompanyRequest request) {
        log.info("创建公司请求: name={}", request.getCompanyName());

        if (request.getCompanyName() == null || request.getCompanyName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "公司名称不能为空"));
        }

        try {
            CompanyResponse response = companyService.createCompany(request);
            return ResponseEntity.ok(ApiResponse.success("创建成功", response));
        } catch (Exception e) {
            log.error("创建公司失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(@PathVariable Long companyId) {
        log.info("获取公司详情: companyId={}", companyId);
        CompanyResponse response = companyService.getCompanyById(companyId);
        if (response == null) {
            return ResponseEntity.ok(ApiResponse.error(404, "公司不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllCompanies() {
        log.info("获取所有公司列表");
        List<CompanyResponse> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(ApiResponse.success("获取成功", companies));
    }

    @GetMapping("/code/{companyCode}")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyByCode(@PathVariable String companyCode) {
        log.info("根据编码获取公司: companyCode={}", companyCode);
        CompanyResponse response = companyService.getCompanyByCode(companyCode);
        if (response == null) {
            return ResponseEntity.ok(ApiResponse.error(404, "公司不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功", response));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @PathVariable Long companyId,
            @RequestBody CreateCompanyRequest request) {
        log.info("更新公司: companyId={}", companyId);
        try {
            CompanyResponse response = companyService.updateCompany(companyId, request);
            return ResponseEntity.ok(ApiResponse.success("更新成功", response));
        } catch (Exception e) {
            log.error("更新公司失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long companyId) {
        log.info("删除公司: companyId={}", companyId);
        boolean result = companyService.deleteCompany(companyId);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success("删除成功", null));
        }
        return ResponseEntity.ok(ApiResponse.error(400, "删除失败"));
    }
}

