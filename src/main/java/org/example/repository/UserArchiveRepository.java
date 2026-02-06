package org.example.repository;

import org.example.entity.SysUserArchive;

import java.util.List;

/**
 * 用户归档 Repository 接口
 */
public interface UserArchiveRepository {

    /**
     * 保存归档记录
     */
    int save(SysUserArchive archive);

    /**
     * 根据用户ID查询归档记录
     */
    List<SysUserArchive> findByUserId(String userId);

    /**
     * 查询所有归档记录
     */
    List<SysUserArchive> findAll();

    /**
     * 根据用户ID删除归档记录
     */
    int deleteByUserId(String userId);

    /**
     * 检查用户是否已归档
     */
    boolean existsByUserId(String userId);
}
