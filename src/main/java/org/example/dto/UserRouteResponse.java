package org.example.dto;

import java.util.List;

/**
 * 用户路由响应DTO
 * 用于前端动态路由生成
 */
public class UserRouteResponse {

    /**
     * 路由路径
     */
    private String path;

    /**
     * 路由名称（唯一标识）
     */
    private String name;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 路由元信息
     */
    private RouteMeta meta;

    /**
     * 子路由
     */
    private List<UserRouteResponse> children;

    // Getters and Setters
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public RouteMeta getMeta() {
        return meta;
    }

    public void setMeta(RouteMeta meta) {
        this.meta = meta;
    }

    public List<UserRouteResponse> getChildren() {
        return children;
    }

    public void setChildren(List<UserRouteResponse> children) {
        this.children = children;
    }

    /**
     * 路由元信息
     */
    public static class RouteMeta {
        private String title;
        private String icon;
        private Boolean hidden;
        private List<String> permissions;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Boolean getHidden() {
            return hidden;
        }

        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }
    }
}
