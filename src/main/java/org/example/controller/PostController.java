package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ApiResponse;
import org.example.dto.AssignPostRequest;
import org.example.dto.CreatePostRequest;
import org.example.dto.PostResponse;
import org.example.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@Tag(name = "岗位管理", description = "岗位（角色）增删改查及用户分配接口")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @Operation(summary = "创建岗位", description = "创建新岗位并关联到指定部门")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody CreatePostRequest request) {
        log.info("创建岗位: name={}, departmentId={}", request.getPostName(), request.getDepartmentId());

        if (request.getPostName() == null || request.getPostName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "岗位名称不能为空"));
        }
        if (request.getDepartmentId() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "部门ID不能为空"));
        }

        try {
            PostResponse response = postService.createPost(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("岗位创建成功", response));
        } catch (Exception e) {
            log.error("岗位创建失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "岗位创建失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{postId}")
    @Operation(summary = "获取岗位详情")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPostById(postId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(404, "岗位不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success("获取成功", response));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "获取部门下所有岗位")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsByDepartment(@PathVariable Long departmentId) {
        List<PostResponse> posts = postService.getPostsByDepartmentId(departmentId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", posts));
    }

    @GetMapping
    @Operation(summary = "获取所有岗位")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success("获取成功", posts));
    }

    @PostMapping("/assign")
    @Operation(summary = "分配用户岗位", description = "为用户分配一个或多个岗位（角色）")
    public ResponseEntity<ApiResponse<Void>> assignPostsToUser(@RequestBody AssignPostRequest request) {
        log.info("分配岗位: userId={}, postIds={}", request.getUserId(), request.getPostIds());

        if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "用户ID不能为空"));
        }

        try {
            postService.assignPostsToUser(request);
            return ResponseEntity.ok(ApiResponse.success("岗位分配成功", null));
        } catch (Exception e) {
            log.error("岗位分配失败: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "岗位分配失败: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户岗位列表", description = "获取指定用户拥有的所有岗位")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPostsByUser(@PathVariable String userId) {
        List<PostResponse> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("获取成功", posts));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "删除岗位")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        try {
            boolean success = postService.deletePost(postId);
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("删除成功", null));
            }
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "岗位不存在"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
    }
}

