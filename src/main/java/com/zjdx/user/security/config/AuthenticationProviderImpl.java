package com.zjdx.user.security.config;

import com.zjdx.user.security.SecurityUserDetails;
import com.zjdx.user.security.exception.UserAccountBadCredentialsException;
import com.zjdx.user.security.utils.RSAEncrypt;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 自定义账号密码验证逻辑
 * @author Yuntian
 * @since 2022-02-21
 */
@Component
@AllArgsConstructor
public class AuthenticationProviderImpl implements AuthenticationProvider {

    private final UserDetailsService userDetailsServiceImpl;

    private final RSAEncrypt rsaEncrypt;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username=authentication.getName();
        String password=authentication.getCredentials().toString();
        SecurityUserDetails account=(SecurityUserDetails)userDetailsServiceImpl.loadUserByUsername(username);
        if(!account.isEnabled()){
            throw new UserAccountBadCredentialsException("帐号已过期!");
        }
        if(!rsaEncrypt.matches(password, account.getPassword())){
            throw new UserAccountBadCredentialsException("用户名或密码错误!");
        }
        return new UsernamePasswordAuthenticationToken(account,account.getPassword(),account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
