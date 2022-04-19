package com.zjdx.user.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @version v1.0
 * @author yuntian
 * @date 2022/02/21 16:33
 */
@Configuration
@ConditionalOnMissingBean(TokenEnhancer.class)
public class UserTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        SecurityUserDetails user = (SecurityUserDetails) oAuth2Authentication.getUserAuthentication().getPrincipal();
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
        additionalInfo.put("roles", user.getAuthorities());
        return oAuth2AccessToken;
    }
}
