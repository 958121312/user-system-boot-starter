package com.zjdx.user.security.authority;

import com.zjdx.user.security.Authority;
import com.zjdx.user.security.Roles;
import com.zjdx.user.security.config.SecurityWhiteMetadataSource;
import com.zjdx.user.security.mapper.AuthorityMapper;
import com.zjdx.user.security.mapper.RolesMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 权限信息获取过滤器
 * @author yuntian
 * @since 2022-02-21
 */
@Component
@AllArgsConstructor
public class AuthorityMetadataSourceFilter implements FilterInvocationSecurityMetadataSource {

    private final RolesMapper rolesMapper;

    private final AuthorityMapper authorityMapper;

    private final SecurityWhiteMetadataSource securityWhiteMetadataSource;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取当前访问url
        HttpServletRequest request = ((FilterInvocation) o).getRequest();
        List<String> whiteList = new ArrayList<>(Arrays.asList(securityWhiteMetadataSource.getAttributes()));
        whiteList.add("/oauth/**");
        /* 过滤白名单请求 */
        for (String url : whiteList) {
            if(new AntPathRequestMatcher(url).matches(request)) {
                return null;
            }
        }

        AtomicReference<AntPathRequestMatcher> matcher = new AtomicReference<>();
        List<Authority> authorityList = authorityMapper.selectList(null);
        for (Authority authority : authorityList) {
            matcher.set(new AntPathRequestMatcher(authority.getUrl()));
            if(matcher.get().matches(request)) {
                List<Roles> roles = rolesMapper.queryRolesByAuthority(authority.getId());
                return roles.stream().map(role -> new SecurityConfig(role.getRoleCode())).collect(Collectors.toList());
            }
        }
        return SecurityConfig.createList("ROLE_ANONYMOUS");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
