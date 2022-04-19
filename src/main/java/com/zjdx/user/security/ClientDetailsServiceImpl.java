package com.zjdx.user.security;

import com.zjdx.user.security.mapper.AppsMapper;
import com.zjdx.user.security.properties.UserSystemProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

/**
 *  应用详情业务实现类
 * @author yuntian
 * @since 2022-02-22
 */
@Service
@Data
@AllArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private final UserSystemProperties userSystemProperties;

    private final AppsMapper appsMapper;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Apps apps = appsMapper.queryAppsByClientId(clientId);
        if(Objects.isNull(apps)) {
            throw new ClientRegistrationException("应用不存在");
        }
        SecurityClientDetails securityClientDetails = new SecurityClientDetails(userSystemProperties.getAccessTokenValiditySeconds(), userSystemProperties.getRefreshTokenValiditySeconds());
        /**
         * TODO 暂时不获取任何权限
         */
        securityClientDetails.setAuthorities(Collections.emptySet());
        BeanUtils.copyProperties(apps, securityClientDetails);
        return securityClientDetails;
    }
}
