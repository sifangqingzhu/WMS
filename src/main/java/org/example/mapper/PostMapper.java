package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysPost;

import java.util.List;

/**
 * 岗位 Mapper 接口
 * 所有数据库操作都在这里
 */
@Mapper
public interface PostMapper extends BaseMapper<SysPost> {

    default List<SysPost> selectByDepartmentId(Long departmentId) {
        return selectList(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getDepartmentId, departmentId));
    }

    default SysPost selectByNameAndDepartmentId(String postName, Long departmentId) {
        return selectOne(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostName, postName)
                .eq(SysPost::getDepartmentId, departmentId));
    }

    default long countByDepartmentId(Long departmentId) {
        return selectCount(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getDepartmentId, departmentId));
    }

    default long countById(Long postId) {
        return selectCount(new LambdaQueryWrapper<SysPost>()
                .eq(SysPost::getPostId, postId));
    }

    default List<SysPost> selectByPostIds(List<Long> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapper<SysPost>()
                .in(SysPost::getPostId, postIds));
    }
}
