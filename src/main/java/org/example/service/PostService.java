package org.example.service;

import org.example.dto.AssignPostRequest;
import org.example.dto.CreatePostRequest;
import org.example.dto.PostResponse;
import org.example.entity.SysDepartment;
import org.example.entity.SysPost;
import org.example.repository.DepartmentRepository;
import org.example.repository.PostRepository;
import org.example.repository.UserPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserPostRepository userPostRepository;
    private final DepartmentRepository departmentRepository;

    public PostService(PostRepository postRepository,
                       UserPostRepository userPostRepository,
                       DepartmentRepository departmentRepository) {
        this.postRepository = postRepository;
        this.userPostRepository = userPostRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        // 验证部门是否存在
        if (!departmentRepository.existsById(request.getDepartmentId())) {
            throw new RuntimeException("部门不存在");
        }

        SysPost post = new SysPost();
        post.setPostName(request.getPostName());
        post.setDepartmentId(request.getDepartmentId());
        post.setDescription(request.getDescription());

        postRepository.save(post);
        return getPostById(post.getPostId());
    }

    public PostResponse getPostById(Long postId) {
        SysPost post = postRepository.getById(postId);
        if (post == null) {
            return null;
        }
        return convertToResponse(post);
    }

    public List<PostResponse> getPostsByDepartmentId(Long departmentId) {
        List<SysPost> posts = postRepository.findByDepartmentId(departmentId);
        return posts.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<PostResponse> getAllPosts() {
        List<SysPost> posts = postRepository.list();
        return posts.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void assignPostsToUser(AssignPostRequest request) {
        // 验证用户是否存在
        if (!userPostRepository.userExists(request.getUserId())) {
            throw new RuntimeException("用户不存在或已删除");
        }

        userPostRepository.assignPostsToUser(request.getUserId(), request.getPostIds());
    }

    public List<PostResponse> getPostsByUserId(String userId) {
        List<SysPost> posts = postRepository.findByUserId(userId);
        return posts.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByCompanyId(Long companyId) {
        List<SysPost> posts = postRepository.findByCompanyId(companyId);
        return posts.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Transactional
    public boolean deletePost(Long postId) {
        // 检查是否有用户关联此岗位
        int count = userPostRepository.countByPostId(postId);
        if (count > 0) {
            throw new RuntimeException("该岗位下存在用户，无法删除");
        }

        return postRepository.removeById(postId);
    }

    private PostResponse convertToResponse(SysPost post) {
        PostResponse response = new PostResponse();
        response.setPostId(post.getPostId());
        response.setPostName(post.getPostName());
        response.setDepartmentId(post.getDepartmentId());
        response.setDescription(post.getDescription());

        // 获取部门名称
        if (post.getDepartmentId() != null) {
            SysDepartment department = departmentRepository.findById(post.getDepartmentId());
            if (department != null) {
                response.setDepartmentName(department.getDepartmentName());
            }
        }
        return response;
    }
}
