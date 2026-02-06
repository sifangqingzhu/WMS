package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysUserPost;

import java.util.List;

@Mapper
public interface UserPostMapper extends BaseMapper<SysUserPost> {

    default List<SysUserPost> selectByUserId(String userId) {
        return selectList(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getUserId, userId));
    }

    default List<SysUserPost> selectByPostId(Long postId) {
        return selectList(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getPostId, postId));
    }

    default int deleteByUserId(String userId) {
        return delete(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getUserId, userId));
    }

    default int deleteByPostId(Long postId) {
        return delete(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getPostId, postId));
    }

    default int countByPostId(Long postId) {
        return Math.toIntExact(selectCount(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getPostId, postId)));
    }
}
