package com.zjdx.user.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjdx.user.security.Authority;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

/**
 * 权限持久层访问接口
 * @author yuntian
 * @since 2022-02-21
 */
@Repository
@ConditionalOnMissingBean(AuthorityMapper.class)
public interface AuthorityMapper extends BaseMapper<Authority> {
}
