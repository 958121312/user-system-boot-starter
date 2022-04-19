package com.zjdx.user.security.config;

import com.zjdx.user.security.authority.AuthoritySecurityInterceptor;
import com.zjdx.user.security.utils.RSAEncrypt;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 资源服务器配置类
 * @author yuntian
 * @since 2021-09-26
 */

@Configuration
@AllArgsConstructor
@EnableResourceServer
public class OAuth2ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final AuthoritySecurityInterceptor authoritySecurityInterceptor;

    private final SecurityWhiteMetadataSource securityWhiteMetadataSource;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuth2RequestedMatcher()).authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(securityWhiteMetadataSource.getAttributes()).permitAll()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable();
        http.addFilterBefore(authoritySecurityInterceptor, FilterSecurityInterceptor.class);
    }

    /**
     * 定义OAuth2请求匹配器
     */
    private static class OAuth2RequestedMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader("Authorization");
            //判断来源请求是否包含oauth2授权信息,这里授权信息来源可能是头部的Authorization值以Bearer开头,或者是请求参数中包含access_token参数,满足其中一个则匹配成功
            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
            boolean haveAccessToken = request.getParameter("access_token") != null;
            return haveOauth2Token || haveAccessToken;
        }
    }
}
