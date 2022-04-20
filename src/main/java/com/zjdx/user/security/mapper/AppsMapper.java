package com.zjdx.user.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjdx.user.security.entity.Apps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

/**
 * 应用持久层访问接口
 * @author yuntian
 * @since 2022-02-22
 */
@Repository
@ConditionalOnMissingBean(AppsMapper.class)
public interface AppsMapper extends BaseMapper<Apps> {
    Apps queryAppsByClientId(String clientId);
}
