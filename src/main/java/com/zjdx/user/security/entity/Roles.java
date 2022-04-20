package com.zjdx.user.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zjdx.user.security.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色实体
 * @author yuntian
 * @since 2021-09-23
 */
@Data
@Accessors(chain = true)
@TableName("sys_roles")
public class Roles extends BaseEntity {

    /**
     * 角色代码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 系统预置角色
     */
    private Boolean defaultRole;

    /**
     * 状态
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String remark;
}
