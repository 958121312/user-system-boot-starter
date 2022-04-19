package com.zjdx.user.security.utils;

import com.zjdx.user.security.SecurityUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * @Author: yuntian
 * @Date: 2022/02/21
 * 用户工具类
 **/
public class SecurityUtils {
    public static String getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return String.valueOf(authentication.getCredentials());
        } else {
            return "";
        }
    }


    public static SecurityUserDetails getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (SecurityUserDetails)authentication.getPrincipal();
        } else {
            return null;
        }
    }
}
