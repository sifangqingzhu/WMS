package org.example.dto;

/**
 * 创建部门请求DTO
 */
public class CreateDepartmentRequest {

    /**
     * 部门名称（必填）
     */
    private String departmentName;

    /**
     * 所属公司ID（必填）
     */
    private Long companyId;

    /**
     * 父部门ID（可选，为空则为根部门）
     */
    private Long parentId;

    // Getters and Setters
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

