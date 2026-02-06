package org.example.repository.impl;

import org.example.entity.SysMenu;
import org.example.entity.SysPostMenu;
import org.example.entity.SysUserPost;
import org.example.mapper.MenuMapper;
import org.example.mapper.PostMenuMapper;
import org.example.mapper.UserPostMapper;
import org.example.repository.MenuRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单 Repository 实现类
 * 组装多表查询逻辑
 */
@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuMapper menuMapper;
    private final PostMenuMapper postMenuMapper;
    private final UserPostMapper userPostMapper;

    public MenuRepositoryImpl(MenuMapper menuMapper, PostMenuMapper postMenuMapper, UserPostMapper userPostMapper) {
        this.menuMapper = menuMapper;
        this.postMenuMapper = postMenuMapper;
        this.userPostMapper = userPostMapper;
    }

    @Override
    public SysMenu save(SysMenu menu) {
        menuMapper.insert(menu);
        return menu;
    }

    @Override
    public SysMenu findById(Long menuId) {
        return menuMapper.selectById(menuId);
    }

    @Override
    public SysMenu findByMenuCode(String menuCode) {
        return menuMapper.selectByMenuCode(menuCode);
    }

    @Override
    public List<SysMenu> findAll() {
        return menuMapper.selectList(null);
    }

    @Override
    public List<SysMenu> findRootMenus() {
        return menuMapper.selectRootMenus();
    }

    @Override
    public List<SysMenu> findByParentId(Long parentId) {
        return menuMapper.selectByParentId(parentId);
    }

    @Override
    public List<SysMenu> findByUserId(String userId) {
        // 1. 查 user_post 获取用户的岗位IDs
        List<SysUserPost> userPosts = userPostMapper.selectByUserId(userId);
        List<Long> postIds = userPosts.stream()
                .map(SysUserPost::getPostId)
                .collect(Collectors.toList());
        if (postIds.isEmpty()) {
            return List.of();
        }
        // 2. 查 post_menu 获取这些岗位的菜单IDs
        List<Long> menuIds = postIds.stream()
                .flatMap(postId -> postMenuMapper.selectByPostId(postId).stream())
                .map(SysPostMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        // 3. 查菜单
        return menuMapper.selectByMenuIds(menuIds);
    }

    @Override
    public List<SysMenu> findByPostId(Long postId) {
        // 1. 查 post_menu 获取菜单IDs
        List<SysPostMenu> postMenus = postMenuMapper.selectByPostId(postId);
        List<Long> menuIds = postMenus.stream()
                .map(SysPostMenu::getMenuId)
                .collect(Collectors.toList());
        // 2. 查菜单
        return menuMapper.selectByMenuIds(menuIds);
    }

    @Override
    public List<SysMenu> findVisibleMenus() {
        return menuMapper.selectVisibleMenus();
    }

    @Override
    public List<SysMenu> findByMenuType(Integer menuType) {
        return menuMapper.selectByMenuType(menuType);
    }

    @Override
    public boolean update(SysMenu menu) {
        return menuMapper.updateById(menu) > 0;
    }

    @Override
    public boolean deleteById(Long menuId) {
        return menuMapper.deleteById(menuId) > 0;
    }

    @Override
    public boolean existsById(Long menuId) {
        return menuMapper.selectById(menuId) != null;
    }

    @Override
    public boolean existsByMenuCode(String menuCode) {
        return menuMapper.countByMenuCode(menuCode) > 0;
    }

    @Override
    public long countChildren(Long parentId) {
        return menuMapper.countByParentId(parentId);
    }
}
