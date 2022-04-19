package com.zjdx.user.security.annotation;

import java.lang.annotation.*;

/**
 * BaseEntity子类上添加该注解将会忽视自动填充属性
 * @author yuntian
 * @since 2022-02-23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdFillIgnore {
}
