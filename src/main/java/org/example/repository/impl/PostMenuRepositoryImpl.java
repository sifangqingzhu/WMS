package org.example.repository.impl;

import org.example.entity.SysMenu;
import org.example.entity.SysPostMenu;
import org.example.entity.SysUserPost;
import org.example.mapper.MenuMapper;
import org.example.mapper.PostMenuMapper;
import org.example.mapper.UserPostMapper;
import org.example.repository.PostMenuRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

/**
 * 岗位-菜单关联 Repository 实现类
 * 组装多表查询逻辑
 */
@Repository
public class PostMenuRepositoryImpl implements PostMenuRepository {

    private final PostMenuMapper postMenuMapper;
    private final UserPostMapper userPostMapper;
    private final MenuMapper menuMapper;

    public PostMenuRepositoryImpl(PostMenuMapper postMenuMapper, UserPostMapper userPostMapper, MenuMapper menuMapper) {
        this.postMenuMapper = postMenuMapper;
        this.userPostMapper = userPostMapper;
        this.menuMapper = menuMapper;
    }

    @Override
    public SysPostMenu save(SysPostMenu postMenu) {
        postMenuMapper.insert(postMenu);
        return postMenu;
    }

    @Override
    public void saveAll(List<SysPostMenu> postMenus) {
        for (SysPostMenu postMenu : postMenus) {
            postMenuMapper.insert(postMenu);
        }
    }

    @Override
    public List<SysPostMenu> findByPostId(Long postId) {
        return postMenuMapper.selectByPostId(postId);
    }

    @Override
    public List<SysPostMenu> findByMenuId(Long menuId) {
        return postMenuMapper.selectByMenuId(menuId);
    }

    @Override
    public Integer findPermType(Long postId, Long menuId) {
        SysPostMenu pm = postMenuMapper.selectByPostIdAndMenuId(postId, menuId);
        return pm != null ? pm.getPermType() : null;
    }

    @Override
    public Integer findUserPermType(String userId, Long menuId) {
        // 1. 获取用户的所有岗位
        List<SysUserPost> userPosts = userPostMapper.selectByUserId(userId);
        // 2. 获取这些岗位对该菜单的权限，取最大值
        return userPosts.stream()
                .map(up -> {
                    SysPostMenu pm = postMenuMapper.selectByPostIdAndMenuId(up.getPostId(), menuId);
                    return pm != null ? pm.getPermType() : null;
                })
                .filter(p -> p != null)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    @Override
    public Integer findUserPermTypeByCode(String userId, String menuCode) {
        // 1. 根据 menuCode 查菜单
        SysMenu menu = menuMapper.selectByMenuCode(menuCode);
        if (menu == null) {
            return null;
        }
        // 2. 调用 findUserPermType
        return findUserPermType(userId, menu.getMenuId());
    }

    @Override
    public int deleteByPostId(Long postId) {
        return postMenuMapper.deleteByPostId(postId);
    }

    @Override
    public int deleteByMenuId(Long menuId) {
        return postMenuMapper.deleteByMenuId(menuId);
    }
}
