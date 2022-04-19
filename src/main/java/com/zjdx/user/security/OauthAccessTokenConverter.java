package com.zjdx.user.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

/**
 * 重写DefaultAccessTokenConverter接口的方法，为了从SecurityContext中获取用户信息
 * @author Yuntian
 * @since 2022-02-21
 */
public class OauthAccessTokenConverter extends DefaultAccessTokenConverter {
    public OauthAccessTokenConverter(UserDetailsService userService) {
        DefaultUserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();
        converter.setUserDetailsService(userService);
        super.setUserTokenConverter(converter);
    }
}