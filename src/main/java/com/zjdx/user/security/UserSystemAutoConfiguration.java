package com.zjdx.user.security;

import com.zjdx.user.security.properties.UserSystemProperties;
import com.zjdx.user.security.utils.RSAEncrypt;
import lombok.AllArgsConstructor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuntian
 * @since 2022-02-21
 * 用户系统自动装配配置
 */
@Configuration
@AllArgsConstructor
@ComponentScan
@MapperScan(basePackages = { "com.zjdx.user.security.mapper"})
@EnableConfigurationProperties(UserSystemProperties.class)
public class UserSystemAutoConfiguration {

    @Bean
    public RSAEncrypt rsaEncrypt() {
        return new RSAEncrypt();
    }
}
