package com.zjdx.user.security.base;

/**
 * @Author: Wanshihao
 * @Date: 2022/02/21
 **/
public class BaseException extends RuntimeException{
    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}
