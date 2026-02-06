package org.example.entity;
import com.baomidou.mybatisplus.annotation.*;
/**
 * 用户岗位关联实体类
 */
@TableName("sys_user_post")
public class SysUserPost {
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 岗位ID
     */
    @TableField("post_id")
    private Long postId;
    // Constructors
    public SysUserPost() {
    }
    public SysUserPost(String userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
