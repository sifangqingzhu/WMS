package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 系统菜单/权限实体类
 *
 * menu_type:
 *   1 - 菜单（目录）
 *   2 - 页面
 *   3 - 按钮
 *   4 - 字段
 *
 * menu_code 规则:
 *   - 菜单: userMgr
 *   - 页面: userMgr:list
 *   - 按钮: userMgr:delete, userMgr:add
 *   - 字段: userMgr:phone:view, userMgr:phone:edit
 */
@TableName("sys_menu")
public class SysMenu {

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
     * 菜单名称
     */
    @TableField("menu_name")
    private String menuName;

    /**
     * 菜单类型: 1-菜单 2-页面 3-按钮 4-字段
     */
    @TableField("menu_type")
    private Integer menuType;

    /**
     * 权限代码
     * 格式: module:action 或 module:field:action
     * 例如: userMgr, userMgr:delete, userMgr:phone:edit
     */
    @TableField("menu_code")
    private String menuCode;

    /**
     * 前端路由路径
     * 例如: /system/user, /system/user/detail/:id
     */
    @TableField("menu_path")
    private String menuPath;

    /**
     * 前端组件路径
     * 例如: system/user/index, system/user/detail
     */
    @TableField("menu_component")
    private String menuComponent;

    /**
     * 菜单图标
     */
    @TableField("menu_icon")
    private String menuIcon;

    /**
     * 父菜单ID（邻接表设计，通过parent_id表达层级关系）
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 是否可见 (true-显示, false-隐藏)
     */
    @TableField("is_visible")
    private Boolean isVisible;

    /**
     * 描述
     */
    @TableField("description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // Constructors
    public SysMenu() {
    }

    // Getters and Setters
    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getMenuComponent() {
        return menuComponent;
    }

    public void setMenuComponent(String menuComponent) {
        this.menuComponent = menuComponent;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
