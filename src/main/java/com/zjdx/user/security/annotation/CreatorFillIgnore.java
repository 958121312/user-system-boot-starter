package com.zjdx.user.security.annotation;

import java.lang.annotation.*;

/**
 * 忽略机构创建人填充
 * @author yuntian
 * @since 2022-02-23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CreatorFillIgnore {
}
