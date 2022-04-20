package com.zjdx.user.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjdx.user.security.entity.Roles;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色持久层对象
 * @author yuntian
 * @since 2022-02-21
 */
@Repository
@ConditionalOnMissingBean(RolesMapper.class)
public interface RolesMapper extends BaseMapper<Roles> {
    /**
     * 根据权限查角色
     * @param authorityId
     * @return
     */
    List<Roles> queryRolesByAuthority(String authorityId);

    /**
     * 根据用户id查询角色
     * @param userId
     * @return
     */
    List<Roles> listRolesByUserId(String userId);

}
