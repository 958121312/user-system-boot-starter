package com.zjdx.user.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;


/**
 * 安全应用程序对象
 * @author yuntian
 * @since 2021-09-22
 */
@Data
public class SecurityClientDetails extends Apps implements ClientDetails {

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    private Set<GrantedAuthority> authorities;

    public SecurityClientDetails(Integer accessTokenValiditySeconds, Integer refreshTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    /**
     * 客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: "unity-resource,mobile-resource".
     * 资源服务器可以有多个，我们可以为每一个Resource Server（一个微服务实例）设置一个resourceid。
     * Authorization Server给client第三方客户端授权的时候，可以设置这个client可以访问哪一些Resource Server资源服务，如果没设置，就是对所有的Resource Server都有访问权限。
     * @return
     */
    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    /**
     * 用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成).
     * 对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appSecret,与client_secret是同一个概念.
     * @return
     */
    @Override
    public String getClientSecret() {
        return this.getSecret();
    }


    @Override
    public boolean isScoped() {
        return true;
    }

    /**
     * 指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔,如: "read,write".
     * @EnableGlobalMethodSecurity(prePostEnabled = true)启用方法级权限控制
     * 然后在方法上注解标识@PreAuthorize("#oauth2.hasScope('read')")
     * @return
     */
    @Override
    public Set<String> getScope() {
        return new TreeSet<>(Arrays.asList(this.getScopes().split(",")));
    }

    /**
     * 指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials,
     * 若支持多个grant_type用逗号(,)分隔,如: "authorization_code,password".
     * 在实际应用中,当注册时,该字段是一般由服务器端指定的,而不是由申请者去选择的,
     * 最常用的grant_type组合有: "authorization_code,refresh_token"(针对通过浏览器访问的客户端); "password,refresh_token"(针对移动设备的客户端).
     * implicit与client_credentials在实际中很少使用，可以根据自己的需要，在OAuth2.0 提供的地方进行扩展自定义的授权
    * @return
     */
    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return this.getGrantTypes();
    }

    /**
     * 回调地址
     * @return
     */
    @Override
    public Set<String> getRegisteredRedirectUri() {
        return new TreeSet<>(Collections.singletonList(this.getRedirectUrl()));
    }

    /**
     * @PreAuthorize("hasAuthority('admin')")可以在方法上标志 用户或者说client 需要说明样的权限
     * 指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_UNITY,ROLE_USER".
     * @return
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * accessToken有效时间
     * @return
     */
    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    /**
     * refreshToken有效时间
     * @return
     */
    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    /**
     * 设置用户是否自动Approval操作, 默认值为 'false', 可选值包括 'true','false', 'read','write'.
     * 该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为'true'或支持的scope值,则会跳过用户Approve的页面, 直接授权.
     * @param scope
     * @return
     */
    @Override
    public boolean isAutoApprove(String scope) {
        return false;
    }

    /**
     * 按照spring-security-oauth项目中对该字段的描述
     * Additional information for this client, not need by the vanilla OAuth protocol but might be useful, for example,for storing descriptive information.
     * (详见ClientDetails.java的getAdditionalInformation()方法的注释) 在实际应用中, 可以用该字段来存储关于客户端的一些其他信息,如客户端的国家,地区,注册时的IP地址等等.
     * @return
     */
    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
