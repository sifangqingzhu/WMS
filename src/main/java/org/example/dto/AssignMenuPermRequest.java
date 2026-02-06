package org.example.dto;

import java.util.List;

/**
 * 分配岗位权限请求DTO
 */
public class AssignMenuPermRequest {

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 菜单权限列表
     */
    private List<MenuPermItem> permissions;

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public List<MenuPermItem> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<MenuPermItem> permissions) {
        this.permissions = permissions;
    }

    /**
     * 菜单权限项
     */
    public static class MenuPermItem {
        private Long menuId;
        /**
         * 权限类型: 1-只读 2-读写 3-完全控制
         */
        private Integer permType;

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
    }
}
