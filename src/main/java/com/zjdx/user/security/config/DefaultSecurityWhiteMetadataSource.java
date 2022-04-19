package com.zjdx.user.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author yuntian
 * @since 2022-02-21
 */
@Component
@ConditionalOnMissingBean(SecurityWhiteMetadataSource.class)
public class DefaultSecurityWhiteMetadataSource implements SecurityWhiteMetadataSource {
    @Override
    public String[] getAttributes() {
        return new String[0];
    }
}
