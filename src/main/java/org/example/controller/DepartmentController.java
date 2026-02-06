package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ApiResponse;
import org.example.dto.CreateDepartmentRequest;
import org.example.dto.DepartmentResponse;
import org.example.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/api/department")
@Tag(name = "部门管理", description = "部门增删改查接口")
public class DepartmentController {

    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 创建部门
     */
    @PostMapping
    @Operation(summary = "创建部门", description = "创建新部门，支持指定父部门形成树形结构")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @RequestBody CreateDepartmentRequest request) {

        log.info("收到创建部门请求: name={}, companyId={}, parentId={}",
                request.getDepartmentName(), request.getCompanyId(), request.getParentId());

        // 参数校验
        if (request.getDepartmentName() == null || request.getDepartmentName().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "部门名称不能为空"));
        }
        if (request.getCompanyId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "公司ID不能为空"));
        }

        try {
            DepartmentResponse response = departmentService.createDepartment(request);
            log.info("部门创建成功: departmentId={}", response.getDepartmentId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("部门创建成功", response));
        } catch (Exception e) {
            log.error("部门创建失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "部门创建失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取部门
     */
    @GetMapping("/{departmentId}")
    @Operation(summary = "获取部门详情", description = "根据部门ID获取部门详情")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(
            @PathVariable Long departmentId) {

        log.info("获取部门详情: departmentId={}", departmentId);

        DepartmentResponse response = departmentService.getDepartmentById(departmentId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "部门不存在"));
        }

        return ResponseEntity.ok(ApiResponse.success("获取成功", response));
    }

    /**
     * 获取公司下所有部门
     */
    @GetMapping("/company/{companyId}")
    @Operation(summary = "获取公司部门列表", description = "根据公司ID获取该公司下所有部门")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentsByCompany(
            @PathVariable Long companyId) {

        log.info("获取公司部门列表: companyId={}", companyId);

        List<DepartmentResponse> departments = departmentService.getDepartmentsByCompanyId(companyId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", departments));
    }

    /**
     * 获取所有部门
     */
    @GetMapping
    @Operation(summary = "获取所有部门", description = "获取系统中所有部门列表")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {

        log.info("获取所有部门列表");

        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("获取成功", departments));
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{departmentId}")
    @Operation(summary = "删除部门", description = "删除指定部门（需先删除子部门）")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long departmentId) {

        log.info("删除部门请求: departmentId={}", departmentId);

        try {
            boolean success = departmentService.deleteDepartment(departmentId);
            if (success) {
                log.info("部门删除成功: departmentId={}", departmentId);
                return ResponseEntity.ok(ApiResponse.success("删除成功", null));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "删除失败，部门不存在"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * 获取公司部门树
     */
    @GetMapping("/company/{companyId}/tree")
    @Operation(summary = "获取公司部门树", description = "获取公司下的部门树形结构")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentTree(
            @PathVariable Long companyId) {

        log.info("获取公司部门树: companyId={}", companyId);

        List<DepartmentResponse> tree = departmentService.getDepartmentTree(companyId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", tree));
    }

    /**
     * 获取子部门列表
     */
    @GetMapping("/{departmentId}/children")
    @Operation(summary = "获取子部门列表", description = "获取指定部门的直接子部门")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getChildDepartments(
            @PathVariable Long departmentId) {

        log.info("获取子部门列表: departmentId={}", departmentId);

        List<DepartmentResponse> children = departmentService.getChildDepartments(departmentId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", children));
    }

    /**
     * 获取父部门
     */
    @GetMapping("/{departmentId}/parent")
    @Operation(summary = "获取父部门", description = "获取指定部门的父部门")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getParentDepartment(
            @PathVariable Long departmentId) {

        log.info("获取父部门: departmentId={}", departmentId);

        DepartmentResponse parent = departmentService.getParentDepartment(departmentId);
        if (parent == null) {
            return ResponseEntity.ok(ApiResponse.success("该部门无父部门", null));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功", parent));
    }

    /**
     * 获取兄弟部门
     */
    @GetMapping("/{departmentId}/siblings")
    @Operation(summary = "获取兄弟部门", description = "获取与指定部门同级的其他部门")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getSiblingDepartments(
            @PathVariable Long departmentId) {

        log.info("获取兄弟部门: departmentId={}", departmentId);

        List<DepartmentResponse> siblings = departmentService.getSiblingDepartments(departmentId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", siblings));
    }

    /**
     * 移动部门
     */
    @PutMapping("/{departmentId}/move")
    @Operation(summary = "移动部门", description = "将部门移动到新的父部门下")
    public ResponseEntity<ApiResponse<Void>> moveDepartment(
            @PathVariable Long departmentId,
            @RequestParam(required = false) Long newParentId) {

        log.info("移动部门: departmentId={}, newParentId={}", departmentId, newParentId);

        try {
            departmentService.moveDepartment(departmentId, newParentId);
            return ResponseEntity.ok(ApiResponse.success("移动成功", null));
        } catch (Exception e) {
            log.error("移动部门失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}

