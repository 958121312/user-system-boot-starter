package com.zjdx.user.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 自定义处理OAuth2异常
 * @author Yuntian
 * @since 2021-09-22
 */
@Component
public class UserAccountWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
       if (e instanceof OAuth2Exception) {
            OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
            return ResponseEntity
                    .status(oAuth2Exception.getHttpErrorCode())
                    .body(new UserAccountBadCredentialsException(oAuth2Exception.getMessage()));
        }else if(e instanceof AuthenticationException){
            AuthenticationException authenticationException = (AuthenticationException) e;
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new UserAccountBadCredentialsException(authenticationException.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserAccountBadCredentialsException(e.getMessage()));
    }
}
