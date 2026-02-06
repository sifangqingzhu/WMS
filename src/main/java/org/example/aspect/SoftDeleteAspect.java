package org.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.dao.UserArchiveDao;
import org.example.dao.UserDao;
import org.example.entity.SysUser;
import org.example.entity.SysUserArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 软删除切面
 * 拦截带有@SoftDelete和@RestoreUser注解的方法
 */
@Aspect
@Component
@Order(1)
public class SoftDeleteAspect {

    private static final Logger log = LoggerFactory.getLogger(SoftDeleteAspect.class);

    private final UserDao userDao;
    private final UserArchiveDao userArchiveDao;

    public SoftDeleteAspect(UserDao userDao, UserArchiveDao userArchiveDao) {
        this.userDao = userDao;
        this.userArchiveDao = userArchiveDao;
    }

    /**
     * 处理软删除
     * 拦截@SoftDelete注解的方法，将用户归档后删除
     */
    @Around("@annotation(org.example.annotation.SoftDelete)")
    public Object handleSoftDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            log.warn("SoftDelete: 无参数，跳过");
            return joinPoint.proceed();
        }

        String userId = args[0].toString();
        log.info("SoftDelete: 开始归档用户, userId={}", userId);

        // 1. 查询用户
        SysUser user = userDao.selectByUserId(userId);
        if (user == null) {
            log.warn("SoftDelete: 用户不存在, userId={}", userId);
            return false;
        }

        // 2. 创建归档记录
        SysUserArchive archive = SysUserArchive.fromUser(user);
        userArchiveDao.insert(archive);

        // 3. 删除原表数据
        userDao.deleteById(userId);

        log.info("SoftDelete: 用户归档成功, userId={}", userId);
        return true;
    }

    /**
     * 处理恢复
     * 拦截@RestoreUser注解的方法，从归档表恢复用户
     */
    @Around("@annotation(org.example.annotation.RestoreUser)")
    public Object handleRestore(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            log.warn("RestoreUser: 无参数，跳过");
            return joinPoint.proceed();
        }

        String userId = args[0].toString();
        log.info("RestoreUser: 开始恢复用户, userId={}", userId);

        // 1. 查询归档记录
        List<SysUserArchive> archives = userArchiveDao.selectByUserId(userId);
        if (archives.isEmpty()) {
            log.warn("RestoreUser: 归档记录不存在, userId={}", userId);
            return false;
        }

        // 2. 检查原表是否已存在
        if (userDao.existsById(userId)) {
            log.warn("RestoreUser: 用户已存在, userId={}", userId);
            return false;
        }

        // 3. 恢复到原表
        SysUser user = convertArchiveToUser(archives.get(0));
        userDao.insert(user);

        // 4. 删除归档记录
        userArchiveDao.deleteByUserId(userId);

        log.info("RestoreUser: 用户恢复成功, userId={}", userId);
        return true;
    }

    /**
     * 将归档记录转换为用户实体
     */
    private SysUser convertArchiveToUser(SysUserArchive archive) {
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
        return user;
    }
}
