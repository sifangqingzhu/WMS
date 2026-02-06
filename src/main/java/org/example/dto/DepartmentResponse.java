package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门响应DTO
 */
public class DepartmentResponse {

    private Long departmentId;
    private String departmentName;
    private Integer level;
    private String depTree;
    private String companyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 解析后的父部门ID列表
     */
    private List<Long> parentIds;

    /**
     * 解析后的子部门ID列表
     */
    private List<Long> childrenIds;

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

    public String getDepTree() {
        return depTree;
    }

    public void setDepTree(String depTree) {
        this.depTree = depTree;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
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

    public List<Long> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<Long> parentIds) {
        this.parentIds = parentIds;
    }

    public List<Long> getChildrenIds() {
        return childrenIds;
    }

    public void setChildrenIds(List<Long> childrenIds) {
        this.childrenIds = childrenIds;
    }
}

