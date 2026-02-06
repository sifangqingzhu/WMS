package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 用户归档表实体类
 */
@TableName("sys_user_archive")
public class SysUserArchive {

    @TableField("user_id")
    private String userId;

    @TableField("user_name")
    private String userName;

    @TableField("password")
    private String password;

    @TableField("email")
    private String email;

    @TableField("phone")
    private String phone;

    @TableField("name")
    private String name;

    @TableField("company_id")
    private String companyId;

    @TableField("is_delete")
    private Integer isDelete;

    @TableField("is_activated")
    private Integer isActivated;

    @TableField("is_cloud")
    private Integer isCloud;

    @TableField("status")
    private String status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("archived_at")
    private LocalDateTime archivedAt;

    public SysUserArchive() {
    }

    /**
     * 从SysUser创建归档记录
     */
    public static SysUserArchive fromUser(org.example.entity.SysUser user) {
        SysUserArchive archive = new SysUserArchive();
        archive.setUserId(user.getUserId());
        archive.setUserName(user.getUserName());
        archive.setPassword(user.getPassword());
        archive.setEmail(user.getEmail());
        archive.setPhone(user.getPhone());
        archive.setName(user.getName());
        archive.setCompanyId(user.getCompanyId());
        archive.setIsDelete(user.getIsDelete());
        archive.setIsActivated(user.getIsActivated());
        archive.setIsCloud(user.getIsCloud());
        archive.setStatus(user.getStatus());
        archive.setCreatedAt(user.getCreatedAt());
        archive.setUpdatedAt(user.getUpdatedAt());
        archive.setArchivedAt(LocalDateTime.now());
        return archive;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public Integer getIsDelete() { return isDelete; }
    public void setIsDelete(Integer isDelete) { this.isDelete = isDelete; }

    public Integer getIsActivated() { return isActivated; }
    public void setIsActivated(Integer isActivated) { this.isActivated = isActivated; }

    public Integer getIsCloud() { return isCloud; }
    public void setIsCloud(Integer isCloud) { this.isCloud = isCloud; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }
}

