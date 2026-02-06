package org.example.dto;

import java.util.List;

/**
 * 菜单响应DTO
 */
public class MenuResponse {

    private Long menuId;
    private String menuName;
    private Integer menuType;
    private String menuTypeName;
    private String menuCode;
    private String menuPath;
    private String menuComponent;
    private String menuIcon;
    private Long parentId;
    private Integer sortOrder;
    private Boolean isVisible;
    private String description;

    /**
     * 子菜单列表（用于构建树形结构）
     */
    private List<MenuResponse> children;

    /**
     * 权限类型（用户查询时返回）
     */
    private Integer permType;
    private String permTypeName;

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
        this.menuTypeName = getMenuTypeNameByType(menuType);
    }

    public String getMenuTypeName() {
        return menuTypeName;
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

    public List<MenuResponse> getChildren() {
        return children;
    }

    public void setChildren(List<MenuResponse> children) {
        this.children = children;
    }

    public Integer getPermType() {
        return permType;
    }

    public void setPermType(Integer permType) {
        this.permType = permType;
        this.permTypeName = getPermTypeNameByType(permType);
    }

    public String getPermTypeName() {
        return permTypeName;
    }

    private String getMenuTypeNameByType(Integer type) {
        if (type == null) return null;
        return switch (type) {
            case 1 -> "菜单";
            case 2 -> "页面";
            case 3 -> "按钮";
            case 4 -> "字段";
            default -> "未知";
        };
    }

    private String getPermTypeNameByType(Integer type) {
        if (type == null) return null;
        return switch (type) {
            case 1 -> "只读";
            case 2 -> "读写";
            case 3 -> "完全控制";
            default -> "无权限";
        };
    }
}
