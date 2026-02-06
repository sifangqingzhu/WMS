package org.example.repository.impl;

import org.example.entity.SysUserArchive;
import org.example.mapper.UserArchiveMapper;
import org.example.repository.UserArchiveRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户归档 Repository 实现类
 */
@Repository
public class UserArchiveRepositoryImpl implements UserArchiveRepository {

    private final UserArchiveMapper userArchiveMapper;

    public UserArchiveRepositoryImpl(UserArchiveMapper userArchiveMapper) {
        this.userArchiveMapper = userArchiveMapper;
    }

    @Override
    public int save(SysUserArchive archive) {
        return userArchiveMapper.insert(archive);
    }

    @Override
    public List<SysUserArchive> findByUserId(String userId) {
        return userArchiveMapper.selectByUserId(userId);
    }

    @Override
    public List<SysUserArchive> findAll() {
        return userArchiveMapper.selectList(null);
    }

    @Override
    public int deleteByUserId(String userId) {
        return userArchiveMapper.deleteByUserId(userId);
    }

    @Override
    public boolean existsByUserId(String userId) {
        List<SysUserArchive> archives = userArchiveMapper.selectByUserId(userId);
        return !archives.isEmpty();
    }
}
