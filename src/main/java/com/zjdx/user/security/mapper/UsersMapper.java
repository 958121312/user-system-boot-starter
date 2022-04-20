package com.zjdx.user.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.zjdx.user.security.entity.Users;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;


/**
 * 用户持久层访问接口
 * @author yuntian
 * @since 2022-02-21
 */
@Repository
@ConditionalOnMissingBean(UsersMapper.class)
public interface UsersMapper extends BaseMapper<Users> {
    /**
     * 查询用户，根据用户名查找
     * @param username
     * @return 用户名对应的账户
     */
    Users queryUsersByUsername(String username);
}
