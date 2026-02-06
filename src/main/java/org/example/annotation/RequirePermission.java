package org.example.annotation;

import java.lang.annotation.*;

/**
 * 权限验证注解
 * 用于Controller方法上，验证用户是否拥有指定权限
 * <p>
 * 使用示例：
 * @RequirePermission("system:user:delete")
 * public void deleteUser(Long userId) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 权限代码
     */
    String value();

    /**
     * 是否需要所有权限（多个权限代码时）
     * true: 需要拥有所有权限（AND）
     * false: 拥有任一权限即可（OR）
     */
    boolean requireAll() default true;
}
