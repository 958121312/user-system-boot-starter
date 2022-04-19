package com.zjdx.user.security.config;


/**
 * @author yuntian
 * @since 2022-02-21
 * 白名单过滤接口
 */
public interface SecurityWhiteMetadataSource {
    String[] getAttributes();
}
