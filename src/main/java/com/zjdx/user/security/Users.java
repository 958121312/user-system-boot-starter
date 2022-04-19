package com.zjdx.user.security;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zjdx.user.security.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户实体
 * @author yuntian
 * @since 2022-02-21
 */
@Data
@Accessors(chain = true)
@TableName("sys_users")
public class Users extends BaseEntity {
    /**
     * 用户代码
     */
    private String userCode;

    /**
     * 用户中文名
     */
    private String userAbbrName;

    /**
     * 账号
     */
    private String username;

    /**
     * 绑定的手机号
     */
    private String phoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型(0.账号密码 1.手机号 2.身份证)
     */
    private Integer accountType;

    /**
     * 标志
     * 第1位: 是否可用
     * 第2位: 是否锁定
     * 第3位: 是否过期
     */
    private String flag;

}
