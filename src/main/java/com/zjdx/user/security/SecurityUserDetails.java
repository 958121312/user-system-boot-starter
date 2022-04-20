package com.zjdx.user.security;

import com.zjdx.user.security.entity.Users;
import com.zjdx.user.security.utils.FlagUtil;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * 安全用户对象
 * @author Yuntian
 * @sincec 2022-02-21
 */
@Data
public class SecurityUserDetails extends Users implements UserDetails {
    private Set<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(FlagUtil.flagIndexOf(this.getFlag(),0),"0");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(FlagUtil.flagIndexOf(this.getFlag(),1),"0");
    }
}
