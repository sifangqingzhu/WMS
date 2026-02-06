package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.annotation.RequirePermission;
import org.example.dto.*;
import org.example.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限管理控制器
 */
@RestController
@RequestMapping("/api/menu")
@Tag(name = "菜单权限管理", description = "菜单增删改查及权限分配接口")
public class MenuController {

    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 创建菜单
     */
    @PostMapping
    @Operation(summary = "创建菜单", description = "创建新菜单/页面/按钮/字段权限")
    @RequirePermission("system:menu:add")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(@RequestBody CreateMenuRequest request) {
        log.info("创建菜单: name={}, code={}", request.getMenuName(), request.getMenuCode());

        if (request.getMenuName() == null || request.getMenuName().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "菜单名称不能为空"));
        }
        if (request.getMenuCode() == null || request.getMenuCode().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "菜单代码不能为空"));
        }
        if (request.getMenuType() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "菜单类型不能为空"));
        }

        try {
            MenuResponse response = menuService.createMenu(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("菜单创建成功", response));
        } catch (Exception e) {
            log.error("菜单创建失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "菜单创建失败: " + e.getMessage()));
        }
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{menuId}")
    @Operation(summary = "获取菜单详情")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenu(@PathVariable Long menuId) {
        MenuResponse response = menuService.getMenuById(menuId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "菜单不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功", response));
    }

    /**
     * 获取所有菜单列表
     */
    @GetMapping
    @Operation(summary = "获取所有菜单列表")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getAllMenus() {
        List<MenuResponse> menus = menuService.getAllMenus();
        return ResponseEntity.ok(ApiResponse.success("获取成功", menus));
    }

    /**
     * 获取菜单树
     */
    @GetMapping("/tree")
    @Operation(summary = "获取菜单树", description = "获取完整菜单树结构")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenuTree() {
        List<MenuResponse> tree = menuService.getMenuTree();
        return ResponseEntity.ok(ApiResponse.success("获取成功", tree));
    }

    /**
     * 获取用户菜单树
     */
    @GetMapping("/user/{userId}/tree")
    @Operation(summary = "获取用户菜单树", description = "根据用户权限获取菜单树")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getUserMenuTree(@PathVariable String userId) {
        List<MenuResponse> tree = menuService.getUserMenuTree(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", tree));
    }

    /**
     * 获取用户动态路由
     */
    @GetMapping("/user/{userId}/routes")
    @Operation(summary = "获取用户动态路由", description = "获取用户的前端动态路由配置")
    public ResponseEntity<ApiResponse<List<UserRouteResponse>>> getUserRoutes(@PathVariable String userId) {
        List<UserRouteResponse> routes = menuService.getUserRoutes(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", routes));
    }

    /**
     * 获取用户权限代码列表
     */
    @GetMapping("/user/{userId}/permissions")
    @Operation(summary = "获取用户权限代码列表", description = "获取用户拥有的所有权限代码")
    public ResponseEntity<ApiResponse<List<String>>> getUserPermissions(@PathVariable String userId) {
        List<String> permissions = menuService.getUserPermissions(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", permissions));
    }

    /**
     * 检查用户权限
     */
    @GetMapping("/user/{userId}/check")
    @Operation(summary = "检查用户权限", description = "检查用户是否拥有某权限代码")
    public ResponseEntity<ApiResponse<Boolean>> checkPermission(
            @PathVariable String userId,
            @RequestParam String menuCode) {
        boolean hasPermission = menuService.hasPermission(userId, menuCode);
        return ResponseEntity.ok(ApiResponse.success("检查完成", hasPermission));
    }

    /**
     * 获取岗位权限列表
     */
    @GetMapping("/post/{postId}")
    @Operation(summary = "获取岗位权限列表", description = "获取岗位拥有的所有菜单权限")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getPostMenus(@PathVariable Long postId) {
        List<MenuResponse> menus = menuService.getPostMenus(postId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", menus));
    }

    /**
     * 分配岗位权限
     */
    @PostMapping("/assign")
    @Operation(summary = "分配岗位权限", description = "为岗位分配菜单权限")
    @RequirePermission("system:menu:assign")
    public ResponseEntity<ApiResponse<Void>> assignMenuPermissions(@RequestBody AssignMenuPermRequest request) {
        log.info("分配岗位权限: postId={}", request.getPostId());

        if (request.getPostId() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "岗位ID不能为空"));
        }

        try {
            menuService.assignMenuPermissions(request);
            return ResponseEntity.ok(ApiResponse.success("权限分配成功", null));
        } catch (Exception e) {
            log.error("权限分配失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "权限分配失败: " + e.getMessage()));
        }
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    @Operation(summary = "删除菜单", description = "删除菜单（需先删除子菜单）")
    @RequirePermission("system:menu:delete")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable Long menuId) {
        log.info("删除菜单: menuId={}", menuId);

        try {
            boolean success = menuService.deleteMenu(menuId);
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("删除成功", null));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "删除失败，菜单不存在"));
            }
        } catch (Exception e) {
            log.error("删除失败: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        }
    }
}
