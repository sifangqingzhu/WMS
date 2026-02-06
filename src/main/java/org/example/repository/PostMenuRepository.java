package org.example.repository;

import org.example.entity.SysPostMenu;

import java.util.List;

/**
 * 岗位-菜单关联 Repository 接口
 */
public interface PostMenuRepository {

    /**
     * 保存权限关联
     */
    SysPostMenu save(SysPostMenu postMenu);

    /**
     * 批量保存权限关联
     */
    void saveAll(List<SysPostMenu> postMenus);

    /**
     * 根据岗位ID查询
     */
    List<SysPostMenu> findByPostId(Long postId);

    /**
     * 根据菜单ID查询
     */
    List<SysPostMenu> findByMenuId(Long menuId);

    /**
     * 查询岗位对某菜单的权限类型
     */
    Integer findPermType(Long postId, Long menuId);

    /**
     * 查询用户对某菜单的最高权限类型
     */
    Integer findUserPermType(String userId, Long menuId);

    /**
     * 查询用户对某菜单代码的最高权限类型
     */
    Integer findUserPermTypeByCode(String userId, String menuCode);

    /**
     * 删除岗位的所有权限
     */
    int deleteByPostId(Long postId);

    /**
     * 删除菜单的所有权限关联
     */
    int deleteByMenuId(Long menuId);
}
