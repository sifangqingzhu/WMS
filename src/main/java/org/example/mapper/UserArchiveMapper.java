package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.SysUserArchive;

import java.util.List;

/**
 * 用户归档表 Mapper 接口
 */
@Mapper
public interface UserArchiveMapper extends BaseMapper<SysUserArchive> {

    @Select("SELECT * FROM sys_user_archive WHERE user_id = #{userId} ORDER BY archived_at DESC")
    List<SysUserArchive> selectByUserId(@Param("userId") String userId);

    @Delete("DELETE FROM sys_user_archive WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") String userId);
}

