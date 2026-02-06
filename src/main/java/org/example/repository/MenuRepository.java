package org.example.repository;

import org.example.entity.SysMenu;

import java.util.List;

/**
 * 菜单 Repository 接口
 */
public interface MenuRepository {

    /**
     * 保存菜单
     */
    SysMenu save(SysMenu menu);

    /**
     * 根据ID查询
     */
    SysMenu findById(Long menuId);

    /**
     * 根据菜单代码查询
     */
    SysMenu findByMenuCode(String menuCode);

    /**
     * 查询所有菜单
     */
    List<SysMenu> findAll();

    /**
     * 查询根菜单
     */
    List<SysMenu> findRootMenus();

    /**
     * 根据父ID查询子菜单
     */
    List<SysMenu> findByParentId(Long parentId);

    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> findByUserId(String userId);

    /**
     * 根据岗位ID查询菜单
     */
    List<SysMenu> findByPostId(Long postId);

    /**
     * 查询可见菜单
     */
    List<SysMenu> findVisibleMenus();

    /**
     * 根据类型查询
     */
    List<SysMenu> findByMenuType(Integer menuType);

    /**
     * 更新菜单
     */
    boolean update(SysMenu menu);


    /**
     * 删除菜单
     */
    boolean deleteById(Long menuId);

    /**
     * 检查是否存在
     */
    boolean existsById(Long menuId);

    /**
     * 检查代码是否存在
     */
    boolean existsByMenuCode(String menuCode);

    /**
     * 统计子菜单数量
     */
    long countChildren(Long parentId);
}
