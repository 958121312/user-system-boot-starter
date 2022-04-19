package com.zjdx.user.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yuntian
 * @since 2022-02-22
 */
@Component
@ConfigurationProperties(prefix = "user.system")
@Data
public class UserSystemProperties {
    private Integer accessTokenValiditySeconds = 7200;

    private Integer refreshTokenValiditySeconds = 259200;
}
