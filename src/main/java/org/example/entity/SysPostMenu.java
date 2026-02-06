package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 岗位-菜单权限关联实体类
 *
 * perm_type:
 *   1 - 只读（查看权限）
 *   2 - 读写（编辑权限）
 *   3 - 完全控制（包含删除权限）
 */
@TableName("sys_post_menu")
public class SysPostMenu {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 岗位ID
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 菜单ID
     */
    @TableField("menu_id")
    private Long menuId;

    /**
     * 权限类型: 1-只读 2-读写 3-完全控制
     */
    @TableField("perm_type")
    private Integer permType;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // Constructors
    public SysPostMenu() {
    }

    public SysPostMenu(Long postId, Long menuId, Integer permType) {
        this.postId = postId;
        this.menuId = menuId;
        this.permType = permType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getPermType() {
        return permType;
    }

    public void setPermType(Integer permType) {
        this.permType = permType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
