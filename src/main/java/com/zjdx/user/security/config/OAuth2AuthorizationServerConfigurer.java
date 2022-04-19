package com.zjdx.user.security.config;

import com.zjdx.user.security.OauthAccessTokenConverter;
import com.zjdx.user.security.UserTokenEnhancer;
import com.zjdx.user.security.exception.UserAccountWebResponseExceptionTranslator;
import com.zjdx.user.security.properties.UserSystemProperties;
import com.zjdx.user.security.utils.RSAEncrypt;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

/**
 * OAuth2认证服务配置类
 * @author yuntian
 * @since 2022-02-21
 */
@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class OAuth2AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {
    private final UserDetailsService userDetailsServiceImpl;

    private final UserTokenEnhancer userTokenEnhancer;


    private final AuthenticationManager authenticationManager;

    private final UserSystemProperties userSystemProperties;


    private final UserAccountWebResponseExceptionTranslator userAccountWebResponseExceptionTranslator;

    private final ClientDetailsService clientDetailsServiceImpl;

    private final RSAEncrypt rsaEncrypt;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("274cdf0d3f105330e3bee68a11bff970lkVLCg6JliV")
                .secret(rsaEncrypt.encode("962464"))
                .authorizedGrantTypes("password")
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()");
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager);
        endpoints.userDetailsService(userDetailsServiceImpl);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(userTokenEnhancer,accessTokenConverter()));
        endpoints.tokenEnhancer(tokenEnhancerChain);
        endpoints.exceptionTranslator(userAccountWebResponseExceptionTranslator);
    }

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("fea768819d75457ebd1fdf7f7fd72337");
        converter.setAccessTokenConverter(new OauthAccessTokenConverter(userDetailsServiceImpl));
        return converter;
    }

    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenStore(tokenStore());

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(userTokenEnhancer,accessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        tokenServices.setAccessTokenValiditySeconds(userSystemProperties.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(userSystemProperties.getRefreshTokenValiditySeconds());
        return tokenServices;
    }

}
