package org.example.annotation;

import java.lang.annotation.*;

/**
 * 软删除注解
 * 标记在方法上，自动将用户数据归档到sys_user_archive表
 * <p>
 * 使用方式：
 * @SoftDelete
 * public boolean deleteUser(String userId) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoftDelete {
}

