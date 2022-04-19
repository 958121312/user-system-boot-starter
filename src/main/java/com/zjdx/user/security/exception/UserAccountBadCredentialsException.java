package com.zjdx.user.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjdx.user.security.serializer.UserAccountExceptionSerializer;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 坏的凭证
 * @author Yuntian
 * @since 2021-09-22
 */
@JsonSerialize(using = UserAccountExceptionSerializer.class)
public class UserAccountBadCredentialsException extends OAuth2Exception {

    public UserAccountBadCredentialsException(String msg) {
        super(msg);
    }
}
