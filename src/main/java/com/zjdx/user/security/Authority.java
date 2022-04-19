package com.zjdx.user.security;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zjdx.user.security.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权限实体
 * @author yuntian
 * @since 2021-09-26
 */
@Data
@Accessors(chain = true)
@TableName("sys_authority")
public class Authority extends BaseEntity {
    /**
     * 权限代码
     */
    private String authorityCode;

    /**
     * 类型(menu、interface)
     */
    private String type;

    /**
     * 权限名
     */
    private String authorityName;

    /**
     * 权限访问地址
     */
    private String url;

}
