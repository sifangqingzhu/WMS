package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.SysUserPost;

import java.util.List;

/**
 * 用户岗位关联 Mapper 接口
 */
@Mapper
public interface UserPostMapper extends BaseMapper<SysUserPost> {

    /**
     * 根据用户ID查询关联
     */
    @Select("SELECT * FROM sys_user_post WHERE user_id = #{userId}")
    List<SysUserPost> selectByUserId(@Param("userId") String userId);

    /**
     * 根据岗位ID查询关联
     */
    @Select("SELECT * FROM sys_user_post WHERE post_id = #{postId}")
    List<SysUserPost> selectByPostId(@Param("postId") Long postId);

    /**
     * 删除用户的所有岗位关联
     */
    @Delete("DELETE FROM sys_user_post WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") String userId);

    /**
     * 删除岗位的所有用户关联
     */
    @Delete("DELETE FROM sys_user_post WHERE post_id = #{postId}")
    int deleteByPostId(@Param("postId") Long postId);

    /**
     * 统计岗位关联的用户数量
     */
    @Select("SELECT COUNT(*) FROM sys_user_post WHERE post_id = #{postId}")
    int countByPostId(@Param("postId") Long postId);
}

