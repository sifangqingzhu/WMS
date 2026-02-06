package org.example.annotation;

import java.lang.annotation.*;

/**
 * 恢复注解
 * 标记在方法上，自动从sys_user_archive表恢复用户数据
 * <p>
 * 使用方式：
 * @RestoreUser
 * public boolean restoreUser(String userId) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestoreUser {
}

