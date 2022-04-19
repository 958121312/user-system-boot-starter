package com.zjdx.user.security.authority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * 权限拦截器
 * @author yuntian
 * @since 2022-02-22
 */
@Component
public class AuthoritySecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    private FilterInvocationSecurityMetadataSource authorityMetadataSourceFilter;

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.authorityMetadataSourceFilter;
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return this.authorityMetadataSourceFilter;
    }

    //设置自定义的FilterInvocationSecurityMetadataSource
    @Autowired
    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource authorityMetadataSourceFilter) {
        this.authorityMetadataSourceFilter = authorityMetadataSourceFilter;
    }

    @Override
    @Autowired
    public void setAccessDecisionManager(AccessDecisionManager roleAccessDecisionManager) {
        super.setAccessDecisionManager(roleAccessDecisionManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    public void invoke(FilterInvocation fi) throws IOException {
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } catch (ServletException e) {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }
}
