package com.zjdx.user.security.utils;

import java.util.UUID;

/**
 * id生成工具
 * @author yuntian
 * @since
 */
public class IdUtil {
    /**
     * 下一个id
     * @return
     */
    public static String nextId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
