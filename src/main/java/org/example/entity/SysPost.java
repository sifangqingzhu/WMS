package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 岗位实体类
 */
@TableName("sys_post")
public class SysPost {

    /**
     * 岗位ID
     */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;

    /**
     * 岗位名称
     */
    @TableField("post_name")
    private String postName;

    /**
     * 所属部门ID
     */
    @TableField("department_id")
    private Long departmentId;

    /**
     * 岗位描述
     */
    @TableField("description")
    private String description;

    // Constructors
    public SysPost() {
    }

    public SysPost(Long postId, String postName, Long departmentId, String description) {
        this.postId = postId;
        this.postName = postName;
        this.departmentId = departmentId;
        this.description = description;
    }

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

