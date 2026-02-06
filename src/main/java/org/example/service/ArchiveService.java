package org.example.service;

import org.example.entity.SysUser;
import org.example.entity.SysUserArchive;
import org.example.repository.UserArchiveRepository;
import org.example.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final UserArchiveRepository userArchiveRepository;

    public ArchiveService(UserRepository userRepository, UserArchiveRepository userArchiveRepository) {
        this.userRepository = userRepository;
        this.userArchiveRepository = userArchiveRepository;
    }

    /**
     * 归档用户（软删除）
     */
    @Transactional
    public boolean archiveUser(String userId) {
        log.info("归档用户: userId={}", userId);

        SysUser user = userRepository.findById(userId);
        if (user == null) {
            log.warn("归档失败: 用户不存在, userId={}", userId);
            return false;
        }

        SysUserArchive archive = SysUserArchive.fromUser(user);
        userArchiveRepository.save(archive);

        userRepository.delete(userId);

        log.info("用户归档成功: userId={}", userId);
        return true;
    }

    /**
     * 从归档表恢复用户
     */
    @Transactional
    public boolean restoreUser(String userId) {
        log.info("恢复用户: userId={}", userId);

        List<SysUserArchive> archives = userArchiveRepository.findByUserId(userId);
        if (archives.isEmpty()) {
            log.warn("恢复失败: 归档记录不存在, userId={}", userId);
            return false;
        }

        SysUserArchive archive = archives.get(0);

        if (userRepository.existsById(userId)) {
            log.warn("恢复失败: 用户已存在, userId={}", userId);
            return false;
        }

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
        userRepository.save(user);

        userArchiveRepository.deleteByUserId(userId);

        log.info("用户恢复成功: userId={}", userId);
        return true;
    }

    /**
     * 查询用户归档记录
     */
    public List<SysUserArchive> getArchivedUsers() {
        return userArchiveRepository.findAll();
    }

    /**
     * 检查用户是否已归档
     */
    public boolean isUserArchived(String userId) {
        return userArchiveRepository.existsByUserId(userId);
    }
}
