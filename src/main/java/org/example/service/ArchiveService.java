package org.example.service;

import org.example.dao.UserArchiveDao;
import org.example.dao.UserDao;
import org.example.entity.SysUser;
import org.example.entity.SysUserArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户归档服务
 * 提供用户软删除（归档）和恢复功能
 */
@Service
public class ArchiveService {

    private static final Logger log = LoggerFactory.getLogger(ArchiveService.class);

    private final UserDao userDao;
    private final UserArchiveDao userArchiveDao;

    public ArchiveService(UserDao userDao, UserArchiveDao userArchiveDao) {
        this.userDao = userDao;
        this.userArchiveDao = userArchiveDao;
    }

    /**
     * 归档用户（软删除）
     * 将用户数据移动到归档表，并从原表删除
     */
    @Transactional
    public boolean archiveUser(String userId) {
        log.info("归档用户: userId={}", userId);

        // 1. 查询用户
        SysUser user = userDao.selectByUserId(userId);
        if (user == null) {
            log.warn("归档失败: 用户不存在, userId={}", userId);
            return false;
        }

        // 2. 创建归档记录
        SysUserArchive archive = SysUserArchive.fromUser(user);
        userArchiveDao.insert(archive);

        // 3. 删除原表数据
        userDao.deleteById(userId);

        log.info("用户归档成功: userId={}", userId);
        return true;
    }

    /**
     * 从归档表恢复用户
     */
    @Transactional
    public boolean restoreUser(String userId) {
        log.info("恢复用户: userId={}", userId);

        // 1. 查询归档记录
        List<SysUserArchive> archives = userArchiveDao.selectByUserId(userId);
        if (archives.isEmpty()) {
            log.warn("恢复失败: 归档记录不存在, userId={}", userId);
            return false;
        }

        // 取最新的归档记录
        SysUserArchive archive = archives.get(0);

        // 2. 检查原表是否已存在
        if (userDao.existsById(userId)) {
            log.warn("恢复失败: 用户已存在, userId={}", userId);
            return false;
        }

        // 3. 恢复到原表
        SysUser user = new SysUser();
        user.setUserId(archive.getUserId());
        user.setUserName(archive.getUserName());
        user.setPassword(archive.getPassword());
        user.setEmail(archive.getEmail());
        user.setPhone(archive.getPhone());
        user.setName(archive.getName());
        user.setCompanyId(archive.getCompanyId());
        user.setIsDelete(false);
        user.setIsActivated(archive.getIsActivated());
        user.setIsCloud(archive.getIsCloud());
        user.setStatus(archive.getStatus());
        userDao.insert(user);

        // 4. 删除归档记录
        userArchiveDao.deleteByUserId(userId);

        log.info("用户恢复成功: userId={}", userId);
        return true;
    }

    /**
     * 查询用户归档记录
     */
    public List<SysUserArchive> getArchivedUsers() {
        return userArchiveDao.selectAll();
    }

    /**
     * 检查用户是否已归档
     */
    public boolean isUserArchived(String userId) {
        return userArchiveDao.existsByUserId(userId);
    }
}
