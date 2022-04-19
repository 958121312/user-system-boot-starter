package com.zjdx.user.security;

import com.zjdx.user.security.mapper.RolesMapper;
import com.zjdx.user.security.mapper.UsersMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户详情接口实现类
 * @author yuntian
 * @since 2022-02-21
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersMapper usersMapper;

    private final RolesMapper rolesMapper;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users users = usersMapper.queryUsersByUsername(s);
        if(Objects.isNull(users)){
            throw new UsernameNotFoundException("账号或密码错误");
        }

        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        BeanUtils.copyProperties(users, securityUserDetails);
        /* 查询权限 */
        List<Roles> roles = rolesMapper.listRolesByUserId(users.getId());
        securityUserDetails.setAuthorities(roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleCode())).collect(Collectors.toSet()));
        return securityUserDetails;
    }
}
