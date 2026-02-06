package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysUserArchive;

import java.util.List;

@Mapper
public interface UserArchiveMapper extends BaseMapper<SysUserArchive> {

    default List<SysUserArchive> selectByUserId(String userId) {
        return selectList(new LambdaQueryWrapper<SysUserArchive>()
                .eq(SysUserArchive::getUserId, userId)
                .orderByDesc(SysUserArchive::getArchivedAt));
    }

    default int deleteByUserId(String userId) {
        return delete(new LambdaQueryWrapper<SysUserArchive>()
                .eq(SysUserArchive::getUserId, userId));
    }
}
