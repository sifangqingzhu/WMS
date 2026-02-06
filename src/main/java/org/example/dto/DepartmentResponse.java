package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门响应DTO（邻接表设计）
 */
public class DepartmentResponse {

    private Long departmentId;
    private String departmentName;
    private Integer level;
    private Long parentId;
    private Long companyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 子部门列表（用于树形结构）
     */
    private List<DepartmentResponse> children;

    // Getters and Setters
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<DepartmentResponse> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentResponse> children) {
        this.children = children;
    }
}

