package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysUser;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    default SysUser selectByUsername(String username) {
        return selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, username));
    }

    default SysUser selectByUserId(String userId) {
        return selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId));
    }

    default SysUser selectActiveByUserId(String userId) {
        return selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getIsDelete, false));
    }

    default List<SysUser> selectByStatus(String status) {
        return selectList(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getStatus, status));
    }

    default long countByUsername(String username) {
        return selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, username));
    }

    default long countActiveById(String userId) {
        return selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getIsDelete, false));
    }

    default int deleteByUserId(String userId) {
        return delete(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId));
    }

    default int softDeleteById(String userId) {
        SysUser user = new SysUser();
        user.setIsDelete(true);
        return update(user, new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getUserId, userId));
    }

    default int restoreById(String userId) {
        SysUser user = new SysUser();
        user.setIsDelete(false);
        return update(user, new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getUserId, userId));
    }
}
