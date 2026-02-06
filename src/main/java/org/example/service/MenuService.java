package org.example.service;

import org.example.dto.*;
import org.example.entity.SysMenu;
import org.example.entity.SysPostMenu;
import org.example.repository.MenuRepository;
import org.example.repository.PostMenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限服务类
 */
@Service
public class MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    private final MenuRepository menuRepository;
    private final PostMenuRepository postMenuRepository;

    public MenuService(MenuRepository menuRepository,
                       PostMenuRepository postMenuRepository) {
        this.menuRepository = menuRepository;
        this.postMenuRepository = postMenuRepository;
    }

    /**
     * 创建菜单（邻接表设计，只需设置parent_id）
     */
    @Transactional
    public MenuResponse createMenu(CreateMenuRequest request) {
        log.info("创建菜单: name={}, code={}, type={}",
                request.getMenuName(), request.getMenuCode(), request.getMenuType());

        // 检查菜单代码是否已存在
        if (menuRepository.existsByMenuCode(request.getMenuCode())) {
            throw new RuntimeException("菜单代码已存在: " + request.getMenuCode());
        }

        // 如果指定了父菜单，验证父菜单是否存在
        if (request.getParentId() != null && !menuRepository.existsById(request.getParentId())) {
            throw new RuntimeException("父菜单不存在: " + request.getParentId());
        }

        SysMenu menu = new SysMenu();
        menu.setMenuName(request.getMenuName());
        menu.setMenuType(request.getMenuType());
        menu.setMenuCode(request.getMenuCode());
        menu.setMenuPath(request.getMenuPath());
        menu.setMenuComponent(request.getMenuComponent());
        menu.setMenuIcon(request.getMenuIcon());
        menu.setParentId(request.getParentId());
        menu.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        menu.setIsVisible(request.getIsVisible() != null ? request.getIsVisible() : true);
        menu.setDescription(request.getDescription());

        menuRepository.save(menu);


        log.info("菜单创建成功: menuId={}", menu.getMenuId());
        return convertToResponse(menu);
    }


    /**
     * 根据ID获取菜单
     */
    public MenuResponse getMenuById(Long menuId) {
        SysMenu menu = menuRepository.findById(menuId);
        return menu != null ? convertToResponse(menu) : null;
    }

    /**
     * 获取所有菜单
     */
    public List<MenuResponse> getAllMenus() {
        List<SysMenu> menus = menuRepository.findAll();
        return menus.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    /**
     * 获取菜单树（层级结构）
     */
    public List<MenuResponse> getMenuTree() {
        List<SysMenu> allMenus = menuRepository.findAll();
        return buildTree(allMenus, null);
    }

    /**
     * 获取用户菜单树（根据权限）
     */
    public List<MenuResponse> getUserMenuTree(String userId) {
        log.info("获取用户菜单树: userId={}", userId);
        List<SysMenu> userMenus = menuRepository.findByUserId(userId);

        // 只返回菜单和页面类型（type=1,2），按钮和字段不需要构建路由
        List<SysMenu> routeMenus = userMenus.stream()
                .filter(m -> m.getMenuType() != null && m.getMenuType() <= 2)
                .collect(Collectors.toList());

        return buildTreeWithPerms(routeMenus, null, userId);
    }

    /**
     * 获取用户动态路由
     */
    public List<UserRouteResponse> getUserRoutes(String userId) {
        log.info("获取用户动态路由: userId={}", userId);
        List<SysMenu> userMenus = menuRepository.findByUserId(userId);

        // 只返回菜单和页面类型
        List<SysMenu> routeMenus = userMenus.stream()
                .filter(m -> m.getMenuType() != null && m.getMenuType() <= 2)
                .collect(Collectors.toList());

        // 获取用户所有权限代码（按钮和字段级别）
        List<String> permissions = userMenus.stream()
                .filter(m -> m.getMenuType() != null && m.getMenuType() > 2)
                .map(SysMenu::getMenuCode)
                .collect(Collectors.toList());

        return buildRouteTree(routeMenus, null, permissions);
    }

    /**
     * 获取用户权限代码列表
     */
    public List<String> getUserPermissions(String userId) {
        log.info("获取用户权限代码: userId={}", userId);
        List<SysMenu> userMenus = menuRepository.findByUserId(userId);
        return userMenus.stream()
                .map(SysMenu::getMenuCode)
                .collect(Collectors.toList());
    }

    /**
     * 检查用户是否有某权限
     */
    public boolean hasPermission(String userId, String menuCode) {
        Integer permType = postMenuRepository.findUserPermTypeByCode(userId, menuCode);
        return permType != null && permType > 0;
    }

    /**
     * 检查用户权限类型
     */
    public Integer getUserPermType(String userId, String menuCode) {
        return postMenuRepository.findUserPermTypeByCode(userId, menuCode);
    }

    /**
     * 分配岗位权限
     */
    @Transactional
    public void assignMenuPermissions(AssignMenuPermRequest request) {
        log.info("分配岗位权限: postId={}, permissions.size={}",
                request.getPostId(), request.getPermissions().size());

        // 先删除原有权限
        postMenuRepository.deleteByPostId(request.getPostId());

        // 添加新权限
        List<SysPostMenu> postMenus = request.getPermissions().stream()
                .map(p -> new SysPostMenu(request.getPostId(), p.getMenuId(), p.getPermType()))
                .collect(Collectors.toList());

        postMenuRepository.saveAll(postMenus);

        log.info("岗位权限分配完成: postId={}", request.getPostId());
    }

    /**
     * 获取岗位权限列表
     */
    public List<MenuResponse> getPostMenus(Long postId) {
        List<SysMenu> menus = menuRepository.findByPostId(postId);
        List<SysPostMenu> postMenus = postMenuRepository.findByPostId(postId);

        Map<Long, Integer> permTypeMap = postMenus.stream()
                .collect(Collectors.toMap(SysPostMenu::getMenuId, SysPostMenu::getPermType));

        return menus.stream().map(menu -> {
            MenuResponse response = convertToResponse(menu);
            response.setPermType(permTypeMap.get(menu.getMenuId()));
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 删除菜单
     */
    @Transactional
    public boolean deleteMenu(Long menuId) {
        log.info("删除菜单: menuId={}", menuId);

        // 检查是否有子菜单
        if (menuRepository.countChildren(menuId) > 0) {
            throw new RuntimeException("存在子菜单，无法删除");
        }

        // 删除权限关联
        postMenuRepository.deleteByMenuId(menuId);

        return menuRepository.deleteById(menuId);
    }

    /**
     * 构建树形结构
     */
    private List<MenuResponse> buildTree(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .map(menu -> {
                    MenuResponse response = convertToResponse(menu);
                    response.setChildren(buildTree(menus, menu.getMenuId()));
                    return response;
                })
                .sorted(Comparator.comparing(MenuResponse::getSortOrder,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    /**
     * 构建带权限的树形结构
     */
    private List<MenuResponse> buildTreeWithPerms(List<SysMenu> menus, Long parentId, String userId) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .map(menu -> {
                    MenuResponse response = convertToResponse(menu);
                    Integer permType = postMenuRepository.findUserPermType(userId, menu.getMenuId());
                    response.setPermType(permType);
                    response.setChildren(buildTreeWithPerms(menus, menu.getMenuId(), userId));
                    return response;
                })
                .sorted(Comparator.comparing(MenuResponse::getSortOrder,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    /**
     * 构建路由树（用于前端动态路由）
     */
    private List<UserRouteResponse> buildRouteTree(List<SysMenu> menus, Long parentId, List<String> permissions) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .map(menu -> {
                    UserRouteResponse route = new UserRouteResponse();
                    route.setPath(menu.getMenuPath());
                    route.setName(menu.getMenuCode());
                    route.setComponent(menu.getMenuComponent());

                    UserRouteResponse.RouteMeta meta = new UserRouteResponse.RouteMeta();
                    meta.setTitle(menu.getMenuName());
                    meta.setIcon(menu.getMenuIcon());
                    meta.setHidden(!Boolean.TRUE.equals(menu.getIsVisible()));

                    // 获取当前菜单下的按钮/字段权限
                    List<String> menuPerms = permissions.stream()
                            .filter(p -> p.startsWith(menu.getMenuCode() + ":"))
                            .collect(Collectors.toList());
                    meta.setPermissions(menuPerms);

                    route.setMeta(meta);
                    route.setChildren(buildRouteTree(menus, menu.getMenuId(), permissions));

                    return route;
                })
                .collect(Collectors.toList());
    }

    /**
     * 转换为响应DTO
     */
    private MenuResponse convertToResponse(SysMenu menu) {
        MenuResponse response = new MenuResponse();
        response.setMenuId(menu.getMenuId());
        response.setMenuName(menu.getMenuName());
        response.setMenuType(menu.getMenuType());
        response.setMenuCode(menu.getMenuCode());
        response.setMenuPath(menu.getMenuPath());
        response.setMenuComponent(menu.getMenuComponent());
        response.setMenuIcon(menu.getMenuIcon());
        response.setParentId(menu.getParentId());
        response.setSortOrder(menu.getSortOrder());
        response.setIsVisible(menu.getIsVisible());
        response.setDescription(menu.getDescription());
        return response;
    }
}
